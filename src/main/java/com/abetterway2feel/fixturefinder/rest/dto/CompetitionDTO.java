package com.abetterway2feel.fixturefinder.rest.dto;

import com.abetterway2feel.fixturefinder.domain.Competition;
import lombok.Builder;

@Builder
public class CompetitionDTO {
    public String name;
    public String type;
    public String location;

    public static CompetitionDTO from(Competition competition) {
        return CompetitionDTO.builder()
                .name(competition.getName())
                .type(competition.getType().name())
                .location(competition.getLocation().name())
                .build();
    }
}
