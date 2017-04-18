package com.abetterway2feel.fixturefinder.repository.fixtures;

import com.abetterway2feel.fixturefinder.domain.Fixture;
import com.abetterway2feel.fixturefinder.domain.Team;

import java.time.LocalDate;
import java.util.Collection;

public interface FixtureRepository {

    Collection<Fixture> getFor(Team team, LocalDate start, LocalDate end);
}
