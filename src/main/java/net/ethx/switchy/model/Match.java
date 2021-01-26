package net.ethx.switchy.model;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Match<T> {
    private final T value;
    private final List<Pair<Integer, Integer>> ranges;

    public Match(final T value, final List<Pair<Integer, Integer>> ranges) {
        this.value = value;
        this.ranges = Collections.unmodifiableList(new ArrayList<>(ranges));
    }

    public T value() {
        return value;
    }

    public List<Pair<Integer, Integer>> ranges() {
        return ranges;
    }
}
