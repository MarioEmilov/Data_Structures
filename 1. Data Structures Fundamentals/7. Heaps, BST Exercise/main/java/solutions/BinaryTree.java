package solutions;


import model.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BinaryTree {

    private int key;

    private BinaryTree first;

    private BinaryTree second;

    public BinaryTree(int key, BinaryTree first, BinaryTree second) {
        this.key = key;
        this.first = first;
        this.second = second;
    }

    public Integer findLowestCommonAncestor(int first, int second) {
        List<Integer> firstPath = findPath(first);
        List<Integer> secondPath = findPath(second);

        int smallerPath = Math.min(firstPath.size(), secondPath.size());

        int i = 0;
        for (; i < smallerPath; i++) {
            if (!firstPath.get(i).equals(secondPath.get(i))) {
                break;
            }
        }

        return firstPath.get(i - 1);
    }

    private List<Integer> findPath(int element) {

        List<Integer> path = new ArrayList<>();

        findNodePath(this, element, path);

        return path;
    }

    private boolean findNodePath(BinaryTree binaryTree, int element, List<Integer> currentPath) {

        if (binaryTree == null) {
            return false;
        }

        if (binaryTree.key == element) {
            return true;
        }

        currentPath.add(binaryTree.key);

        boolean leftResult = findNodePath(binaryTree.first, element, currentPath);

        if (leftResult) {
            return true;
        }

        boolean rightResult = findNodePath(binaryTree.second, element, currentPath);

        if (rightResult) {
            return true;
        }

        currentPath.remove(Integer.valueOf(binaryTree.key));
        return false;
    }

    public List<Integer> topView() {

        Map<Integer, Pair<Integer, Integer>> offsetToValueLevel = new TreeMap<>();

        traverseTree(this, 0, 1, offsetToValueLevel);

        return offsetToValueLevel
                .values()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toList());

    }

    private void traverseTree(BinaryTree binaryTree,
                              int offset,
                              int level,
                              Map<Integer, Pair<Integer, Integer>> offsetToValueLevel) {

        if (binaryTree == null) { return; }

        Pair<Integer, Integer> currentValueLevel = offsetToValueLevel.get(offset);

        if (currentValueLevel == null || level < currentValueLevel.getValue()) {
            offsetToValueLevel.put(offset, new Pair<>(binaryTree.key, level));
        }

        traverseTree(binaryTree.first, offset - 1, level + 1, offsetToValueLevel);
        traverseTree(binaryTree.second, offset + 1, level + 1, offsetToValueLevel);
    }
}