package implementations;

import interfaces.AbstractBinarySearchTree;

public class BinarySearchTree<E extends Comparable<E>> implements AbstractBinarySearchTree<E> {

    private Node<E> root;

    private Node<E> left;

    private Node<E> right;

    public BinarySearchTree() {
    }

    public BinarySearchTree(Node<E> root) {
        copy(root);
    }

    private void copy(Node<E> node) {
        if (node != null) {
            insert(node.value);
            copy(node.leftChild);
            copy(node.rightChild);
        }
    }

    @Override
    public void insert(E element) {

        Node<E> newNode = new Node<>(element);

        if (getRoot() == null) {
            root = newNode;
        } else {

            Node<E> current = getRoot();
            Node<E> prev = current;

            while (current != null) {
                prev = current;
                if (isGreater(element, current.value)) {
                    current = current.rightChild;
                } else if (isGreater(current.value, element)) {
                    current = current.leftChild;
                } else {
                    return;
                }
            }

            if (isGreater(prev.value, element)) {
                prev.leftChild = newNode;
            } else if (isGreater(element, prev.value)) {
                prev.rightChild = newNode;
            }

        }
    }

    private boolean isGreater(E first, E second) {
        return first.compareTo(second) > 0;
    }

    @Override
    public boolean contains(E element) {

        Node<E> current = getRoot();

        while (current != null) {
            if (isGreater(element, current.value)) {
                current = current.rightChild;
            } else if (isGreater(current.value, element)) {
                current = current.leftChild;
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public AbstractBinarySearchTree<E> search(E element) {

        AbstractBinarySearchTree<E> searchTree = new BinarySearchTree<>();

        Node<E> current = getRoot();

        while (current != null) {
            if (isGreater(element, current.value)) {
                current = current.rightChild;
            } else if (isGreater(current.value, element)) {
                current = current.leftChild;
            } else {
                return new BinarySearchTree<>(current);
            }
        }

        return searchTree;
    }

    @Override
    public Node<E> getRoot() {
        return root;
    }

    @Override
    public Node<E> getLeft() {
        return left;
    }

    @Override
    public Node<E> getRight() {
        return right;
    }

    @Override
    public E getValue() {
        return root.value;
    }
}
