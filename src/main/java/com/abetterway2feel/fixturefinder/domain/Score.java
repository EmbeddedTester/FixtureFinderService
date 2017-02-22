package com.abetterway2feel.fixturefinder.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.abetterway2feel.fixturefinder.domain.FixtureOutcome.*;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Score {

    private final String homeGoals;

    private final String awayGoals;

    public Score(String homeGoals, String awayGoals) {
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public Score(String scoreString) {
        String[] goals = scoreString.split("-");
        homeGoals = goals[0].trim();
        awayGoals = goals[1].trim();
    }

    public FixtureOutcome getFixtureOutcome() {
        try {
            int homeGoalsComparedToAwayGoals = new Integer(Integer.parseInt(homeGoals))
                    .compareTo(
                            Integer.parseInt(awayGoals)
                    );

            if (homeGoalsComparedToAwayGoals > 0) {
                return HOME_WIN;
            } else if (homeGoalsComparedToAwayGoals < 0) {
                return AWAY_WIN;
            } else {
                return DRAW;
            }
        } catch (RuntimeException exception) {
            return NOT_STARTED;
        }
    }

    public String score() {
        return homeGoals + "-" + awayGoals;
    }
}
