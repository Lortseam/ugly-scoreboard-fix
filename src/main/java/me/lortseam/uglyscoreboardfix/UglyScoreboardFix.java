package me.lortseam.uglyscoreboardfix;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.lortseam.uglyscoreboardfix.config.Settings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer {

    public static final String MOD_ID = "uglyscoreboardfix";

    @Override
    public void onInitializeClient() {
        new Settings().load();
        ConfigScreenBuilder.setMain(MOD_ID, new ClothConfigScreenBuilder());
    }

}
