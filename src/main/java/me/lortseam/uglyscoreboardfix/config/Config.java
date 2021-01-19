package me.lortseam.uglyscoreboardfix.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lortseam.completeconfig.ConfigHandler;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
import me.lortseam.uglyscoreboardfix.HidePart;
import me.lortseam.uglyscoreboardfix.SidebarPosition;
import me.lortseam.uglyscoreboardfix.UglyScoreboardFix;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Config {

    @Getter(AccessLevel.PACKAGE)
    private static ConfigHandler handler;
    public static final Sidebar sidebar = new Sidebar();

    public static void register() {
        handler = me.lortseam.completeconfig.data.Config.builder(UglyScoreboardFix.MOD_ID)
                .add(sidebar)
                .build();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Sidebar implements ConfigGroup {

        public final Background background = new Background();
        public final Hiding hiding = new Hiding();

        @Getter
        @ConfigEntry(comment = "RIGHT (default) or LEFT")
        private SidebarPosition position = SidebarPosition.RIGHT;

        @Override
        public boolean isConfigPOJO() {
            return true;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Background implements ConfigGroup {

            @Getter
            @ConfigEntry.Color(alphaMode = true)
            private int headingColor = 1711276032;
            @Getter
            @ConfigEntry.Color(alphaMode = true)
            private int color = 1275068416;

            @Override
            public boolean isConfigPOJO() {
                return true;
            }

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Hiding implements ConfigGroup {

            @ConfigEntry(comment = "ENABLED or DISABLED or AUTO (only enabled when scores are in consecutive order)")
            private State state = State.AUTO;
            @ConfigEntry(comment = "SCORES or SIDEBAR")
            private HidePart hidePart = HidePart.SCORES;

            public boolean shouldHide(HidePart hidePart, ScoreboardObjective objective) {
                return hidePart == this.hidePart && state.test(objective);
            }

            @Override
            public boolean isConfigPOJO() {
                return true;
            }

            private enum State {

                ENABLED() {
                    @Override
                    boolean test(ScoreboardObjective objective) {
                        return true;
                    }
                },
                AUTO() {
                    @Override
                    boolean test(ScoreboardObjective objective) {
                        int[] scores = objective.getScoreboard().getAllPlayerScores(objective).stream().mapToInt(ScoreboardPlayerScore::getScore).toArray();
                        if (scores.length >= 2) {
                            for (int i = 1; i < scores.length; i++) {
                                if (scores[i - 1] + 1 != scores[i]) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                },
                DISABLED() {
                    @Override
                    boolean test(ScoreboardObjective objective) {
                        return false;
                    }
                };

                abstract boolean test(ScoreboardObjective objective);

            }

        }

    }

}
