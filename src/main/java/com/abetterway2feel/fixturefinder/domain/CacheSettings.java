package com.abetterway2feel.fixturefinder.domain;

import lombok.Value;

import java.util.concurrent.TimeUnit;

@Value
public class CacheSettings {
    public Integer maximumCacheSize;
    public Integer expireAfter;
    public TimeUnit timeUnit;
}
