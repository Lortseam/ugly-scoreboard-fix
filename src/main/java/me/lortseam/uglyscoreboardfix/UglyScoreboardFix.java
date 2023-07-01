package me.lortseam.uglyscoreboardfix;

import lombok.Getter;
import me.lortseam.uglyscoreboardfix.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer {

    public static final String MOD_ID = "uglyscoreboardfix";

    @Getter
    private static final ModConfig config = new ModConfig();

    @Override
    public void onInitializeClient() {
        config.load();
    }

}
