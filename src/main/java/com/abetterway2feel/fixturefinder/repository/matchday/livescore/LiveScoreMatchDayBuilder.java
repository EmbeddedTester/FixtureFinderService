package com.abetterway2feel.fixturefinder.repository.matchday.livescore;

import com.abetterway2feel.fixturefinder.domain.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LiveScoreMatchDayBuilder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TIME_SEPARATOR = ":";
    private static final String MINUTE_INDICATOR = "'";
    private static final String BLANK_SEPARATOR = " ";

    private LocalDate matchDate;

    public Competition getCompetition() {
        return competition;
    }

    private Competition competition;
    private Set<Fixture> fixtures = new HashSet<>();

    LiveScoreMatchDayBuilder(LocalDate matchDate, Competition competition) {
        this.matchDate = matchDate;
        this.competition = competition;
    }

    void addFixture(Element element) {
        try {
            Fixture fixture = getFixture(competition, element);
            logger.debug(fixture.toString());
            fixtures.add(fixture);
        }
        catch (Exception e) {
            logger.error("Failed Extracting fixture from "+ element.toString(), e);
        }
    }

    private Fixture getFixture(Competition competition, Element element) {
        String statusOrMinOrKOTime = element.getElementsByClass("min")
                .text();
        Elements teamElement = element.getElementsByClass("ply");
        Team homeTeam = Team.builder()
                .name(teamElement.get(0)
                        .text()
                        .replace("*", "")
                        .trim())
                .build();
        Team awayTeam = Team.builder()
                .name(teamElement.get(1)
                        .text()
                        .replace("*", "")
                        .trim())
                .build();

        return Fixture.builder()
                .fixtureDate(getFixtureDate(statusOrMinOrKOTime))
                .competition(competition)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .score(new Score(element.getElementsByClass("sco")
                        .text()))
                .build();
    }

    private FixtureDate getFixtureDate(String matchStatusIndicator) {
        if (matchStatusIndicator.contains(TIME_SEPARATOR)) {
            String[] time = matchStatusIndicator.split(TIME_SEPARATOR);
            ZonedDateTime utcKickOffTime = matchDate
                    .atStartOfDay(ZoneOffset.UTC)
                    .plusHours(Integer.parseInt(time[0].trim()))
                    .plusMinutes(Integer.parseInt(time[1].split(BLANK_SEPARATOR)[0].trim()));

            return FixtureDate.builder()
                    .status(FixtureStatus.SCHEDULED)
                    .utcKickOffTime(utcKickOffTime.toLocalDateTime())
                    .build();
        }
        else if (matchStatusIndicator.contains(MINUTE_INDICATOR)) {
            Integer minutesPlayed = Arrays.stream(matchStatusIndicator.replace("'", "")
                    .split("\\+"))
                    .map(Integer::parseInt)
                    .reduce(0, (x, y) -> x + y);

            return FixtureDate.builder()
                    .status(FixtureStatus.STARTED)
                    .utcKickOffTime(Instant.now()
                            .atOffset(ZoneOffset.UTC)
                            .minusMinutes(minutesPlayed)
                            .toLocalDateTime())
                    .build();
        }
        else {
            switch (matchStatusIndicator) {
                case LiveScoreConstants.HALF_TIME:
                    return FixtureDate.builder()
                            .status(FixtureStatus.HALF_TIME)
                            .utcKickOffTime(matchDate.atStartOfDay())
                            .build();
                case LiveScoreConstants.FULL_TIME:
                    return FixtureDate.builder()
                            .status(FixtureStatus.FULL_TIME)
                            .utcKickOffTime(matchDate.atStartOfDay())
                            .build();
                case LiveScoreConstants.AFTER_EXTRA_TIME:
                    return FixtureDate.builder()
                            .status(FixtureStatus.AFTER_EXTRA_TIME)
                            .utcKickOffTime(matchDate.atStartOfDay())
                            .build();
                case LiveScoreConstants.ABANDONED:
                case LiveScoreConstants.POSTPONED:
                case LiveScoreConstants.CANCELLED:
                case LiveScoreConstants.INTERRUPTED:
                    return FixtureDate.builder()
                            .status(FixtureStatus.NON_STARTER)
                            .utcKickOffTime(matchDate.atStartOfDay())
                            .build();
                case LiveScoreConstants.AWARDED_HOME_WIN:
                case LiveScoreConstants.AWARDED_AWAY_WIN:
                case LiveScoreConstants.AWARDED_WIN:
                    return FixtureDate.builder()
                            .status(FixtureStatus.AWARDED_WIN)
                            .utcKickOffTime(matchDate.atStartOfDay())
                            .build();
                default:
                    return FixtureDate.builder()
                            .status(FixtureStatus.NOT_FOUND)
                            .utcKickOffTime(matchDate.atStartOfDay())
                            .build();
            }
        }
    }


    MatchDay build() {
        return new MatchDay(matchDate, fixtures);
    }
}
