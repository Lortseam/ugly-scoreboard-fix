package me.lortseam.uglyscoreboardfix.mixin;

import me.lortseam.uglyscoreboardfix.config.HidePart;
import me.lortseam.uglyscoreboardfix.config.HorizontalPosition;
import me.lortseam.uglyscoreboardfix.config.ModConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Unique
    private int xShift;

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void uglyscoreboardfix$hideOrScale(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
        if (ModConfig.Sidebar.Hiding.shouldHide(HidePart.SIDEBAR, objective)) {
            ci.cancel();
            return;
        }
        matrices.push();
        float scale = ModConfig.Sidebar.getScale();
        matrices.scale(scale, scale, scale);
        scaledWidth *= 1 / scale;
        scaledHeight *= 1 / scale;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    private String uglyscoreboardfix$modifyScore(String score, MatrixStack matrices, ScoreboardObjective objective) {
        return ModConfig.Sidebar.Hiding.shouldHide(HidePart.SCORES, objective) ? "" : score;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 2)
    private int uglyscoreboardfix$modifySeperatorWidth(int seperatorWidth, MatrixStack matrices, ScoreboardObjective objective) {
        return ModConfig.Sidebar.Hiding.shouldHide(HidePart.SCORES, objective) ? 0 : seperatorWidth;
    }

    @Redirect(method = "renderScoreboardSidebar", slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I", ordinal = 0))
    private int uglyscoreboardfix$modifyScoreWidth(TextRenderer textRenderer, String score, MatrixStack matrices, ScoreboardObjective objective) {
        return ModConfig.Sidebar.Hiding.shouldHide(HidePart.SCORES, objective) ? 0 : textRenderer.getWidth(score);
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 5)
    private int uglyscoreboardfix$modifyX1(int x1) {
        if (ModConfig.Sidebar.Position.getX() == HorizontalPosition.LEFT) {
            xShift = x1 - 1;
            return 1;
        }
        return x1;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 11)
    private int uglyscoreboardfix$modifyX2(int x2) {
        if (ModConfig.Sidebar.Position.getX() == HorizontalPosition.LEFT) {
            return x2 - xShift;
        }
        return x2;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 3)
    private int uglyscoreboardfix$modifyY(int y) {
        return MathHelper.clamp(y + ModConfig.Sidebar.Position.getYOffset(), (y - scaledHeight / 2) * 3 + 9 + 1, scaledHeight - 1);
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 8)
    private int uglyscoreboardfix$modifyHeadingBackgroundColor(int color) {
        return ModConfig.Sidebar.Background.getHeadingColor();
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE", ordinal = 0), ordinal = 7)
    private int uglyscoreboardfix$modifyBackgroundColor(int color) {
        return ModConfig.Sidebar.Background.getColor();
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I", ordinal = 1))
    private int uglyscoreboardfix$drawHeadingText(TextRenderer textRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        color = ModConfig.Sidebar.Text.getHeadingColor().getRgb();
        if (ModConfig.Sidebar.Text.isHeadingShadow()) {
            return textRenderer.drawWithShadow(matrices, text, x, y, color);
        }
        return textRenderer.draw(matrices, text, x, y, color);
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I", ordinal = 0))
    private int uglyscoreboardfix$drawText(TextRenderer textRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        color = ModConfig.Sidebar.Text.getColor().getRgb();
        if (ModConfig.Sidebar.Text.isShadow()) {
            return textRenderer.drawWithShadow(matrices, text, x, y, color);
        }
        return textRenderer.draw(matrices, text, x, y, color);
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I", ordinal = 0))
    private int uglyscoreboardfix$drawScoreText(TextRenderer textRenderer, MatrixStack matrices, String text, float x, float y, int color) {
        text = Formatting.strip(text);
        color = ModConfig.Sidebar.Text.getScoreColor().getRgb();
        if (ModConfig.Sidebar.Text.isScoreShadow()) {
            return textRenderer.drawWithShadow(matrices, text, x, y, color);
        }
        return textRenderer.draw(matrices, text, x, y, color);
    }

    @ModifyConstant(method = "renderScoreboardSidebar", constant = @Constant(intValue = 15))
    private int uglyscoreboardfix$modifyMaxLineCount(int maxLineCount) {
        return ModConfig.Sidebar.getMaxLineCount();
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("TAIL"))
    private void uglyscoreboardfix$pop(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
        matrices.pop();
        float scale = ModConfig.Sidebar.getScale();
        scaledWidth *= scale;
        scaledHeight *= scale;
    }

}
