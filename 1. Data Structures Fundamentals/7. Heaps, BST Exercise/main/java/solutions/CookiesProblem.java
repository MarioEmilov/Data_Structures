package solutions;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class CookiesProblem {

    private Queue<Integer> queue;

    private int required;

    private int min;

    private int operations;

    public Integer solve(int req, int[] cookies) {

        init(req, cookies);

        while (notSolved()) {
            doMagic();
        }

        return getResult();
    }

    private void setQueue(int[] cookies) {
        this.queue = Arrays.stream(cookies).boxed().collect(Collectors.toCollection(PriorityQueue::new));
    }

    private int getResult() {
        return minIsNotLessThanRequired() ? -1 : operations;
    }

    private void doMagic() {
        combineLastTwo();
        min = queue.peek();
        operations++;
    }

    private void init(int req, int[] cookies) {
        setQueue(cookies);
        this.required = req;
        this.min = queue.peek();
        this.operations = 0;
    }

    private void combineLastTwo() {
        queue.add(combine(queue.poll(), queue.poll()));
    }

    private boolean notSolved() {
        return minIsNotLessThanRequired() && atLeastTwoElementsExist();
    }

    private int combine(int first, int second) {
        return first + 2 * second;
    }

    private boolean minIsNotLessThanRequired() {
        return min < required;
    }

    private boolean atLeastTwoElementsExist() {
        return queue.size() > 1;
    }
}