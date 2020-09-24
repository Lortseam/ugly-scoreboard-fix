package me.lortseam.uglyscoreboardfix;

import io.github.prospector.modmenu.api.ModMenuApi;
import lombok.Getter;
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
    @Getter
    private static UglyScoreboardFix instance;

    @Getter
    private ConfigManager configManager;
    @ConfigEntry
    private Type type = Type.CONSECUTIVE_ORDER;
    @ConfigEntry
    private Hide hide = Hide.SCORES;

    @Override
    public void onInitializeClient() {
        instance = this;
        configManager = CompleteConfig.register(MOD_ID);
        configManager.register(this);
    }

    public Hide getHide(ScoreboardObjective objective) {
        if (!type.test(objective)) {
            return null;
        }
        return hide;
    }

}
