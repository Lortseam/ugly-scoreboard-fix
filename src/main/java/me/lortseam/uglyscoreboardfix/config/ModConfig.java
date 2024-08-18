package me.lortseam.uglyscoreboardfix.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import lombok.Getter;
import me.lortseam.uglyscoreboardfix.UglyScoreboardFix;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Unique;

import java.awt.Color;
import java.util.Arrays;

@Getter
public class ModConfig {

    @SerialEntry
    private float scale = 1;
    @SerialEntry
    private long maxLineCount = 15;

    @SerialEntry
    private HorizontalPosition horizontalPosition = HorizontalPosition.Right;
    @SerialEntry
    private int YOffset = 0;

    @SerialEntry
    private Color headingForegroundColor;
    @SerialEntry
    private boolean forceHeadingForegroundColor = false;
    @SerialEntry
    private Color headingBackgroundColor = new Color(0, 0, 0, 102);
    @SerialEntry
    private boolean headingShadow = false;

    @SerialEntry
    private Color foregroundColor;

    @SerialEntry
    private boolean forceForegroundColor = false;
    @SerialEntry
    private Color backgroundColor = new Color(0, 0, 0, 76);
    @SerialEntry
    private boolean shadow = false;

    @SerialEntry
    private Color scoreForegroundColor;
    @SerialEntry
    private boolean forceScoreForegroundColor = true;
    @SerialEntry
    private boolean scoreShadow = false;

    @SerialEntry
    private boolean hideSidebar = false;
    @SerialEntry
    private HideScores hideScores = HideScores.Auto;
    @SerialEntry
    private boolean hideTitle = false;

    @SerialEntry
    private boolean hideOnDebug = true;


    {
        assert Formatting.WHITE.getColorValue() != null;
        assert Formatting.RED.getColorValue() != null;
        foregroundColor = new Color(Formatting.WHITE.getColorValue());
        headingForegroundColor = new Color(Formatting.WHITE.getColorValue());
        scoreForegroundColor = new Color(Formatting.RED.getColorValue());
    }


    public void toggleHideSidebar() {
        hideSidebar = !hideSidebar;
        HANDLER.save();
    }

    public static boolean scoresAreConsecutive(ScoreboardObjective objective) {
        long previ = Long.MIN_VALUE;  // Long, so that no score (Int) can match Long.MIN_VALUE
        for (int i : objective.getScoreboard().getScoreboardEntries(objective).stream().mapToInt(ScoreboardEntry::value).limit(UglyScoreboardFix.getConfig().getMaxLineCount()).sorted().toArray()) {
            if (previ != Long.MIN_VALUE && i != previ + 1)
                return false;
            previ = i;
        }
        return objective.getScoreboard().getScoreboardEntries(objective).size() > 1;
    }

    public void toggleHideScores() {
        hideScores = switch (hideScores) {
            case Yes -> HideScores.No;
            case No -> HideScores.Yes;
            case Auto -> {
                if (MinecraftClient.getInstance().world == null)
                    yield HideScores.No;
                ScoreboardObjective objective = MinecraftClient.getInstance().world.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
                yield (objective != null && scoresAreConsecutive(objective)) ? HideScores.No : HideScores.Yes;
            }
        };
        HANDLER.save();
    }

    public enum HorizontalPosition {
        Right, Left
    }

    public enum HideScores {
        Yes, No, Auto
    }
    
    private static Text getHideScoresColoredText(HideScores hs) {
        assert Formatting.RED.getColorValue() != null;
        assert Formatting.GREEN.getColorValue() != null;
        assert Formatting.BLUE.getColorValue() != null;
        return switch (hs) {
            case Yes -> Text.empty().append("Yes").withColor(Formatting.GREEN.getColorValue());
            case No -> Text.empty().append("No").withColor(Formatting.RED.getColorValue());
            case Auto -> Text.empty().append("Auto").withColor(Formatting.BLUE.getColorValue());
        };
    }

    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(new Identifier(UglyScoreboardFix.MODID, "config"))
            .serializer((config) -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(UglyScoreboardFix.MODID).resolve("config.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public YetAnotherConfigLib.Builder getBuilder() {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Ugly Scoreboard Fix Configuration"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Scoreboard Configuration"))
                        .option(Option.<Float>createBuilder()
                                .name(Text.literal("Sidebar scale"))
                                .description(OptionDescription.of(Text.literal("Scale of the scoreboard sidebar, depending on the GUI scale.\n\nDefault is 1.")))
                                .binding(
                                        scale,
                                        this::getScale,
                                        (value) -> scale = value)
                                .controller((opt) -> FloatFieldControllerBuilder.create(opt).range(0f, Float.MAX_VALUE))
                                .build())
                        .option(Option.<Long>createBuilder()
                                .name(Text.literal("Maximum number of lines"))
                                .description(OptionDescription.of(Text.literal("Maximum number of lines the sidebar can have, without counting the title line.\n\nDefault is 15.")))
                                .binding(
                                        maxLineCount,
                                        this::getMaxLineCount,
                                        (value) -> maxLineCount = value)
                                .controller(LongFieldControllerBuilder::create)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Position"))
                                .option(Option.<HorizontalPosition>createBuilder()
                                        .name(Text.literal("Horizontal position"))
                                        .description(OptionDescription.of(Text.literal("Whether to put the sidebar on the right or left side of the screen.\n\nDefault is Right.")))
                                        .binding(
                                                horizontalPosition,
                                                this::getHorizontalPosition,
                                                (value) -> horizontalPosition = value)
                                        .controller((opt) -> EnumControllerBuilder.create(opt).enumClass(HorizontalPosition.class))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.literal("Y offset"))
                                        .description(OptionDescription.of(Text.literal("Vertical offset of the sidebar in pixels, negative goes up and positive goes down.\n\nDefault is 0.")))
                                        .binding(
                                                YOffset,
                                                this::getYOffset,
                                                (value) -> YOffset = value)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Text"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Title foreground color"))
                                        .description(OptionDescription.of(Text.literal("Color of the title text.\n\nDefault is #FFFFFF.")))
                                        .binding(
                                                headingForegroundColor,
                                                this::getHeadingForegroundColor,
                                                (value) -> headingForegroundColor = value)
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Force the title color?"))
                                        .description(OptionDescription.of(Text.literal("Whether to force the color of the title text to be the one selected above or not.\n\nColors for texts and numbers are only applied when their style is unset, setting this option to Yes means that the color will override it not matter what.\n\nDefault is No.")))
                                        .binding(
                                                forceHeadingForegroundColor,
                                                this::isForceHeadingForegroundColor,
                                                (value) -> forceHeadingForegroundColor = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Title background color"))
                                        .description(OptionDescription.of(Text.literal("Color of the background behind the title text.\n\nDefault is #00000066.")))
                                        .binding(
                                                headingBackgroundColor,
                                                this::getHeadingBackgroundColor,
                                                (value) -> headingBackgroundColor = value)
                                        .controller((opt) -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Draw the title shadow?"))
                                        .description(OptionDescription.of(Text.literal("Whether to draw text shadow behind the title text or not.\n\nDefault is No.")))
                                        .binding(
                                                headingShadow,
                                                this::isHeadingShadow,
                                                (value) -> headingShadow = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Text color"))
                                        .description(OptionDescription.of(Text.literal("Color of the scoreboard text.\n\nDefault is #FFFFFF.")))
                                        .binding(
                                                foregroundColor,
                                                this::getForegroundColor,
                                                (value) -> foregroundColor = value)
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Force the text color?"))
                                        .description(OptionDescription.of(Text.literal("Whether to force the color of the scoreboard text to be the one selected above or not.\n\nColors for texts and numbers are only applied when their style is unset, setting this option to Yes means that the color will override it not matter what.\n\nDefault is No.")))
                                        .binding(
                                                forceForegroundColor,
                                                this::isForceForegroundColor,
                                                (value) -> forceForegroundColor = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Text background color"))
                                        .description(OptionDescription.of(Text.literal("Color of the background behind the scoreboard text.\n\nDefault is #0000004C.")))
                                        .binding(
                                                backgroundColor,
                                                this::getBackgroundColor,
                                                (value) -> backgroundColor = value)
                                        .controller((opt) -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Draw the text shadow?"))
                                        .description(OptionDescription.of(Text.literal("Whether to draw text shadow behind the scoreboard text or not.\n\nDefault is No.")))
                                        .binding(
                                                shadow,
                                                this::isShadow,
                                                (value) -> shadow = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Scores color"))
                                        .description(OptionDescription.of(Text.literal("Color of the scores.\n\nDefault is #FF5555.")))
                                        .binding(
                                                scoreForegroundColor,
                                                this::getScoreForegroundColor,
                                                (value) -> scoreForegroundColor = value)
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Force the scores color?"))
                                        .description(OptionDescription.of(Text.literal("Whether to force the color of the scores to be the one selected above or not.\n\nColors for texts and numbers are only applied when their style is unset, setting this option to Yes means that the color will override it not matter what.\n\nDefault is Yes.")))
                                        .binding(
                                                forceScoreForegroundColor,
                                                this::isForceScoreForegroundColor,
                                                (value) -> forceScoreForegroundColor = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Draw the scores shadow?"))
                                        .description(OptionDescription.of(Text.literal("Whether to draw text shadow behind the scores or not.\n\nDefault is No.")))
                                        .binding(
                                                scoreShadow,
                                                this::isScoreShadow,
                                                (value) -> scoreShadow = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Hiding"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide the sidebar?"))
                                        .description(OptionDescription.of(Text.literal("Whether to hide the whole sidebar or not.\n\nDefault is No.")))
                                        .binding(
                                                hideSidebar,
                                                this::isHideSidebar,
                                                (value) -> hideSidebar = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .option(Option.<HideScores>createBuilder()
                                        .name(Text.literal("Hide the scores?"))
                                        .description(OptionDescription.of(Text.literal("Whether to hide the scores or not, where Auto will automatically hide scores when they they are consecutive.\n\nDefault is Auto.")))
                                        .binding(
                                                hideScores,
                                                this::getHideScores,
                                                (value) -> hideScores = value)
                                        .controller((opt) -> EnumControllerBuilder.create(opt).enumClass(HideScores.class).formatValue(ModConfig::getHideScoresColoredText))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide the title?"))
                                        .description(OptionDescription.of(Text.literal("Whether to hide the title of the sidebar or not.\n\nDefault is No.")))
                                        .binding(
                                                hideTitle,
                                                this::isHideTitle,
                                                (value) -> hideTitle = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Hide on debug?"))
                                        .description(OptionDescription.of(Text.literal("Whether to hide the sidebar when the debug screen is shown or not.\n\nDefault is Yes.")))
                                        .binding(
                                                hideOnDebug,
                                                this::isHideOnDebug,
                                                (value) -> hideOnDebug = value)
                                        .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).yesNoFormatter())
                                        .build())
                                .build())
                        .build())
                .save(HANDLER::save);
    }
}
