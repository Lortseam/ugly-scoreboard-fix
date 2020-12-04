package me.lortseam.uglyscoreboardfix;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.lortseam.completeconfig.ConfigBuilder;
import me.lortseam.completeconfig.ConfigHandler;
import me.lortseam.completeconfig.api.ConfigCategory;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigOwner;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.scoreboard.ScoreboardObjective;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ConfigOwner, ConfigCategory, ModMenuApi {

    private static ConfigHandler configHandler;
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
    public void onInitializeClientConfig(ConfigBuilder builder) {
        configHandler = builder.add(this).finish();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configHandler.buildScreen(parent);
    }

}
