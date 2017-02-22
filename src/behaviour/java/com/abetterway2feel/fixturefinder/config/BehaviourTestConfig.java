package com.abetterway2feel.fixturefinder.config;

import com.abetterway2feel.fixturefinder.domain.CacheSettings;
import com.abetterway2feel.fixturefinder.domain.Competition;
import com.abetterway2feel.fixturefinder.domain.Location;
import com.abetterway2feel.fixturefinder.service.document.FromFileMatchDayDocumentLoader;
import com.abetterway2feel.fixturefinder.service.document.MatchDayLoader;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Profile("behaviour-test")
@SpringBootConfiguration
public class BehaviourTestConfig {

    @Bean
    public CacheSettings provideCacheSettings() {
        return new CacheSettings(50, 5, TimeUnit.MINUTES);
    }

    @Bean
    public Set<Competition> getSupportedLiveScorecompetitions() {
        Set<Competition> supportedCompetitions = new HashSet<>();
        supportedCompetitions.addAll(Location.ENGLAND.buildStandardDomesticsFor("FA Cup", "Premier League", "Championship"));

        return supportedCompetitions;
    }



    @Bean
    public MatchDayLoader provideMatchDayDocumentLoader() {
        Path path = new File("src/main/resources/livescore").toPath();
        return new FromFileMatchDayDocumentLoader(path, "livescore-results-{date}.html");
    }

    @Bean
    public Clock provideClock() {
        return Clock.fixed(LocalDate.of(2017, 4, 9).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }

}
