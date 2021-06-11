package com.abetterway2feel.fixturefinder.repository.matchday.livescore;

import com.abetterway2feel.fixturefinder.domain.CacheSettings;
import com.abetterway2feel.fixturefinder.domain.Competition;
import com.abetterway2feel.fixturefinder.domain.Location;
import com.abetterway2feel.fixturefinder.domain.MatchDay;
import com.abetterway2feel.fixturefinder.repository.competition.CompetitionKey;
import com.abetterway2feel.fixturefinder.repository.competition.CompetitionRepository;
import com.abetterway2feel.fixturefinder.repository.document.DocumentRepository;
import com.abetterway2feel.fixturefinder.repository.document.MatchDayNotFoundException;
import com.abetterway2feel.fixturefinder.repository.matchday.MatchDayRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.abetterway2feel.fixturefinder.repository.matchday.livescore.LiveScoreConstants.*;

@Component
public class LiveScoreMatchDayRepository implements MatchDayRepository {

    final private Logger logger = LoggerFactory.getLogger(this.getClass());
    final private LoadingCache<LocalDate, MatchDay> matchDayLoadingCache;
    final private CompetitionRepository competitionRepository;
    final private DocumentRepository documentRepository;

    @Autowired
    public LiveScoreMatchDayRepository(CacheSettings cacheSettings, CompetitionRepository competitionRepository, DocumentRepository documentRepository) {
        this.competitionRepository = competitionRepository;
        this.documentRepository = documentRepository;
        this.matchDayLoadingCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSettings.maximumCacheSize)
                .expireAfterWrite(cacheSettings.expireAfter, cacheSettings.timeUnit)
                .build(matchDayLoader());
    }

    private CacheLoader<LocalDate, MatchDay> matchDayLoader() {
        return new CacheLoader<LocalDate, MatchDay>() {
            public MatchDay load(LocalDate matchDate) {
                Document document;
                try {
                    document = documentRepository.get(matchDate);
                }
                catch (MatchDayNotFoundException ex) {
                    logger.warn(ex.getMessage());
                    return MatchDay.empty(matchDate);
                }

                Elements content = document.getElementsByClass(CONTENT);
                Set<MatchDay> matchDays = new HashSet<>();
                if (!(content.isEmpty())) {
                    LiveScoreMatchDayBuilder liveScoreMatchDayBuilder = null;

                    for (Element element : content.get(0).children()) {
                        if (isATitleRow(element)) {
                            if (liveScoreMatchDayBuilder != null) {
                                MatchDay matchDay = liveScoreMatchDayBuilder.build();
                                if (matchDay.nonEmpty()) {
                                    matchDays.add(matchDay);
                                }
                            }

                            Competition competition = getCompetition(element);
                            logger.debug(competition.toString());
                            liveScoreMatchDayBuilder = new LiveScoreMatchDayBuilder(matchDate, competition);

                        }
                        else if (isAFixtureRow(element) && liveScoreMatchDayBuilder != null && liveScoreMatchDayBuilder.getCompetition()
                                .isSupported()) {
                            liveScoreMatchDayBuilder.addFixture(element);
                        }
                    }
                }

                return matchDays.stream()
                        .reduce(new MatchDay(matchDate), MatchDay::merge);
            }
        };
    }

    @Override
    public MatchDay fetchFor(LocalDate matchDate) {
        try {
            return matchDayLoadingCache.get(matchDate);
        }
        catch (ExecutionException e) {
            //TODO: would it be better to actually return an error?
            logger.error("Something went wrong retrieveing fixtures for" + matchDate.format(DateTimeFormatter.ISO_DATE), e);
            return MatchDay.empty(matchDate);
        }
    }

    private Competition getCompetition(Element element) {
        String fullText = element
                .getElementsByClass(ROW_TALL)
                .get(0)
                .getElementsByClass(LEFT)
                .get(0)
                .text();

        logger.debug(fullText);

        String[] labels = fullText.split(" \\- ");
        String firstLabel = labels[0];
        switch (firstLabel) {
            case "Champions League":
                return competitionRepository
                        .get(new CompetitionKey(Location.EUROPE, "Champions League"))
                        .orElse(Competition.UNSUPPORTED);
            case "Europa League":
                return competitionRepository
                        .get(new CompetitionKey(Location.EUROPE, "Europa League"))
                        .orElse(Competition.UNSUPPORTED);
            case "World Cup":
                //TODO: it would be nice to enrich the Competition with the group and continent. not sure how though
                return competitionRepository
                        .get(new CompetitionKey(Location.WORLD, "World Cup"))
                        .orElse(Competition.UNSUPPORTED);
            case "Euro 2020":
                return competitionRepository
                       .get(new CompetitionKey(Location.EUROPE, "Euro 2020"))
                       .orElse(Competition.UNSUPPORTED);

            default:
                try {
                    Location location = Location.valueOf(firstLabel.toUpperCase());
                    return competitionRepository
                            .get(new CompetitionKey(location, labels[1]))
                            .orElse(Competition.UNSUPPORTED);
                }
                catch (IllegalArgumentException exc) {
                    return Competition.UNSUPPORTED;
                }


        }
    }

    private boolean isAFixtureRow(Element element) {
        return element.hasClass(ROW_GRAY);
    }

    private boolean isATitleRow(Element element) {
        return element.hasClass(ROW_TALL);
    }

}
