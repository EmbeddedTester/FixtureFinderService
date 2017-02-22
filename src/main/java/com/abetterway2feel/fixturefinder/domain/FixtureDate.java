package com.abetterway2feel.fixturefinder.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Value
public class FixtureDate implements Comparable<FixtureDate> {

    private FixtureStatus status;
    private LocalDateTime utcKickOffTime;

    public String getDate() {
        return utcKickOffTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getTime() {
        return utcKickOffTime.format(DateTimeFormatter.ofPattern("hh:mm"));
    }

    @Override
    public int compareTo(FixtureDate that) {
        return this.utcKickOffTime.compareTo(that.utcKickOffTime);
    }
}
