package com.abetterway2feel.fixturefinder.repository;

import com.abetterway2feel.fixturefinder.domain.Competition;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public interface CompetitionRepository {

    Optional<Competition> get(CompetitionKey key);

    Collection<Competition> getAll();
}
