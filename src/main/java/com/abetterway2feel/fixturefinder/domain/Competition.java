package com.abetterway2feel.fixturefinder.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Getter
@Builder
public class Competition implements Comparable<Competition> {
    public static Competition UNSUPPORTED = new Competition(Integer.MAX_VALUE, CompetitionType.UNSUPPORTED, Location.WORLD, "Competition Details Unknown");

    private final int tier;

    @NonNull
    private final CompetitionType type;

    @NonNull
    private final Location location;

    @NonNull
    private final String name;

    @NonNull
    private final Set<String> tags;

    public Competition(int tier, CompetitionType type, Location location, String name, Set<String> tags) {
        this.tier = tier;
        this.type = type;
        this.location = location;
        this.name = name;
        if(tags == null){
            this.tags = Collections.emptySet();
        }
        else{
            this.tags = tags;
        }
    }

    public Competition(int tier, CompetitionType type, Location location, String name) {
        this.tier = tier;
        this.type = type;
        this.location = location;
        this.name = name;
        this.tags = Collections.emptySet();
    }

    @Override
    public int compareTo(Competition o) {
        int compareLocations = location.compareTo(o.location);
        if (compareLocations == 0) {
            return new Integer(tier).compareTo(o.tier);
        }
        else {
            return compareLocations;
        }
    }

    public boolean isSupported() {
        return !this.equals(UNSUPPORTED);
    }

    public Competition withTag(String tag){
        Set<String> currentTags = new HashSet<>(this.tags);
        currentTags.add(tag);

        return Competition
                .builder()
                .name(this.name)
                .location(this.location)
                .tier(this.tier)
                .type(this.type)
                .tags(currentTags)
                .build();
    }
}
