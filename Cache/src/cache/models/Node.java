package cache.models;

public class Node<K> {
    private K key;
    private Node<K> prev;
    private Node<K> next;

    public Node(K key) {
        this.key = key;
        this.prev = null;
        this.next = null;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public Node<K> getPrev() {
        return prev;
    }

    public void setPrev(Node<K> prev) {
        this.prev = prev;
    }

    public Node<K> getNext() {
        return next;
    }

    public void setNext(Node<K> next) {
        this.next = next;
    }
}
