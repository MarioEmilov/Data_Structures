package implementations;

import interfaces.AbstractBinaryTree;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BinaryTree<E> implements AbstractBinaryTree<E> {

    private E key;

    private AbstractBinaryTree<E> left;

    private AbstractBinaryTree<E> right;

    public BinaryTree(E key, AbstractBinaryTree<E> left, AbstractBinaryTree<E> right) {
        this.setKey(key);
        this.left = left;
        this.right = right;
    }

    @Override
    public E getKey() {
        return this.key;
    }

    @Override
    public AbstractBinaryTree<E> getLeft() {
        return this.left;
    }

    @Override
    public AbstractBinaryTree<E> getRight() {
        return this.right;
    }

    @Override
    public void setKey(E key) {
        this.key = key;
    }

    @Override
    public String asIndentedPreOrder(int indent) {

        StringBuilder treeAsString = new StringBuilder();

        treeAsString.append(getSpaces(indent)).append(getKey());

        if (getLeft() != null) {
            treeAsString.append(System.lineSeparator())
                    .append(getLeft().asIndentedPreOrder(indent + 2));
        }

        if (getRight() != null) {
            treeAsString.append(System.lineSeparator())
                    .append(getRight().asIndentedPreOrder(indent + 2));
        }

        return treeAsString.toString();
    }

    private String getSpaces(int indent) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < indent; i++) {
            result.append(" ");
        }

        return result.toString();
    }

    @Override
    public List<AbstractBinaryTree<E>> preOrder() {

        List<AbstractBinaryTree<E>> preOrdered = new ArrayList<>();

        preOrdered.add(this);

        if (getLeft() != null) {
            preOrdered.addAll(getLeft().preOrder());
        }

        if (getRight() != null) {
            preOrdered.addAll(getRight().preOrder());
        }

        return preOrdered;
    }

    @Override
    public List<AbstractBinaryTree<E>> inOrder() {

        List<AbstractBinaryTree<E>> inOrdered = new ArrayList<>();

        if (getLeft() != null) {
            inOrdered.addAll(getLeft().inOrder());
        }

        inOrdered.add(this);

        if (getRight() != null) {
            inOrdered.addAll(getRight().inOrder());
        }

        return inOrdered;
    }

    @Override
    public List<AbstractBinaryTree<E>> postOrder() {
        List<AbstractBinaryTree<E>> postOrdered = new ArrayList<>();

        if (getLeft() != null) {
            postOrdered.addAll(getLeft().postOrder());
        }

        if (getRight() != null) {
            postOrdered.addAll(getRight().postOrder());
        }

        postOrdered.add(this);

        return postOrdered;
    }

    @Override
    public void forEachInOrder(Consumer<E> consumer) {

        if (getLeft() != null) {
            getLeft().forEachInOrder(consumer);
        }

        consumer.accept(getKey());

        if (getRight() != null) {
            getRight().forEachInOrder(consumer);
        }
    }
}
