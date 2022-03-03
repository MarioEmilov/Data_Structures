package implementations;

import interfaces.AbstractQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriorityQueue<E extends Comparable<E>> implements AbstractQueue<E> {

    private List<E> elements;

    public PriorityQueue() {
        this.elements = new ArrayList<>();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void add(E element) {

        elements.add(element);

        heapifyUp(elements.size() - 1);
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return getAt(0);
    }

    @Override
    public E poll() {

        ensureNonEmpty();

        E first = elements.get(0);

        Collections.swap(elements, 0, size() - 1);

        elements.remove(size() - 1);

        heapifyDown(0);

        return first;
    }

    private void heapifyDown(int index) {

        while (hasChild(index)) {

            int childIndex = getLeftChildIndex(index);

            int rightChildIndex = getRightChildIndex(index);

            if (rightChildIndex < size() && isGreater(rightChildIndex, childIndex)) {
                childIndex = rightChildIndex;
            }

            if (isGreater(index,childIndex)) {
                break;
            }

            Collections.swap(elements, index, childIndex);

            index = childIndex;
        }
    }

    private boolean hasChild(int index) {
        return getLeftChildIndex(index) < size();
    }

    private void heapifyUp(int index) {

        while (hasParent(index) && isGreater(index, getParentIndex(index))) {

            Collections.swap(elements, index, getParentIndex(index));

            index = getParentIndex(index);
        }
    }

    private int getLeftChildIndex(int index) {
        return 2 * index + 1;
    }

    private int getRightChildIndex(int index) {
        return 2 * index + 2;
    }

    private boolean isGreater(int index, int parentIndex) {
        return getAt(index).compareTo(getAt(parentIndex)) > 0;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) < size();
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private E getAt(int index) {
        return elements.get(index);
    }

    private void ensureNonEmpty() {
        if (size() == 0) {
            throw new IllegalStateException("Queue is empty");
        }
    }
}
