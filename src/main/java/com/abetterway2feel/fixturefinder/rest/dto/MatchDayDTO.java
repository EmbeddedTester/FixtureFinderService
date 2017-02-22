package com.abetterway2feel.fixturefinder.rest.dto;

import com.abetterway2feel.fixturefinder.domain.Fixture;
import com.abetterway2feel.fixturefinder.domain.MatchDay;
import lombok.Builder;
import lombok.Value;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Value
public class MatchDayDTO {
    private final List<FixtureDTO> fixtures;
    private final String matchDate;
    private final String lastUpdate;

    public static MatchDayDTO createResponseFor(MatchDay matchDay) {
        List<FixtureDTO> fixturesDTOs = matchDay.getFixtures().stream()
                .sorted(Comparator.comparing(Fixture::getCompetition))
                .map(FixtureDTO::from)
                .collect(Collectors.toCollection(LinkedList::new));

        fixturesDTOs.sort(FixtureDTO::compareTo);

        return MatchDayDTO.builder()
                .fixtures(fixturesDTOs)
                .matchDate(matchDay.getMatchDate().format(DateTimeFormatter.ISO_DATE))
                .lastUpdate(matchDay.getCreatedAt().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
    }

}
