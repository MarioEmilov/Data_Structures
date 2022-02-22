package implementations;

import interfaces.Deque;

import java.util.Iterator;

public class ArrayDeque<E> implements Deque<E> {
    private static final int INITIAL_CAPACITY = 7;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public ArrayDeque() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
        int middle = INITIAL_CAPACITY / 2;
        head = tail = middle;
    }

    @Override
    public void add(E element) {
        addLast(element);
    }

    @Override
    public void offer(E element) {
        addLast(element);
    }

    @Override
    public void addFirst(E element) {

        if (capacityToRightReached()) {
            grow();
        } else if (capacityToLeftReached()) {
            shiftRightAll();
        }

        if (isEmpty()) {
            elements[head] = element;
        } else {
            elements[--head] = element;
        }
        size++;
    }

    @Override
    public void addLast(E element) {

        if (capacityToLeftReached()) {
            grow();
        } else if (capacityToRightReached()) {
            shiftLeftAll();
        }

        if (isEmpty()) {
            elements[tail] = element;
        } else {
            elements[++tail] = element;
        }

        size++;
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public void insert(int index, E element) {

        ensureValidIndex(index);
        index += head;

        if (index == head) {
            addFirst(element);
        } else {

            if (capacityToRightReached()) {
                grow();
            }

            shiftRight(index);
            elements[index] = element;
            size++;
        }
    }

    @Override
    public void set(int index, E element) {
        ensureValidIndex(index);
        index += head;
        elements[index] = element;
    }

    @Override
    public E peek() {

        if (!isEmpty()) {
            return getAt(head);
        }

        return null;
    }

    @Override
    public E poll() {

        if (!isEmpty()) {
            return removeLast();
        }

        return null;
    }

    @Override
    public E pop() {

        if (!isEmpty()) {
            return removeFirst();
        }

        return null;
    }

    @Override
    public E get(int index) {
        ensureValidIndex(index);
        index += head;
        return getAt(index);
    }

    @Override
    public E get(Object object) {

        if (!isEmpty()) {
            for (int i = head; i <= tail; i++) {
                E current = getAt(i);
                if (current.equals(object)) {
                    return current;
                }
            }
        }

        return null;
    }

    @Override
    public E remove(int index) {
        ensureValidIndex(index);
        index += head;

        E element = getAt(index);

        shiftLeft(index + 1);

        size--;
        return element;
    }

    private void shiftLeft(int index) {

        for (int i = index; i <= tail; i++) {
            elements[i - 1] = elements[i];
        }

        elements[tail] = null;
        tail--;
    }

    @Override
    public E remove(Object object) {

        if (!isEmpty()) {
            for (int i = head; i <= tail; i++) {
                E current = getAt(i);
                if (current.equals(object)) {
                    shiftLeft(i + 1);
                    size--;
                    return current;
                }
            }
        }

        return null;
    }

    @Override
    public E removeFirst() {

        if (!isEmpty()) {
            E element = getAt(head);
            elements[head] = null;
            head++;
            size--;
            return element;
        }

        return null;
    }

    @Override
    public E removeLast() {
        if (!isEmpty()) {
            E element = getAt(tail);
            elements[tail] = null;
            tail--;
            size--;
            return element;
        }

        return null;
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
    public void trimToSize() {

        Object[] newElements = new Object[size];

        for (int i = 0, j = head; i < size; i++) {
            newElements[i] = elements[j++];
        }

        elements = newElements;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = head;

            @Override
            public boolean hasNext() {
                return index <= tail;
            }

            @Override
            public E next() {
                return getAt(index++);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) this.elements[index];
    }

    private void shiftRight(int index) {

        for (int i = tail; i >= index; i--) {
            elements[i + 1] = elements[i];
        }

        tail++;
    }

    private void shiftLeftAll() {

        for (int i = head; i <= tail; i++) {
            elements[i - 1] = elements[i];
        }
        head--;
        tail--;
    }

    private void shiftRightAll() {

        for (int i = tail; i >= head; i--) {
            elements[i + 1] = elements[i];
        }
        head++;
        tail++;
    }

    private void ensureValidIndex(int index) {

        if (index < 0 || index + head > tail) {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean capacityToRightReached() {
        return tail == elements.length - 1;
    }

    private void grow() {

        int newCapacity = elements.length * 2 + 1;
        Object[] newElements = new Object[newCapacity];
        int newMiddle = newCapacity / 2;

        head = newMiddle - elements.length / 2;
        tail = newMiddle + elements.length / 2;

        for (int i = head, j = 0; i <= tail; i++, j++) {
            newElements[i] = elements[j];
        }

        elements = newElements;
    }

    private boolean capacityToLeftReached() {
        return head == 0;
    }
}
