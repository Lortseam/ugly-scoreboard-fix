package me.lortseam.uglyscoreboardfix;

import me.lortseam.uglyscoreboardfix.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer {

    public static final String MOD_ID = "uglyscoreboardfix";

    @Override
    public void onInitializeClient() {
        Config.register();
    }

}
