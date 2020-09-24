package me.lortseam.uglyscoreboardfix;

import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

public enum Type {

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