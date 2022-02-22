package implementations;

import interfaces.AbstractReversedList;

import java.util.Arrays;
import java.util.Iterator;

public class ReversedList<E> implements AbstractReversedList<E> {

    private static final int INIT_CAPACITY = 2;

    private Object[] elements;
    private int head;
    private int size;

    public ReversedList() {
        this.elements = new Object[INIT_CAPACITY];
        this.head = this.size = 0;
    }

    @Override
    public void add(E element) {

        if (capacityReached()) {
            grow();
        }

        if (isEmpty()) {
            elements[head] = element;
        } else {
            elements[++head] = element;
        }

        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int capacity() {
        return elements.length;
    }

    @Override
    public E get(int index) {

        if (!isEmpty()) {
            int realIndex = head - index;
            ensureIndex(realIndex);


            return getAt(realIndex);
        }

        return null;
    }

    @Override
    public E removeAt(int index) {

        if (!isEmpty()) {
            index = head - index;
            ensureIndex(index);


            E element = getAt(index);
            elements[index] = null;
            moveLeft(index + 1);
            head--;
            size--;
            return element;
        }

        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = head;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public E next() {
                return getAt(index--);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) this.elements[index];
    }

    private void grow() {
        elements = Arrays.copyOf(elements, capacity() * 2);
    }

    private void moveLeft(int startIndex) {

        for (int i = startIndex; i <= head; i++) {
            elements[i - 1] = elements[i];
        }

        elements[head] = null;
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private void ensureIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean capacityReached() {
        return head == capacity() - 1;
    }
}