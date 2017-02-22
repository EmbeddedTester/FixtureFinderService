package com.abetterway2feel.fixturefinder.parser.livescore;

import com.abetterway2feel.fixturefinder.domain.Competition;
import com.abetterway2feel.fixturefinder.domain.CompetitionType;
import com.abetterway2feel.fixturefinder.domain.Location;
import com.abetterway2feel.fixturefinder.domain.MatchDay;
import com.abetterway2feel.fixturefinder.parser.matchday.livescore.LiveScoreDocumentFetcher;
import com.abetterway2feel.fixturefinder.repository.CompetitionKey;
import com.abetterway2feel.fixturefinder.repository.CompetitionRepository;
import com.abetterway2feel.fixturefinder.service.document.FromFileMatchDayDocumentLoader;
import com.abetterway2feel.fixturefinder.service.document.MatchDayLoader;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LiveScoreDocumentFetcherSpec {
    private CompetitionRepository competitionRepository = Mockito.mock(CompetitionRepository.class);
    private MatchDayLoader<Document> matchDayLoader = new FromFileMatchDayDocumentLoader(new File("src/main/resources/livescore").toPath(), "livescore-results-{date}.html");

    private LiveScoreDocumentFetcher sut = new LiveScoreDocumentFetcher(competitionRepository, matchDayLoader);

    @Test
    public void givenThatOnlyEnglishChampionshipIsSupported_thenOneFixtureWillBeReturnedFor20170331() {
        Mockito.when(competitionRepository.get(Matchers.any(CompetitionKey.class)))
                .thenReturn(Optional.of(Competition.UNSUPPORTED));
        Mockito.when(competitionRepository.get(new CompetitionKey(Location.ENGLAND, "Championship")))
                .thenReturn(Optional.of(Competition.builder().tier(2).type(CompetitionType.DOMESTIC_LEAGUE).location(Location.ENGLAND).name("Championship").build()));

        MatchDay matchDay = sut.fetchFor(LocalDate.of(2017, 3, 31));


        assertThat(matchDay.numberOfFixtures(), is(1));
    }

    @Test
    public void givenThatOnlyScottishPremiershipIsSupported_thenOneFixtureWillBeReturnedFor20170331() {
        Mockito.when(competitionRepository.get(Matchers.any(CompetitionKey.class)))
                .thenReturn(Optional.of(Competition.UNSUPPORTED));
        Mockito.when(competitionRepository.get( new CompetitionKey(Location.SCOTLAND, "Premiership")))
                .thenReturn(Optional.of(Competition.builder().tier(1).type(CompetitionType.DOMESTIC_LEAGUE).location(Location.SCOTLAND).name("Premiership").build()));

        MatchDay matchDay = sut.fetchFor(LocalDate.of(2017, 3, 31));

        assertThat(matchDay.numberOfFixtures(), is(1));
    }

    @Test
    public void givenThatItalySpainAndGermanyAreSupported_thenOneFixtureWillBeReturnedFor20170331() {
        Mockito.when(competitionRepository.get(Matchers.any(CompetitionKey.class)))
                .thenReturn(Optional.of(Competition.UNSUPPORTED));
        Mockito.when(competitionRepository.get(  new CompetitionKey(Location.ITALY, "Serie A")))
                .thenReturn(Optional.of(Competition.builder().tier(1).location(Location.ITALY).name("Serie A").type(CompetitionType.DOMESTIC_LEAGUE).build()));
        Mockito.when(competitionRepository.get(  new CompetitionKey(Location.ITALY, "Serie B")))
                .thenReturn(Optional.of(Competition.builder().tier(2).location(Location.ITALY).name("Serie B").type(CompetitionType.DOMESTIC_LEAGUE).build()));
        Mockito.when(competitionRepository.get(  new CompetitionKey(Location.SPAIN, "LaLiga Santander")))
                .thenReturn(Optional.of(Competition.builder().tier(1).location(Location.ITALY).name("Serie B").type(CompetitionType.DOMESTIC_LEAGUE).build()));
        Mockito.when(competitionRepository.get(  new CompetitionKey(Location.SPAIN, "LaLiga 1|2|3")))
                .thenReturn(Optional.of(Competition.builder().tier(2).location(Location.ITALY).name("Serie B").type(CompetitionType.DOMESTIC_LEAGUE).build()));
        Mockito.when(competitionRepository.get(  new CompetitionKey(Location.GERMANY, "Bundesliga")))
                .thenReturn(Optional.of(Competition.builder().tier(1).location(Location.ITALY).name("Serie B").type(CompetitionType.DOMESTIC_LEAGUE).build()));
        Mockito.when(competitionRepository.get(  new CompetitionKey(Location.GERMANY, "2nd Bundesliga")))
                .thenReturn(Optional.of(Competition.builder().tier(2).location(Location.ITALY).name("Serie B").type(CompetitionType.DOMESTIC_LEAGUE).build()));

        MatchDay matchDay = sut.fetchFor(LocalDate.of(2017, 4, 9));

        assertThat(matchDay.numberOfFixtures(), is(23));
    }

    //TODO test a champions league night
}
