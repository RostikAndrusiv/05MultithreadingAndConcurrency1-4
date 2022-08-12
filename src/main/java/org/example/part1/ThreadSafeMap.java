package org.example.part1;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

public class ThreadSafeMap<K, V> {

    private final Map<K, V> map = new HashMap<>();

    private final Lock lock = new ReentrantLock();

    public V get(Object key) {
        try {
            lock.lock();
            return map.get(key);
        } finally {
            lock.unlock();
        }
    }

    public V put(K key, V value) {
        try {
            lock.lock();
            return map.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public V remove(Object key) {
        try {
            lock.lock();
            return map.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public Set<K> keySet() {
        try {
            lock.lock();
            return map.keySet();
        } finally {
            lock.unlock();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        try {
            lock.lock();
            return map.entrySet();
        } finally {
            lock.unlock();
        }
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        Map<K, V> copy;
        try {
            lock.lock();
            copy = Map.copyOf(map);
        } finally {
            lock.unlock();
        }
        return copy.entrySet().iterator();
    }
}
