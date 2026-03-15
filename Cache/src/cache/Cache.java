package cache;

import java.util.HashMap;
import java.util.Map;

import cache.models.EvictionStrategy;

public class Cache<K, V> {

    private final int capacity;
    private final Map<K, V> storage;
    private final EvictionStrategy<K> evictionStrategy;
    private final Object lock = new Object();

    public Cache(int capacity, EvictionStrategy<K> evictionStrategy) {
        this.capacity = capacity;
        this.storage = new HashMap<>();
        this.evictionStrategy = evictionStrategy;
    }

    public V get(K key) {
        synchronized (lock) {
            if (!storage.containsKey(key)) {
                return null;
            }
            evictionStrategy.keyAccessed(key);
            return storage.get(key);
        }
    }

    public void put(K key, V value) {
        synchronized (lock) {
            if (storage.containsKey(key)) {
                // Update existing key
                storage.put(key, value);
                evictionStrategy.keyAccessed(key);
            } else {
                // Check if cache is full
                if (storage.size() >= capacity) {
                    K keyToEvict = evictionStrategy.evictKey();
                    if (keyToEvict != null) {
                        storage.remove(keyToEvict);
                    }
                }
                // Add new key
                storage.put(key, value);
                evictionStrategy.keyAccessed(key);
            }
        }
    }

    public int size() {
        synchronized (lock) {
            return storage.size();
        }
    }

    public void clear() {
        synchronized (lock) {
            storage.clear();
        }
    }

    public boolean containsKey(K key) {
        synchronized (lock) {
            return storage.containsKey(key);
        }
    }
}
