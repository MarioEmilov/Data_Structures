package implementations;

import interfaces.AbstractTree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Tree<E> implements AbstractTree<E> {

    private E key;
    private Tree<E> parent;
    private List<Tree<E>> children;

    @SafeVarargs
    public Tree(E key, Tree<E>... subtrees) {

        this.key = key;
        this.parent = null;
        this.children = new ArrayList<>();

        for (Tree<E> tree : subtrees) {
            tree.parent = this;
            this.children.add(tree);
        }
    }

    @Override
    public List<E> orderBfs() {

        List<E> result = new ArrayList<>();

        Deque<Tree<E>> queue = new ArrayDeque<>();

        if (this.key == null) {
            return result;
        }

        queue.offer(this);

        while (!queue.isEmpty()) {

            Tree<E> current = queue.poll();
            result.add(current.key);

            for (Tree<E> child : current.children) {
                queue.offer(child);
            }
        }

        return result;
    }

    @Override
    public List<E> orderDfs() {

        List<E> result = new ArrayList<>();

        dfs(this, result);

        return result;
    }

    private void dfs(Tree<E> node, List<E> result) {

        for (Tree<E> child : node.children) {
            dfs(child, result);
        }

        result.add(node.key);
    }

    @Override
    public void addChild(E parentKey, Tree<E> child) {

        Tree<E> parent = search(parentKey);

        if (parent == null) {
            throw new IllegalArgumentException();
        }

        child.parent = parent;
        parent.children.add(child);
    }

    private Tree<E> search(E key) {

        Deque<Tree<E>> queue = new ArrayDeque<>();

        queue.offer(this);

        while (!queue.isEmpty()) {

            Tree<E> current = queue.poll();

            if (current.key.equals(key)) {
                return current;
            }

            for (Tree<E> child : current.children) {
                queue.offer(child);
            }
        }

        return null;
    }

    @Override
    public void removeNode(E nodeKey) {

        Tree<E> nodeToRemove = search(nodeKey);

        if (nodeToRemove == null) {
            throw new IllegalArgumentException();
        }

        if (!isRoot(nodeToRemove)) {
            nodeToRemove.parent.children.remove(nodeToRemove);
        }

        for (Tree<E> child : nodeToRemove.children) {
            child.parent = null;
        }

        nodeToRemove.children.clear();
        nodeToRemove.key = null;
    }

    private boolean isRoot(Tree<E> node) {

        return node.parent == null;
    }

    @Override
    public void swap(E firstKey, E secondKey) {

        Tree<E> first = search(firstKey);
        Tree<E> second = search(secondKey);

        if (first == null || second == null) {
            throw new IllegalArgumentException();
        }

        Tree<E> firstParent = first.parent;
        Tree<E> secondParent = second.parent;

        if (firstParent == null) {
            swapRoot(second);
            return;
        } else if (secondParent == null) {
            swapRoot(first);
            return;
        }

        int firstNodeChildIndex = firstParent.children.indexOf(first);
        int secondNodeChildIndex = secondParent.children.indexOf(second);

        first.parent = secondParent;
        second.parent = firstParent;

        firstParent.children.set(firstNodeChildIndex, second);
        secondParent.children.set(secondNodeChildIndex, first);
    }

    private void swapRoot(Tree<E> newRoot) {
        this.key = newRoot.key;
        this.parent = null;
        this.children = newRoot.children;
    }
}