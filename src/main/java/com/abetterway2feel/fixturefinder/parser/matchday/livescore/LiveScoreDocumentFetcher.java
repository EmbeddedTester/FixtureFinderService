package com.abetterway2feel.fixturefinder.parser.matchday.livescore;

import com.abetterway2feel.fixturefinder.domain.Competition;
import com.abetterway2feel.fixturefinder.domain.Location;
import com.abetterway2feel.fixturefinder.domain.MatchDay;
import com.abetterway2feel.fixturefinder.parser.matchday.MatchDayFetcher;
import com.abetterway2feel.fixturefinder.repository.CompetitionKey;
import com.abetterway2feel.fixturefinder.repository.CompetitionRepository;
import com.abetterway2feel.fixturefinder.service.document.MatchDayLoader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.abetterway2feel.fixturefinder.parser.matchday.livescore.LiveScoreConstants.*;

@Component
public class LiveScoreDocumentFetcher implements MatchDayFetcher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CompetitionRepository competitionRepository;
    private final MatchDayLoader<Document> matchDayLoader;

    @Autowired
    public LiveScoreDocumentFetcher(
            CompetitionRepository competitionRepository,
            MatchDayLoader<Document> matchDayLoader
    ) {
        this.competitionRepository = competitionRepository;
        this.matchDayLoader = matchDayLoader;
    }

    @Override
    public MatchDay fetchFor(LocalDate matchDate) {
        Document document = matchDayLoader.get(matchDate);
        Elements content = document.getElementsByClass(CONTENT);
        Set<MatchDay> matchDays = new HashSet<>();
        if (!(content.isEmpty())) {
            LiveScoreMatchDayBuilder liveScoreMatchDayBuilder = null;

            for (Element element : content.get(0)
                    .children()) {
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
