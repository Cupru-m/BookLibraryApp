package com.example.booklibrary.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MyCache {
    private static final int MAX_ENTRIES = 100;

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        if (key != null) {
            if (cache.size() >= MAX_ENTRIES) {
                cache.clear();
            }
            cache.put(key, value);
        } else {
            throw new IllegalArgumentException("Key cannot be null");
        }
    }

    public Object get(String key) {
        return cache.get(key);
    }
}