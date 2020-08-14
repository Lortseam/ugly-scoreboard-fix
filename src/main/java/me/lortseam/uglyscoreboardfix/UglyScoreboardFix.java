package me.lortseam.uglyscoreboardfix;

import lombok.Getter;
import me.lortseam.completeconfig.CompleteConfig;
import me.lortseam.completeconfig.ConfigManager;
import me.lortseam.completeconfig.api.ConfigCategory;
import me.lortseam.completeconfig.api.ConfigEntry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer, ConfigCategory {

    private static final String MOD_ID = "uglyscoreboardfix";
    @Getter
    private static UglyScoreboardFix instance;

    private final ConfigManager configManager = CompleteConfig.register(MOD_ID);
    @Getter
    @ConfigEntry
    private boolean enabled = true;

    @Override
    public void onInitializeClient() {
        instance = this;
        configManager.register(this);
    }

}
