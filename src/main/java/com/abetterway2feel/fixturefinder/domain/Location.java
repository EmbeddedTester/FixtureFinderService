package com.abetterway2feel.fixturefinder.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Location {
    WORLD,
    EUROPE,
    ENGLAND,
    GERMANY,
    ITALY,
    SCOTLAND,
    SPAIN;


    public Set<Competition> buildStandardDomesticsFor(String cupName, String... leagueNames) {
        Set<Competition> standardDomestic = new HashSet<>();
        standardDomestic.addAll(buildDomesticLeaguesWithNames(leagueNames));
        standardDomestic.add(buildDomesticCupWithName(cupName));
        return standardDomestic;
    }

    public Set<Competition> buildDomesticLeaguesWithNames(String... leagueNames) {
        final AtomicInteger counter = new AtomicInteger(0);
        return Stream.of(leagueNames)
                .map(
                        name -> Competition
                                .builder()
                                .location(this)
                                .type(CompetitionType.DOMESTIC_LEAGUE)
                                .tier(counter.incrementAndGet())
                                .name(name)
                                .build()
                )
                .collect(Collectors.toSet());

    }

    public Competition buildDomesticCupWithName(String name){
        return buildDomesticCupWith(name, 0);
    }

    public Competition buildDomesticCupWith(String name, int tier){
        return Competition
                .builder()
                .tier(tier)
                .type(CompetitionType.DOMESTIC_CUP)
                .location(this)
                .name(name)
                .build();
    }

}
