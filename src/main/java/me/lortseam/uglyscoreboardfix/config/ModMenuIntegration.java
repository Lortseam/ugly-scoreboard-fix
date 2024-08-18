package me.lortseam.uglyscoreboardfix.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.lortseam.uglyscoreboardfix.UglyScoreboardFix;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> UglyScoreboardFix.getConfig().getBuilder().build().generateScreen(parent);
    }
}
