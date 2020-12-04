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
public class UglyScoreboardFix implements ConfigOwner, ModMenuApi {

    private static Config config = new Config();
    private static ConfigHandler configHandler;

    public static Config.Hide determineFix(ScoreboardObjective objective) {
        if (!config.getType().test(objective)) {
            return null;
        }
        return config.getHide();
    }

    @Override
    public void onInitializeClientConfig(ConfigBuilder builder) {
        configHandler = builder.add(config).finish();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configHandler.buildScreen(parent);
    }

}
