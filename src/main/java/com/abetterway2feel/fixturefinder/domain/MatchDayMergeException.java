package com.abetterway2feel.fixturefinder.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MatchDayMergeException extends RuntimeException {

    public MatchDayMergeException(LocalDate matchDate1, LocalDate matchDate2){
        super(String.format("Cannot merge MatchDays with differnted dates: %s != %s",
                matchDate1.format(DateTimeFormatter.BASIC_ISO_DATE),
                matchDate2.format(DateTimeFormatter.BASIC_ISO_DATE)
        ));
    }
}
