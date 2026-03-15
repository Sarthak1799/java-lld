package cache.models;

public class DoublyLinkedList<K> {
    private Node<K> head;
    private Node<K> tail;

    public DoublyLinkedList() {
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        head.setNext(tail);
        tail.setPrev(head);
    }

    public void addToFront(Node<K> node) {
        node.setNext(head.getNext());
        node.setPrev(head);
        head.getNext().setPrev(node);
        head.setNext(node);
    }

    public void removeNode(Node<K> node) {
        if (node == null) return;
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    public Node<K> removeLast() {
        if (isEmpty()) return null;
        Node<K> lastNode = tail.getPrev();
        removeNode(lastNode);
        return lastNode;
    }

    public boolean isEmpty() {
        return head.getNext() == tail;
    }
}
