package me.lortseam.uglyscoreboardfix;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lortseam.completeconfig.api.ConfigCategory;
import me.lortseam.completeconfig.api.ConfigEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Config implements ConfigCategory {

    @Getter
    private static final Config instance = new Config();

    private State state = State.AUTO;
    @ConfigEntry("hide")
    private HidePart hidePart = HidePart.SCORES;
    @Getter
    private SidebarPosition position = SidebarPosition.RIGHT;

    @Override
    public boolean isConfigPOJO() {
        return true;
    }

    public boolean shouldHide(HidePart hidePart, ScoreboardObjective objective) {
        return hidePart == this.hidePart && state.test(objective);
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
