package implementations;

import interfaces.LinkedList;

import java.util.Iterator;

public class SinglyLinkedList<E> implements LinkedList<E> {

    private static final String EMPTY_LIST = "Cant perform such operation on empty list";

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    private Node<E> firstNode;

    private int size;

    public SinglyLinkedList() {
        this.firstNode = null;
        this.size = 0;
    }

    @Override
    public void addFirst(E element) {
        Node<E> node = new Node<>(element);
        if (isEmpty()) {
            this.firstNode = node;
        } else {
            node.next = this.firstNode;
            this.firstNode = node;
        }
        this.size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> node = new Node<>(element);
        if (isEmpty()) {
            this.firstNode = node;
        } else {
            Node<E> current = this.firstNode;
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
        this.size++;
    }

    @Override
    public E removeFirst() {
        ensureNonEmpty();
        E element = firstNode.element;
        this.firstNode = this.firstNode.next;
        this.size--;
        return element;
    }

    @Override
    public E removeLast() {
        ensureNonEmpty();
        Node<E> preLastNode = this.firstNode;
        while (preLastNode.next.next != null) {
            preLastNode = preLastNode.next;
        }
        E lastElement = preLastNode.next.element;
        preLastNode.next = null;
        this.size--;
        return lastElement;
    }

    @Override
    public E getFirst() {
        ensureNonEmpty();
        return this.firstNode.element;
    }

    @Override
    public E getLast() {
        ensureNonEmpty();
        Node<E> lastNode = this.firstNode;
        while (lastNode.next != null) {
            lastNode = lastNode.next;
        }
        return lastNode.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private Node<E> current = firstNode;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E element = current.element;
                current = current.next;
                return element;
            }
        };
    }

    private void ensureNonEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException(EMPTY_LIST);
        }
    }
}
