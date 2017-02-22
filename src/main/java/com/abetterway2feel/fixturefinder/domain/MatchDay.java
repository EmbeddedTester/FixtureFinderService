package com.abetterway2feel.fixturefinder.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Value
public class MatchDay {
    private final ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC);

    private final LocalDate matchDate;

    private final Set<Fixture> fixtures;


    public MatchDay(LocalDate matchDate){
        this.matchDate = matchDate;
        this.fixtures = new HashSet<>();
    }


    public Integer numberOfFixtures() {
        return fixtures.size();
    }

    public boolean nonEmpty() {
        return fixtures.size() > 0;
    }

    public MatchDay merge(MatchDay matchDay) {
        if(!this.matchDate.equals(matchDay.matchDate)){
            throw new MatchDayMergeException(this.matchDate, matchDay.matchDate);
        }
        Set<Fixture> combined = new HashSet<>();
        combined.addAll(fixtures);
        combined.addAll(matchDay.fixtures);
        return new MatchDay(matchDate, combined);
    }
}
