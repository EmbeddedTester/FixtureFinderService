package com.abetterway2feel.fixturefinder.domain;

import lombok.val;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class FixtureTest {

    @Test
    public void checkThatTheIDReturnsTheExpectedIdForAnEnglishPremiershipGame() {

        val now = LocalDateTime.now();
        val fixture = Fixture.builder()
                .fixtureDate(FixtureDate.builder().status(FixtureStatus.SCHEDULED).utcKickOffTime(now).build())
                .competition(Competition.builder().location(Location.ENGLAND).type(CompetitionType.DOMESTIC_LEAGUE).name("test").tier(1).build())
                .homeTeam(Team.builder().name("Team One").build())
                .awayTeam(Team.builder().name("Team Two").build())
                .score(new Score("0 - 0"))
                .build();

        assertThat(fixture.id(), is(equalTo("ENG-test-" + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "-Team One-Team Two")));
    }

    @Test
    public void checkThatTheIDReturnsTheExpectedIdForAnScottishPremiershipGame() {
        val now = LocalDateTime.now();
        val fixture = Fixture.builder()
                .fixtureDate(FixtureDate.builder().status(FixtureStatus.SCHEDULED).utcKickOffTime(now).build())
                .competition(Competition.builder().location(Location.SCOTLAND).type(CompetitionType.DOMESTIC_LEAGUE).name("other test").tier(1).build())
                .homeTeam(Team.builder().name("Team One").build())
                .awayTeam(Team.builder().name("Team Two").build())
                .score(new Score("0 - 0"))
                .build();

        assertThat(fixture.id(), is(equalTo("SCO-other test-" + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "-Team One-Team Two")));
    }
}
