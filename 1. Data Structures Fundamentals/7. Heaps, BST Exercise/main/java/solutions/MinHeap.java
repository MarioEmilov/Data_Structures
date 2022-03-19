package solutions;

import interfaces.Decrease;
import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinHeap<E extends Comparable<E> & Decrease<E>> implements Heap<E> {

    private List<E> elements;

    public MinHeap() {
        this.elements = new ArrayList<>();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void add(E element) {
        elements.add(element);
        heapifyUp();
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return elements.get(0);
    }

    @Override
    public E poll() {
        ensureNonEmpty();

        Collections.swap(elements, 0, elements.size() - 1);

        E removed = elements.remove(elements.size() - 1);

        heapifyDown();

        return removed;
    }

    @Override
    public void decrease(E element) {

        int elementIndex = elements.indexOf(element);
        E elementToDecrease = elements.remove(elementIndex);
        elementToDecrease.decrease();
        add(elementToDecrease);
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) < size();
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private void ensureNonEmpty() {
        if (elements.size() == 0) {
            throw new IllegalStateException();
        }
    }

    private int getRightChildIndex(int index) {
        return getLeftChildIndex(index) + 1;
    }

    private int getLeftChildIndex(int index) {
        return 2 * index + 1;
    }

    private void heapifyDown() {

        int index = 0;
        int swapIndex = getLeftChildIndex(index);

        while (swapIndex < size()) {

            int rightChildIndex = getRightChildIndex(index);

            if (rightChildIndex < size()) {
                swapIndex = isLess(swapIndex, rightChildIndex) ? swapIndex : rightChildIndex;
            }

            if (isLess(index, swapIndex)) {
                break;
            }

            Collections.swap(elements, index, swapIndex);

            index = swapIndex;
            swapIndex = getLeftChildIndex(index);
        }
    }

    private void heapifyUp() {

        int lastIndex = size() - 1;

        while (hasParent(lastIndex) && isLess(lastIndex, getParentIndex(lastIndex))) {

            Collections.swap(elements, lastIndex, getParentIndex(lastIndex));

            lastIndex = getParentIndex(lastIndex);
        }
    }

    private boolean isLess(int firstIndex, int secondIndex) {
        return elements.get(firstIndex).compareTo(elements.get(secondIndex)) < 0;
    }
}