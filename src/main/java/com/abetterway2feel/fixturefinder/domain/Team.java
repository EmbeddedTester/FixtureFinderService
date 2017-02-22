package com.abetterway2feel.fixturefinder.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class Team {
    @NonNull
    private String name;
}
