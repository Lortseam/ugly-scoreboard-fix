package me.lortseam.uglyscoreboardfix;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.lortseam.completeconfig.CompleteConfig;
import me.lortseam.completeconfig.ConfigManager;
import me.lortseam.completeconfig.api.ConfigCategory;
import me.lortseam.completeconfig.api.ConfigEntry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.scoreboard.ScoreboardObjective;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ClientModInitializer, ConfigCategory, ModMenuApi {

    private static final String MOD_ID = "uglyscoreboardfix";

    private static ConfigManager configManager;
    @ConfigEntry
    private static Type type = Type.CONSECUTIVE_ORDER;
    @ConfigEntry
    private static Hide hide = Hide.SCORES;

    public static Hide getHide(ScoreboardObjective objective) {
        if (!type.test(objective)) {
            return null;
        }
        return hide;
    }

    @Override
    public void onInitializeClient() {
        configManager = CompleteConfig.register(MOD_ID);
        configManager.register(this);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configManager.buildScreen(parent);
    }

}
