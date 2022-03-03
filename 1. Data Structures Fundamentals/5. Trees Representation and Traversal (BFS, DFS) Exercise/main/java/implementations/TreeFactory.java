package implementations;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class TreeFactory {
    private Map<Integer, Tree<Integer>> nodesByKeys;

    public TreeFactory() {
        this.nodesByKeys = new LinkedHashMap<>();
    }

    public Tree<Integer> createTreeFromStrings(String[] input) {

        Arrays.stream(input)
                .map(x -> Arrays.stream(x.split("\\s+")).mapToInt(Integer::parseInt).toArray())
                .forEach(x -> addEdge(x[0], x[1]));

        return getRoot();
    }

    public Tree<Integer> createNodeByKey(int key) {
        nodesByKeys.putIfAbsent(key, new Tree<>(key));
        return nodesByKeys.get(key);
    }

    public void addEdge(int parent, int child) {

        Tree<Integer> parentByKey = createNodeByKey(parent);
        Tree<Integer> childByKey = createNodeByKey(child);

        childByKey.setParent(parentByKey);
        parentByKey.addChild(childByKey);
    }

    private Tree<Integer> getRoot() {

        for (Tree<Integer> node : nodesByKeys.values()) {
            if (node.getParent() == null) {
                return node;
            }
        }

        return null;
    }
}



