package me.lortseam.uglyscoreboardfix;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lortseam.completeconfig.api.ConfigCategory;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Config implements ConfigCategory {

    @Getter
    private static final Config instance = new Config();

    private Type type = Type.CONSECUTIVE_ORDER;
    private Hide hide = Hide.SCORES;

    @Override
    public boolean isConfigPOJO() {
        return true;
    }

    public boolean shouldHide(Hide hide, ScoreboardObjective objective) {
        return hide == this.hide && type.test(objective);
    }

    private enum Type {

        ALWAYS() {
            @Override
            boolean test(ScoreboardObjective objective) {
                return true;
            }
        },
        CONSECUTIVE_ORDER() {
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
