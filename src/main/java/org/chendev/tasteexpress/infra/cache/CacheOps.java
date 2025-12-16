package org.chendev.tasteexpress.infra.cache;

public interface CacheOps {
    void evictByPattern(String pattern);
}
