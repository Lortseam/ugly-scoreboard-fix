package me.lortseam.uglyscoreboardfix.mixin;

import me.lortseam.uglyscoreboardfix.config.ModConfig;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {

    @Inject(method = "onKeyPressed", at = @At("RETURN"))
    private static void uglyscoreboardfix$onKey(InputUtil.Key key, CallbackInfo ci) {
        if (key == ModConfig.Sidebar.Hiding.getToggleKeyBind()) {
            ModConfig.Sidebar.Hiding.toggle();
        }
    }

}
