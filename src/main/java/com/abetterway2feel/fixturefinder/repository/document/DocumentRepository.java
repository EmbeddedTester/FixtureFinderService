package com.abetterway2feel.fixturefinder.repository.document;

import org.jsoup.nodes.Document;

import java.time.LocalDate;

public interface DocumentRepository {

    Document get(LocalDate matchDate) throws MatchDayNotFoundException;

}
