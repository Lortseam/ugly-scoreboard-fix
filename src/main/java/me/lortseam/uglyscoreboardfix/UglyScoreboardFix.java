package me.lortseam.uglyscoreboardfix;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.lortseam.completeconfig.ConfigBuilder;
import me.lortseam.completeconfig.ConfigHandler;
import me.lortseam.completeconfig.api.ConfigOwner;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UglyScoreboardFix implements ConfigOwner, ModMenuApi {

    private static ConfigHandler configHandler;

    @Override
    public void onInitializeClientConfig(ConfigBuilder builder) {
        configHandler = builder.add(Config.getInstance()).finish();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configHandler.buildScreen(parent);
    }

}
