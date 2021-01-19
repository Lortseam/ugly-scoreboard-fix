package me.lortseam.uglyscoreboardfix.mixin;

import me.lortseam.uglyscoreboardfix.config.Config;
import me.lortseam.uglyscoreboardfix.HidePart;
import me.lortseam.uglyscoreboardfix.SidebarPosition;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private int xShift;

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void uglyscoreboardfix$modifySidebar(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
        if (Config.sidebar.hiding.shouldHide(HidePart.SIDEBAR, objective)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At("STORE"))
    private String uglyscoreboardfix$modifyScore(String score, MatrixStack matrices, ScoreboardObjective objective) {
        return Config.sidebar.hiding.shouldHide(HidePart.SCORES, objective) ? "" : score;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At("STORE"), ordinal = 2)
    private int uglyscoreboardfix$modifySeperatorWidth(int seperatorWidth, MatrixStack matrices, ScoreboardObjective objective) {
        return Config.sidebar.hiding.shouldHide(HidePart.SCORES, objective) ? 0 : seperatorWidth;
    }

    @Redirect(method = "renderScoreboardSidebar", slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I", ordinal = 0))
    private int uglyscoreboardfix$modifyScoreWidth(TextRenderer textRenderer, String score, MatrixStack matrices, ScoreboardObjective objective) {
        return Config.sidebar.hiding.shouldHide(HidePart.SCORES, objective) ? 0 : textRenderer.getWidth(score);
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 5)
    private int uglyscoreboardfix$modifyX1(int x1) {
        if (Config.sidebar.getPosition() == SidebarPosition.LEFT) {
            xShift = x1;
            return 2;
        }
        return x1;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 11)
    private int uglyscoreboardfix$modifyX2(int x2) {
        if (Config.sidebar.getPosition() == SidebarPosition.LEFT) {
            return x2 - xShift;
        }
        return x2;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 8)
    private int uglyscoreboardfix$modifyHeadingBackgroundColor(int color) {
        return Config.sidebar.background.getHeadingColor();
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 7)
    private int uglyscoreboardfix$modifyBackgroundColor(int color) {
        return Config.sidebar.background.getColor();
    }

    @ModifyArg(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I", ordinal = 1), index = 4)
    private int uglyscoreboardfix$modifyHeadingColor(int color) {
        return Config.sidebar.text.getHeadingColor().getRgb();
    }

    @ModifyArg(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I", ordinal = 0), index = 4)
    private int uglyscoreboardfix$modifyTextColor(int color) {
        return Config.sidebar.text.getColor().getRgb();
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I", ordinal = 0))
    private int uglyscoreboardfix$modifyScoreColor(TextRenderer textRenderer, MatrixStack matrices, String text, float x, float y, int color) {
        return textRenderer.draw(matrices, Formatting.strip(text), x, y, Config.sidebar.text.getScoreColor().getRgb());
    }

}
