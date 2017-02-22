package com.abetterway2feel.fixturefinder.repository.fixture;

import com.abetterway2feel.fixturefinder.domain.CacheSettings;
import com.abetterway2feel.fixturefinder.domain.MatchDay;
import com.abetterway2feel.fixturefinder.parser.matchday.MatchDayFetcher;
import com.abetterway2feel.fixturefinder.repository.MatchDayRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Repository
public class LiveScoreMatchDayRepository implements MatchDayRepository {

    final private LoadingCache<LocalDate, MatchDay> matchDayLoadingCache;

    final private MatchDayFetcher liveScoreMatchDayFetcher;

    @Autowired
    public LiveScoreMatchDayRepository(CacheSettings cacheSettings, MatchDayFetcher liveScoreMatchDayFetcher) {
        this.liveScoreMatchDayFetcher = liveScoreMatchDayFetcher;
        this.matchDayLoadingCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSettings.maximumCacheSize)
                .expireAfterWrite(cacheSettings.expireAfter, cacheSettings.timeUnit)
                .build(loader());
    }

    private CacheLoader<LocalDate, MatchDay> loader() {
        return new CacheLoader<LocalDate, MatchDay>() {
            public MatchDay load(LocalDate matchDate) {
                return liveScoreMatchDayFetcher.fetchFor(matchDate);
            }
        };
    }

    @Override
    public MatchDay getFor(LocalDate matchDate) {
        try {
            return matchDayLoadingCache.get(matchDate);
        }
        catch (ExecutionException e) {
            throw new RuntimeException("Something went wrong....");
        }
    }
}