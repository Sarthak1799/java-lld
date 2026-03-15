package cache.models;

public interface EvictionStrategy<K> {
    void keyAccessed(K key);
    K evictKey();
}
