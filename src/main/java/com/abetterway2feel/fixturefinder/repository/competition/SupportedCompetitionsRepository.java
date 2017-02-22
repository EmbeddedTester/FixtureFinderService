package com.abetterway2feel.fixturefinder.repository.competition;

import com.abetterway2feel.fixturefinder.domain.Competition;
import com.abetterway2feel.fixturefinder.domain.Location;
import com.abetterway2feel.fixturefinder.repository.CompetitionKey;
import com.abetterway2feel.fixturefinder.repository.CompetitionRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SupportedCompetitionsRepository implements CompetitionRepository {

    @NonNull
    private final Map<Location, List<Competition>> competitions;

    @Autowired
    public SupportedCompetitionsRepository(Set<Competition> competitions) {
        this.competitions = competitions.stream()
                .collect(Collectors.groupingBy(Competition::getLocation));
    }

    @Override
    public Optional<Competition> get(CompetitionKey key) {

        List<Competition> competitions = this.competitions
                .get(key.getLocation());
        if(competitions == null || competitions.isEmpty()){
            return Optional.empty();
        }
        else {
            return competitions
                    .stream()
                    // exact match on name or listed in tags
                    .filter(c -> c.getName()
                            .equals(key.getTag()) || c.getTags()
                            .contains(key.getTag()))
                    .findFirst();
        }
    }

    @Override
    public Collection<Competition> getAll() {
        return competitions
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
