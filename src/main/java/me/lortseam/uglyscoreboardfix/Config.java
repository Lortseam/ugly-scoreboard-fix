package me.lortseam.uglyscoreboardfix;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lortseam.completeconfig.api.ConfigCategory;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Config {

    public static final Sidebar SIDEBAR = new Sidebar();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Sidebar implements ConfigCategory {

        public final Hiding HIDING = new Hiding();

        @Getter
        private SidebarPosition position = SidebarPosition.RIGHT;

        @Override
        public boolean isConfigPOJO() {
            return true;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Hiding implements ConfigCategory {

            private State state = State.AUTO;
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
