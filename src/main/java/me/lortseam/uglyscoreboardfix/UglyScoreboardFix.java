package me.lortseam.uglyscoreboardfix;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.lortseam.uglyscoreboardfix.config.Settings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer {

    public static final String MOD_ID = "uglyscoreboardfix";

    @Override
    public void onInitializeClient() {
        new Settings().load();
        if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
            ConfigScreenBuilder.setMain(MOD_ID, new ClothConfigScreenBuilder());
        }
    }

}
