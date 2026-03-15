package cache.models;

import java.util.HashMap;
import java.util.Map;

public class LRUEvictionStrategy<K> implements EvictionStrategy<K> {
    private DoublyLinkedList<K> dll;
    private Map<K, Node<K>> keyNodeMap;

    public LRUEvictionStrategy() {
        this.dll = new DoublyLinkedList<>();
        this.keyNodeMap = new HashMap<>();
    }

    @Override
    public void keyAccessed(K key) {
        if (keyNodeMap.containsKey(key)) {
            // Remove from current position
            Node<K> node = keyNodeMap.get(key);
            dll.removeNode(node);
        } else {
            // Create new node
            Node<K> node = new Node<>(key);
            keyNodeMap.put(key, node);
        }
        // Add to front (most recently used)
        Node<K> node = keyNodeMap.get(key);
        dll.addToFront(node);
    }

    @Override
    public K evictKey() {
        // Remove least recently used (from tail)
        Node<K> node = dll.removeLast();
        if (node == null) return null;
        K key = node.getKey();
        keyNodeMap.remove(key);
        return key;
    }
}
