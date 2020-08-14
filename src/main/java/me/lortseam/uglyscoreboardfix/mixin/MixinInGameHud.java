package me.lortseam.uglyscoreboardfix.mixin;

import me.lortseam.uglyscoreboardfix.UglyScoreboardFix;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE"))
    private String uglyscoreboardfix$modifyScore(String score) {
        return UglyScoreboardFix.getInstance().isEnabled() ? "" : score;
    }

}
