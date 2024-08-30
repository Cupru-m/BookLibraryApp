package com.example.booklibrary.service;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CounterService {
    private static AtomicInteger counter = new AtomicInteger(0);
    public synchronized void incrementCounter() {
        counter.incrementAndGet();
    }
    public long getCount() {
        return counter.get();
    }
}
