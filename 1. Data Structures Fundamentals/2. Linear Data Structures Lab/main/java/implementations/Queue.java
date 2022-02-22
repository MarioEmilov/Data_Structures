package implementations;

import interfaces.AbstractQueue;

import java.util.Iterator;

public class Queue<E> implements AbstractQueue<E> {

    private static final String EMPTY_QUEUE = "Queue is empty";

    private Node<E> head;
    private int size;

    private static class Node<E> {

        private E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    public Queue() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public void offer(E element) {
        Node<E> node = new Node<>(element);
        if (isEmpty()) {
            this.head = node;
        } else {
            Node<E> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
        this.size++;
    }

    @Override
    public E poll() {
        ensureNonEmpty();
        E firstElement = this.head.element;
        if (this.size == 1) {
            this.head = null;
        } else {
            Node<E> next = this.head.next;
            this.head.next = null;
            this.head = next;
        }
        this.size--;
        return firstElement;
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return this.head.element;
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

            private Node<E> current = head;

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
            throw new IllegalStateException(EMPTY_QUEUE);
        }
    }
}
