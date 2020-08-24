package me.lortseam.uglyscoreboardfix.mixin;

import me.lortseam.uglyscoreboardfix.UglyScoreboardFix;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE"))
    private String uglyscoreboardfix$modifyScore(String score, MatrixStack matrices, ScoreboardObjective objective) {
        return UglyScoreboardFix.getInstance().shouldHideScores(objective) ? "" : score;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE"), ordinal = 2)
    private int uglyscoreboardfix$modifySeperatorWidth(int seperatorWidth, MatrixStack matrices, ScoreboardObjective objective) {
        return UglyScoreboardFix.getInstance().shouldHideScores(objective) ? 0 : seperatorWidth;
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I", ordinal = 1))
    private int uglyscoreboardfix$modifyScoreWidth(TextRenderer textRenderer, String score, MatrixStack matrices, ScoreboardObjective objective) {
        return UglyScoreboardFix.getInstance().shouldHideScores(objective) ? 0 : textRenderer.getWidth(score);
    }

}
