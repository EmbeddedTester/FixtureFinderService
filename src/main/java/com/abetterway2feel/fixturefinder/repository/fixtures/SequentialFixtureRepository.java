package com.abetterway2feel.fixturefinder.repository.fixtures;

import com.abetterway2feel.fixturefinder.domain.Fixture;
import com.abetterway2feel.fixturefinder.domain.Team;
import com.abetterway2feel.fixturefinder.repository.matchday.MatchDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SequentialFixtureRepository implements FixtureRepository {

    final private MatchDayRepository matchDayRepository;

    @Autowired
    public SequentialFixtureRepository(MatchDayRepository matchDayRepository) {
        this.matchDayRepository = matchDayRepository;
    }

    @Override
    public Collection<Fixture> getFor(Team team, LocalDate start, LocalDate end) {
        List<Fixture> fixtures = new LinkedList<>();
        LocalDate current = start;
        while(current.isBefore(end)){
            List<Fixture> fixturesForTeam = matchDayRepository.fetchFor(current)
                    .getFixtures()
                    .stream()
                    .filter(f -> f.getHomeTeam().equals(team) || f.getAwayTeam().equals(team))
                    .collect(Collectors.toList());
            if(fixturesForTeam != null && !fixturesForTeam.isEmpty()) {
                fixtures.addAll(fixturesForTeam);
            }
            current = current.plus(1, ChronoUnit.DAYS);
        }

        return fixtures
                .stream()
                .sorted(Comparator.comparing(Fixture::getFixtureDate))
                .collect(Collectors.toList());
    }

}