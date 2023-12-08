package com.clickhouse.kafka.connect.sink.db.helper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer<T> {

    private final List<T> items;
    private final AtomicInteger currentIndex;

    public RoundRobinLoadBalancer(List<T> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items cannot be null or empty");
        }
        this.items = items;

        Collections.shuffle(this.items);
        this.currentIndex = new AtomicInteger(0);
    }

    public T getNextItem() {
        int index = currentIndex.getAndUpdate(i -> (i + 1) % items.size());
        return items.get(index);
    }
}