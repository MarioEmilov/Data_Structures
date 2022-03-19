import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.function.Consumer;

import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> {
    private Node<E> root;

    public static class Node<E> {
        private E value;
        private Node<E> leftChild;
        private Node<E> rightChild;
        private int count;

        public Node(E value) {
            this.value = value;
            this.count = 1;
        }

        public Node(Node<E> other) {

            this.value = other.value;
            this.count = other.count;

            if (other.getLeft() != null) {
                this.leftChild = new Node<>(other.getLeft());
            }

            if (other.getRight() != null) {
                this.rightChild = new Node<>(other.getRight());
            }
        }

        public Node<E> getLeft() {
            return this.leftChild;
        }

        public Node<E> getRight() {
            return this.rightChild;
        }

        public E getValue() {
            return this.value;
        }

        public int getCount() {
            return count;
        }
    }

    public BinarySearchTree(Node<E> root) {
        copy(root);
    }

    public BinarySearchTree() {

    }

    public void eachInOrder(Consumer<E> consumer) {
        nodeInOrder(getRoot(), consumer);
    }

    public Node<E> getRoot() {
        return root;
    }

    public void insert(E element) {

        if (getRoot() == null) {
            this.root = new Node<>(element);
        } else {
            insertInto(getRoot(), element);
        }
    }

    public boolean contains(E element) {

        //return containsNode(getRoot(),element) != null;

        Node<E> current = getRoot();

        while (current != null) {

            if (isGreater(current, element)) {
                current = current.getLeft();
            } else if (isLess(current, element)) {
                current = current.getRight();
            } else {
                return true;
            }
        }

        return false;
    }

    public BinarySearchTree<E> search(E element) {

        Node<E> found = containsNode(getRoot(), element);

        return found != null ? new BinarySearchTree<>(found) : null;
    }

    public List<E> range(E lower, E upper) {

        List<E> result = new ArrayList<>();

        if (getRoot() == null) {
            return result;
        }

        Deque<Node<E>> queue = new ArrayDeque<>();
        queue.offer(getRoot());

        while (!queue.isEmpty()) {

            Node<E> current = queue.poll();

            if (current.getLeft() != null) {
                queue.offer(current.getLeft());
            }

            if (current.getRight() != null) {
                queue.offer(current.getRight());
            }

            if (isInBounds(current, lower, upper)) {
                result.add(current.getValue());
            }

        }

        return result;
    }

    public void deleteMin() {

        ensureNonEmpty();

        if (getRoot().getLeft() == null) {
            this.root = this.root.getRight();
            return;
        }

        Node<E> current = getRoot();

        while (current.getLeft().getLeft() != null) {
            current.count--;
            current = current.getLeft();
        }

        current.count--;

        current.leftChild = current.getLeft().getRight();

    }

    public void deleteMax() {

        ensureNonEmpty();

        if (getRoot().getRight() == null) {
            this.root = getRoot().getLeft();
            return;
        }

        Node<E> current = getRoot();

        while (current.getRight().getRight() != null) {
            current.count--;
            current = current.getRight();
        }

        current.count--;
        current.rightChild = current.getRight().getLeft();

    }

    public int count() {
        return getRoot() == null ? 0 : getRoot().count;
    }

    public int rank(E element) {
        return nodeRank(getRoot(), element);
    }

    public E ceil(E element) {

        Node<E> current = getRoot();
        Node<E> nearestGreater = null;

        while (current != null) {

            if (isLess(current, element)) {
                current = current.getRight();
            } else if (isGreater(current, element)) {
                nearestGreater = current;
                current = current.getLeft();
            } else {
                Node<E> right = current.getRight();
                if (right != null & nearestGreater != null) {
                    nearestGreater = isLess(nearestGreater, right.getValue()) ? right : nearestGreater;
                } else if (nearestGreater == null) {
                    nearestGreater = right;
                }
                break;
            }
        }

        return nearestGreater == null ? null : nearestGreater.getValue();
    }

    public E floor(E element) {

        Node<E> current = getRoot();
        Node<E> nearestSmaller = null;

        while (current != null) {

            if (isLess(current, element)) {
                nearestSmaller = current;
                current = current.getRight();
            } else if (isGreater(current, element)) {
                current = current.getLeft();
            } else {
                Node<E> left = current.getLeft();
                if (left != null & nearestSmaller != null) {
                    nearestSmaller = isLess(nearestSmaller, left.getValue()) ? left : nearestSmaller;
                } else if (nearestSmaller == null) {
                    nearestSmaller = left;
                }
                break;
            }
        }

        return nearestSmaller == null ? null : nearestSmaller.getValue();
    }

    private void insertInto(Node<E> node, E element) {

        if (isGreater(node, element)) {

            if (node.getLeft() == null) {
                node.leftChild = new Node<>(element);
            } else {
                insertInto(node.getLeft(), element);
            }

        } else if (isLess(node, element)) {

            if (node.getRight() == null) {
                node.rightChild = new Node<>(element);
            } else {
                insertInto(node.getRight(), element);
            }
        }

        node.count++;
    }

    private boolean isGreater(Node<E> node, E element) {
        return node.getValue().compareTo(element) > 0;
    }

    private boolean isLess(Node<E> node, E element) {
        return node.getValue().compareTo(element) < 0;
    }

    private int nodeRank(Node<E> node, E element) {

        if (node == null) {
            return 0;
        }

        if (isGreater(node, element)) {
            return nodeRank(node.getLeft(), element);
        } else if (isLess(node, element)) {
            return nodeRank(node.getRight(), element) + getNodeCount(node.getLeft()) + 1;
        }

        return getNodeCount(node.getLeft());
    }

    private int getNodeCount(Node<E> node) {
        return node == null ? 0 : node.getCount();
    }

    private void ensureNonEmpty() {
        if (getRoot() == null) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isInBounds(Node<E> current, E lower, E upper) {
        return (isGreater(current, lower) && isLess(current, upper)) || areEqual(current, lower) || areEqual(current, upper);
    }

    private boolean areEqual(Node<E> node, E element) {
        return node.getValue().compareTo(element) == 0;
    }

    private Node<E> containsNode(Node<E> node, E element) {

        if (node == null) {
            return null;
        }

        if (areEqual(node, element)) {
            return node;
        }

        if (isGreater(node, element)) {
            return containsNode(node.getLeft(), element);
        } else {
            return containsNode(node.getRight(), element);
        }
    }

    private void nodeInOrder(Node<E> node, Consumer<E> consumer) {

        if (node == null) {
            return;
        }

        nodeInOrder(node.getLeft(), consumer);

        consumer.accept(node.getValue());

        nodeInOrder(node.getRight(), consumer);
    }

    private void copy(Node<E> node) {
        if (node != null) {
            insert(node.value);
            copy(node.leftChild);
            copy(node.rightChild);
        }
    }
}