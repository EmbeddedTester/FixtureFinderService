package com.abetterway2feel.fixturefinder.service.document;

import java.time.LocalDate;

public interface MatchDayLoader<T> {

    T get(LocalDate matchDate);

}
