package implementations;

import interfaces.AbstractStack;

import java.util.Iterator;

public class Stack<E> implements AbstractStack<E> {

    private static final String EMPTY_STACK = "Stack is empty";

    private static class Node<E> {

        private E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    private Node<E> top;
    private int size;

    public Stack() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public void push(E element) {

        Node<E> node = new Node<>(element);

        if (!isEmpty()) {
            node.next = this.top;
        }

        this.top = node;
        this.size++;
    }

    @Override
    public E pop() {

        if (isEmpty()) {
            throw new IllegalStateException(EMPTY_STACK);
        }

        E topElement = this.top.element;

        if (this.size == 1) {
            this.top = null;
        } else {
            this.top = this.top.next;
        }

        this.size--;
        return topElement;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException(EMPTY_STACK);
        }

        return this.top.element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private Node<E> current = top;

            @Override
            public boolean hasNext() {
                return this.current != null;
            }

            @Override
            public E next() {
                E element = this.current.element;
                this.current = this.current.next;
                return element;
            }
        };
    }
}
