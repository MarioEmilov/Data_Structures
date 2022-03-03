package implementations;

import interfaces.AbstractTree;

import java.util.*;
import java.util.stream.Collectors;

public class Tree<E> implements AbstractTree<E> {

    private E key;
    private Tree<E> parent;
    private List<Tree<E>> children;

    @SafeVarargs
    public Tree(E key, Tree<E>... children) {
        this.key = key;
        this.children = new ArrayList<>();
        Arrays.stream(children).forEach(this::addChild);
    }

    @Override
    public void setParent(Tree<E> parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(Tree<E> child) {
        child.setParent(this);
        this.children.add(child);
    }

    @Override
    public Tree<E> getParent() {
        return this.parent;
    }

    @Override
    public E getKey() {
        return this.key;
    }

    @Override
    public String getAsString() {
        StringBuilder builder = new StringBuilder();
        traverseTreeDFS(this, builder, 0);
        return builder.toString().trim();
    }

    public String traverseBFS() {

        StringBuilder builder = new StringBuilder();

        Deque<Tree<E>> queue = new ArrayDeque<>();

        queue.offer(this);

        int space = 0;

        while (!queue.isEmpty()) {
            Tree<E> tree = queue.poll();

            if (isNotRoot(tree) && isFirstOfThisLevel(tree)) {
                space += 2;
            }

            builder.append(getPadding(space))
                    .append(tree.getKey())
                    .append(System.lineSeparator());

            for (int i = 0; i < tree.children.size(); i++) {

                queue.offer(tree.children.get(i));
            }
        }

        return builder.toString().trim();
    }

    @Override
    public List<E> getLeafKeys() {
        return getAllTreesBFS().stream()
                .filter(x -> x.children.size() == 0)
                .map(Tree::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<E> getMiddleKeys() {
        return getAllTreesBFS().stream()
                .filter(x -> x.getParent() != null && x.children.size() != 0)
                .map(Tree::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Tree<E> getDeepestLeftmostNode() {

        List<Tree<E>> deepestLeftMostTree = new ArrayList<>();
        int[] maxPath = new int[1];
        deepestLeftMostTree.add(0, null);
        findDeepestTreeDFS(deepestLeftMostTree, 0, maxPath, this);
        return deepestLeftMostTree.get(0);
    }

    private void findDeepestTreeDFS(List<Tree<E>> deepest, int currentPath, int[] maxPath, Tree<E> tree) {

        if (currentPath > maxPath[0]) {
            maxPath[0] = currentPath;
            deepest.set(0, tree);
        }

        for (Tree<E> child : tree.children) {
            findDeepestTreeDFS(deepest, currentPath + 1, maxPath, child);
        }
    }

    private Deque<E> getPathToRoot(Tree<E> tree) {

        Deque<E> reversedPath = new ArrayDeque<>();

        Tree<E> current = tree;

        while (current != null) {
            reversedPath.push(current.getKey());
            current = current.parent;
        }

        return reversedPath;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<List<E>> pathsWithGivenSum(int sum) {

        List<Tree<E>> allTrees = getAllTreesBFS();

        List<List<E>> pathsWithGivenSum = new ArrayList<>();

        for (Tree<E> tree : allTrees) {

            List<E> path = new ArrayList<>(getPathToRoot(tree));

            int pathSum = tree.getPathSum((List<Integer>) path);

            if (pathSum == sum) {
                pathsWithGivenSum.add(path);
            }
        }

        return pathsWithGivenSum;
    }

    @Override
    public List<E> getLongestPath() {
        return new ArrayList<>(getPathToRoot(getDeepestLeftmostNode()));
    }

    private int getPathSum(List<Integer> path) {
        return path.stream().reduce(0, Integer::sum);
    }

    @Override
    public List<Tree<E>> subTreesWithGivenSum(int sum) {
        List<Tree<E>> subtreeRoots = new ArrayList<>();
        getSubtreeRootWithSum(this, sum, subtreeRoots);
        return subtreeRoots;
    }

    private int getSubtreeRootWithSum(Tree<E> current, int targetSum, List<Tree<E>> result) {

        int currentSum = (int) current.key;

        for (Tree<E> child : current.children) {
            currentSum += getSubtreeRootWithSum(child, targetSum, result);
        }

        if (currentSum == targetSum) {
            result.add(current);
        }
        return currentSum;
    }

    private String getPadding(int spaces) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < spaces; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    private void traverseTreeDFS(Tree<E> tree, StringBuilder builder, int level) {

        builder.append(getPadding(level))
                .append(tree.getKey())
                .append(System.lineSeparator());

        level += 2;

        for (Tree<E> child : tree.children) {
            traverseTreeDFS(child, builder, level);
        }
    }

    private boolean isFirstOfThisLevel(Tree<E> tree) {
        return isFirstChild(tree) && isFirstChild(tree.getParent());
    }

    private boolean isFirstChild(Tree<E> tree) {

        Tree<E> parent = tree.getParent();

        return parent == null || tree.getParent().children.indexOf(tree) == 0;
    }

    private boolean isNotRoot(Tree<E> tree) {
        return tree.getParent() != null;
    }

    private List<Tree<E>> getAllTreesBFS() {

        Deque<Tree<E>> queue = new ArrayDeque<>();

        queue.offer(this);

        List<Tree<E>> allTrees = new ArrayList<>();

        while (!queue.isEmpty()) {
            Tree<E> tree = queue.poll();

            allTrees.add(tree);

            for (int i = 0; i < tree.children.size(); i++) {

                queue.offer(tree.children.get(i));
            }
        }

        return allTrees;
    }
}


