package com.abetterway2feel.fixturefinder.repository.fixtures;

import com.abetterway2feel.fixturefinder.domain.Fixture;
import com.abetterway2feel.fixturefinder.domain.Team;
import com.abetterway2feel.fixturefinder.repository.matchday.MatchDayRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Repository
public class ParallelFixtureRepository implements FixtureRepository {

    @AllArgsConstructor
    private class ByTeamLoader implements Callable<Collection<Fixture>> {

        final private LocalDate matchDate;
        final private Team team;

        @Override
        public Collection<Fixture> call() throws Exception {
            return matchDayRepository
                    .fetchFor(matchDate)
                    .filterBy(team);
        }
    }


    final private MatchDayRepository matchDayRepository;

    @Autowired
    public ParallelFixtureRepository(MatchDayRepository matchDayRepository) {
        this.matchDayRepository = matchDayRepository;
    }

    @Override
    public Collection<Fixture> getFor(Team team, LocalDate start, LocalDate end) {
        final int threads = 10;
        final int maxCapacity = 10;
        LocalDate current = start;

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(maxCapacity));
        Set<Future<Collection<Fixture>>> futures = new HashSet<>();

        do {
            try {
                Future<Collection<Fixture>> future = threadPoolExecutor.submit(new ByTeamLoader(current, team));
                futures.add(future);
            }
            catch (RejectedExecutionException e) {
                CompletableFuture<Collection<Fixture>> completedFuture = CompletableFuture.completedFuture(matchDayRepository.fetchFor(current).filterBy(team));
                futures.add(completedFuture);
            }
            current = current.plus(1, ChronoUnit.DAYS);
        } while (!current.isAfter(end));

        List<Fixture> fixtures = Collections.synchronizedList(new LinkedList<>());

        futures.forEach(collectionFuture -> {
            try {
                fixtures.addAll(collectionFuture.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        return fixtures
                .stream()
                .sorted(Comparator.comparing(Fixture::getFixtureDate))
                .collect(Collectors.toList());
    }

}