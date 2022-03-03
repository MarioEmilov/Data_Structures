package implementations;

import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxHeap<E extends Comparable<E>> implements Heap<E> {

    private List<E> elements;

    public MaxHeap() {
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

    private void heapifyUp(int index) {

        while (hasParent(index) && isGreater(index, getParentIndex(index))) {

            Collections.swap(elements, index, getParentIndex(index));

            index = getParentIndex(index);
        }
    }

    private boolean isGreater(int index, int parentIndex) {
        return getAt(index).compareTo(getAt(parentIndex)) > 0;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) < size();
    }

    @Override
    public E peek() {
        if (size() == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        return getAt(0);
    }

    private E getAt(int index) {
        return elements.get(index);
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }
}
