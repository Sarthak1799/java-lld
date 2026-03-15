package cache.models;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFUEvictionStrategy<K> implements EvictionStrategy<K> {
    private Map<K, Integer> keyFrequencyMap;
    private Map<Integer, LinkedHashSet<K>> frequencyKeysMap;
    private int minFrequency;

    public LFUEvictionStrategy() {
        this.keyFrequencyMap = new HashMap<>();
        this.frequencyKeysMap = new HashMap<>();
        this.minFrequency = 0;
    }

    @Override
    public void keyAccessed(K key) {
        if (keyFrequencyMap.containsKey(key)) {
            // Update frequency
            int oldFreq = keyFrequencyMap.get(key);
            int newFreq = oldFreq + 1;
            
            // Remove from old frequency bucket
            frequencyKeysMap.get(oldFreq).remove(key);
            if (frequencyKeysMap.get(oldFreq).isEmpty()) {
                frequencyKeysMap.remove(oldFreq);
                if (minFrequency == oldFreq) {
                    minFrequency = newFreq;
                }
            }
            
            // Add to new frequency bucket
            keyFrequencyMap.put(key, newFreq);
            frequencyKeysMap.computeIfAbsent(newFreq, k -> new LinkedHashSet<>()).add(key);
        } else {
            // New key with frequency 1
            keyFrequencyMap.put(key, 1);
            frequencyKeysMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFrequency = 1;
        }
    }

    @Override
    public K evictKey() {
        if (keyFrequencyMap.isEmpty()) return null;
        
        // Get the first key from minimum frequency bucket
        LinkedHashSet<K> keysWithMinFreq = frequencyKeysMap.get(minFrequency);
        K keyToEvict = keysWithMinFreq.iterator().next();
        
        // Remove from both maps
        keysWithMinFreq.remove(keyToEvict);
        if (keysWithMinFreq.isEmpty()) {
            frequencyKeysMap.remove(minFrequency);
        }
        keyFrequencyMap.remove(keyToEvict);
        
        return keyToEvict;
    }
}
