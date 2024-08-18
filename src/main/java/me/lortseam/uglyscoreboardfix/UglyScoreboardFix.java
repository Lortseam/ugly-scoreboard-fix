package me.lortseam.uglyscoreboardfix;

import lombok.Getter;
import me.lortseam.uglyscoreboardfix.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer {

    public static final String MODID = "uglyscoreboardfix";

    @Getter
    private static ModConfig config = null;

    @Override
    public void onInitializeClient() {
        ModConfig.HANDLER.load();
        config = ModConfig.HANDLER.instance();

        KeyBinding sidebarKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Toggle Sidebar Visibility", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Ugly Scoreboard Fix"));
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            while (sidebarKeybinding.wasPressed())
                config.toggleHideSidebar();
        });

        KeyBinding scoresKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Toggle Scores Visibility", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Ugly Scoreboard Fix"));
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            while (scoresKeybinding.wasPressed()) {
                config.toggleHideScores();
            }
        });
    }
}
