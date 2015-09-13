package net.ethx.switchy.model;

import java.util.List;
import java.util.function.Function;

public interface ItemMatcher<T> {
    List<Match<T>> match(final List<T> values, final Function<T, String> extracter, final String input);
}
