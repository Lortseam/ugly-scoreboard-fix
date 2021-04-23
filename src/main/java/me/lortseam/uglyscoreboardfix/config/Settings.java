package me.lortseam.uglyscoreboardfix.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public final class Settings implements ConfigContainer {

    @Transitive
    @ConfigEntries
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Sidebar implements ConfigGroup {

        @Getter
        @ConfigEntry.BoundedFloat(min = 0.1f, max = 10)
        private static float scale = 1;

        @Transitive
        @ConfigEntries
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Position implements ConfigGroup {

            @Getter
            @ConfigEntry(comment = "RIGHT (default) or LEFT")
            private static HorizontalPosition x = HorizontalPosition.RIGHT;
            @Getter
            private static int yOffset = 0;

        }

        @Transitive
        @ConfigEntries
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Text implements ConfigGroup {

            @Getter
            private static TextColor headingColor = TextColor.fromFormatting(Formatting.WHITE);
            @Getter
            private static TextColor color = TextColor.fromFormatting(Formatting.WHITE);
            @Getter
            private static TextColor scoreColor = TextColor.fromFormatting(Formatting.RED);

        }

        @Transitive
        @ConfigEntries
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Background implements ConfigGroup {

            @Getter
            @ConfigEntry.Color(alphaMode = true)
            private static int headingColor = 1711276032;
            @Getter
            @ConfigEntry.Color(alphaMode = true)
            private static int color = 1275068416;

        }

        @Transitive
        @ConfigEntries
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Hiding implements ConfigGroup {

            @ConfigEntry(comment = "ENABLED or DISABLED or AUTO (only enabled when scores are in consecutive order)")
            private static State state = State.AUTO;
            @ConfigEntry(comment = "SCORES or SIDEBAR")
            private static HidePart hidePart = HidePart.SCORES;

            public static boolean shouldHide(HidePart part, ScoreboardObjective objective) {
                return part == hidePart && state.test(objective);
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
