package org.chendev.tasteexpress.infra.cache;

import org.springframework.stereotype.Component;

@Component
public class NoopCacheOps implements CacheOps {
    @Override
    public void evictByPattern(String pattern) {
        /* no-op until Redis is added */ }
}
