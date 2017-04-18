package com.abetterway2feel.fixturefinder.repository.matchday;

import com.abetterway2feel.fixturefinder.domain.MatchDay;

import java.time.LocalDate;

public interface MatchDayRepository {

    MatchDay fetchFor(LocalDate matchDate);

}
