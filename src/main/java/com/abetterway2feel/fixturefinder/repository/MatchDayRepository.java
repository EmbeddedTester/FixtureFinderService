package com.abetterway2feel.fixturefinder.repository;

import com.abetterway2feel.fixturefinder.domain.MatchDay;

import java.time.LocalDate;

public interface MatchDayRepository {

    MatchDay getFor(LocalDate parse);
}
