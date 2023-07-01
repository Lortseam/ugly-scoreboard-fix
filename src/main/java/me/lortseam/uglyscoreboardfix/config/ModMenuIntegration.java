package me.lortseam.uglyscoreboardfix.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import me.lortseam.uglyscoreboardfix.UglyScoreboardFix;

public class ModMenuIntegration implements ModMenuApi {

    private static final ClothConfigScreenBuilder configScreenBuilder = new ClothConfigScreenBuilder();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configScreenBuilder.build(parent, UglyScoreboardFix.getConfig());
    }

}
