package com.abetterway2feel.fixturefinder.service.document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;

public class HttpMatchDayDocumentLoader implements MatchDayLoader<Document> {

    private String baseURL;

    public HttpMatchDayDocumentLoader(String baseURL) {
        this.baseURL = baseURL;
    }

    @Override
    public Document get(LocalDate date) {
        try {
            return Jsoup.connect(String.format(baseURL, date)).get();
        } catch (IOException e) {
            throw new MatchDayLoaderException("Unable to retrieve document from url " + baseURL, e);
        }
    }

}
