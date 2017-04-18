package com.abetterway2feel.fixturefinder.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.format.DateTimeFormatter;

@Builder
@Value
public class Fixture implements Comparable<Fixture> {

    @NonNull
    private final FixtureDate fixtureDate;

    @NonNull
    private final Competition competition;

    @NonNull
    private final Team homeTeam;

    @NonNull
    private final Team awayTeam;

    @NonNull
    private final Score score;

    /**
     * builds a unique id based on the competitions, date and teams"
     **/
    public final String id() {
        return competition.getLocation().name().substring(0, 3)
                + "-"
                + competition.getName()
                + "-"
                + fixtureDate.getUtcKickOffTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                + "-"
                + homeTeam.getName()
                + "-"
                + awayTeam.getName();
    }

    @Override
    public int compareTo(Fixture that) {
        return this.fixtureDate.compareTo(that.fixtureDate);
    }

    public boolean contains(Team team) {
        return this.getHomeTeam().equals(team) || this.getAwayTeam().equals(team);
    }
}
