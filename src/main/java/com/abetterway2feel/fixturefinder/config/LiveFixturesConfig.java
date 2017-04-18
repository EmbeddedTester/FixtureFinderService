package com.abetterway2feel.fixturefinder.config;

import com.abetterway2feel.fixturefinder.repository.matchday.livescore.LiveScoreConstants;
import com.abetterway2feel.fixturefinder.repository.document.WebDocumentRepository;
import com.abetterway2feel.fixturefinder.repository.document.DocumentRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("fixtures-livescore")
@SpringBootConfiguration
public class LiveFixturesConfig {

    @Bean
    public DocumentRepository provideMatchDayDocumentLoader() {
        return new WebDocumentRepository(LiveScoreConstants.LIVE_SCORE_URL);
    }
}
