package com.abetterway2feel.fixturefinder.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Value
public class MatchDay {
    public static MatchDay empty(LocalDate matchDate) {
        return new MatchDay(matchDate);
    }

    private final ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC);

    private final LocalDate matchDate;

    private final Set<Fixture> fixtures;

    public MatchDay(LocalDate matchDate) {
        this.matchDate = matchDate;
        this.fixtures = new HashSet<>();
    }

    public Integer size() {
        return fixtures.size();
    }

    public boolean nonEmpty() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return fixtures.isEmpty();
    }

    /**
     * Combines the give [[MatchDay]] with this match day, creating a new immutable [[MatchDay]]
     */
    public MatchDay merge(MatchDay matchDay) {
        if (!this.matchDate.equals(matchDay.matchDate)) {
            throw new MatchDayMergeException(this.matchDate, matchDay.matchDate);
        }
        Set<Fixture> combined = new HashSet<>();
        combined.addAll(fixtures);
        combined.addAll(matchDay.fixtures);
        return new MatchDay(matchDate, combined);
    }

    public Collection<Fixture> filterBy(Team team) {
        return fixtures
                .stream()
                .filter(f -> f.contains(team))
                .collect(Collectors.toList());
    }

}
