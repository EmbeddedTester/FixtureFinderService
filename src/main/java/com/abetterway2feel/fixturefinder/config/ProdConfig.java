package com.abetterway2feel.fixturefinder.config;

import com.abetterway2feel.fixturefinder.domain.CacheSettings;
import com.abetterway2feel.fixturefinder.domain.Competition;
import com.abetterway2feel.fixturefinder.domain.CompetitionType;
import com.abetterway2feel.fixturefinder.domain.Location;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Profile("prod")
@SpringBootConfiguration
public class ProdConfig {

    @Bean
    public CacheSettings provideCacheSettings() {
        return new CacheSettings(1000, 20, TimeUnit.MINUTES);
    }

    @Bean
    public Set<Competition> getSupportedLiveScoreCompetitions() {
        Set<Competition> supportedCompetitions = new HashSet<>();
        supportedCompetitions.addAll(Location.ENGLAND.buildStandardDomesticsFor("FA Cup", "Premier League", "Championship"));
        supportedCompetitions.addAll(Location.SCOTLAND.buildStandardDomesticsFor("Scottish Cup","Premiership", "Championship"));
        supportedCompetitions.addAll(Location.GERMANY.buildDomesticLeaguesWithNames("Bundesliga", "2nd Bundesliga"));
        supportedCompetitions.add(Location.GERMANY.buildDomesticCupWithName("DFB Pokal").withTag("DFB Cup"));
        supportedCompetitions.addAll(Location.SPAIN.buildStandardDomesticsFor("Super Cup", "LaLiga Santander", "LaLiga 1|2|3"));
        supportedCompetitions.add(Location.SPAIN.buildDomesticCupWithName("Copa del Rey"));
        supportedCompetitions.add(Competition.builder().tier(1).type(CompetitionType.CONTINENTAL_CLUB_CUP).location(Location.EUROPE).name("Champions League").build());
        supportedCompetitions.add(Competition.builder().tier(2).type(CompetitionType.CONTINENTAL_CLUB_CUP).location(Location.EUROPE).name("Europa League").build());
        supportedCompetitions.add(Competition.builder().tier(1).type(CompetitionType.INTERNATIONAL_COUNTRY_CUP).location(Location.WORLD).name("World Cup").build());

        return supportedCompetitions;
    }

    @Bean
    public Clock provideClock() {
       return Clock.systemUTC();
    }
}
