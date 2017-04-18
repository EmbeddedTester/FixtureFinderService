package com.abetterway2feel.fixturefinder.config;

import com.abetterway2feel.fixturefinder.repository.document.LocalFileSystemDocumentRepository;
import com.abetterway2feel.fixturefinder.repository.document.DocumentRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.nio.file.Path;

@Profile("fixtures-local")
@SpringBootConfiguration
public class LocalFixturesConfig {

    @Bean
    public DocumentRepository provideMatchDayDocumentLoader() {
        Path path = new File("src/main/resources/livescore").toPath();
        return new LocalFileSystemDocumentRepository(path, "livescore-results-{date}.html");
    }
}
