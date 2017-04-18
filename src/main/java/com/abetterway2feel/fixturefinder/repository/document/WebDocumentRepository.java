package com.abetterway2feel.fixturefinder.repository.document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;

public class WebDocumentRepository implements DocumentRepository {

    private String baseURL;

    public WebDocumentRepository(String baseURL) {
        this.baseURL = baseURL;
    }

    @Override
    public Document get(LocalDate date) throws MatchDayNotFoundException {
        try {
            return Jsoup.connect(String.format(baseURL, date)).get();
        } catch (IOException e) {
            throw new MatchDayNotFoundException("Unable to retrieve document from url " + baseURL, e);
        }
    }

}
