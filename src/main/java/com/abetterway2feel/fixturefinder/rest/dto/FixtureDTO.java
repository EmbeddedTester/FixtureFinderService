package com.abetterway2feel.fixturefinder.rest.dto;

import com.abetterway2feel.fixturefinder.domain.Fixture;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FixtureDTO implements Comparable<FixtureDTO> {

    public String country;
    public String competition;
    public KickOffDTO kickOff;
    public String homeTeam;
    public String awayTeam;
    public ScoreDTO score;

    public static FixtureDTO from(Fixture fixture) {
        KickOffDTO kickOffDTO = KickOffDTO.builder()
                .date(fixture.getFixtureDate().getDate())
                .time(fixture.getFixtureDate().getTime())
                .status(fixture.getFixtureDate().getStatus().name())
                .build();

        return FixtureDTO.builder()
                .country(fixture.getCompetition().getLocation().name())
                .competition(fixture.getCompetition().getName())
                .kickOff(kickOffDTO)
                .homeTeam(fixture.getHomeTeam().getName())
                .awayTeam(fixture.getAwayTeam().getName())
                .score(ScoreDTO.from(fixture.getScore()))
                .build();

    }


    @Override
    public int compareTo(FixtureDTO o) {
        return kickOff.compareTo(o.kickOff);
    }
}
