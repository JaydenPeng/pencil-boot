package org.pencil.caffeine.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

/**
 * @author pencil
 * @date 24/10/12 10:36
 */
public class CacheTool {

    private static final Cache<String, String> CAFFINE_CACHE = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(120, TimeUnit.MINUTES)
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .recordStats()
            .build();

    private CacheTool() {
    }

    public static Cache<String, String> getCache() {
        return CAFFINE_CACHE;
    }

    public static void put(String key, String value) {
        CAFFINE_CACHE.put(key, value);
    }

    public static String get(String key) {
        return CAFFINE_CACHE.getIfPresent(key);
    }

    public static String get(String key, UnaryOperator<String> func) {
        return CAFFINE_CACHE.get(key, func);
    }

    public static void remove(String key) {
        CAFFINE_CACHE.invalidate(key);
    }

    public static void clear() {
        CAFFINE_CACHE.invalidateAll();
    }
}
