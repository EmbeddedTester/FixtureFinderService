package com.abetterway2feel.fixturefinder.parser.matchday;

import com.abetterway2feel.fixturefinder.domain.MatchDay;

import java.time.LocalDate;

public interface MatchDayFetcher {

    MatchDay fetchFor(LocalDate matchDate);

}
