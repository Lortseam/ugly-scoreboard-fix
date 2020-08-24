package me.lortseam.uglyscoreboardfix.modmenu;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.lortseam.uglyscoreboardfix.UglyScoreboardFix;

public final class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> UglyScoreboardFix.getInstance().getConfigManager().buildScreen(parent);
    }

}
