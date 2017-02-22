package com.abetterway2feel.fixturefinder.rest.dto;

import com.abetterway2feel.fixturefinder.domain.Score;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ScoreDTO {
    public String homeGoals;
    public String awayGoals;

    public static ScoreDTO from(Score score) {
        return ScoreDTO.builder()
                .homeGoals(score.getHomeGoals())
                .awayGoals(score.getAwayGoals())
                .build();
    }
}
