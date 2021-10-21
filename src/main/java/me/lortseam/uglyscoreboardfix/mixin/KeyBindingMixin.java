package me.lortseam.uglyscoreboardfix.mixin;

import me.lortseam.uglyscoreboardfix.config.Settings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public final class KeyBindingMixin {

    @Inject(method = "onKeyPressed", at = @At("RETURN"))
    private static void uglyscoreboardfix$onKey(InputUtil.Key key, CallbackInfo ci) {
        if (key == Settings.Sidebar.Hiding.getToggleKeyBind()) {
            Settings.Sidebar.Hiding.toggle();
        }
    }

}
