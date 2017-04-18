package com.abetterway2feel.fixturefinder.repository.document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This will use the files stored in a given directory as Documents.
 * The base path for the files will be under `pathToFolder` and the
 * file name will be a string which expects a date to be formated in.
 * <p>
 * example:
 * pathToFolder = /my/local/fixtures
 * fileNameTemplate = someProvider-{date}
 * <p>
 * The date which will replace {date} will be in the format is YYYY-MM-dd
 * <p>
 * Therefore the file '/my/local/fixtures/someProvider-2017-04-29' would be loaded for that [[LocalDate]].
 */
public class LocalFileSystemDocumentRepository implements DocumentRepository {

    private final Path pathToFolder;
    private final String fileNameTemplate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalFileSystemDocumentRepository(Path pathToFolder, String fileNameTemplate) {
        this.pathToFolder = pathToFolder;
        this.fileNameTemplate = fileNameTemplate;
    }

    @Override
    public Document get(LocalDate matchDate) throws MatchDayNotFoundException {
        File file = getFile(matchDate.format(dateFormatter));
        try {
            return Jsoup.parse(file, StandardCharsets.UTF_8.name());
        }
        catch (IOException e) {
            throw new MatchDayNotFoundException("Unable to retrieve document from " + file.getAbsolutePath(), e);
        }

    }

    private File getFile(String date) {
        return new File(pathToFolder + "/" + fileNameTemplate.replace("{date}", date));
    }

}
