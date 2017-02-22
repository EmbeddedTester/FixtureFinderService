package com.abetterway2feel.fixturefinder.rest.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class KickOffDTO implements Comparable<KickOffDTO> {
    private final String date;
    private final String time;
    private final String status;

    @Override
    public int compareTo(KickOffDTO o) {
        int compareDates = date.compareTo(date);
        if (compareDates == 0) {
            return time.compareTo(time);
        } else {
            return compareDates;
        }
    }
}
