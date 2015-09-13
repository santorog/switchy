package net.ethx.switchy.match;

import javafx.util.Pair;
import net.ethx.switchy.model.ItemMatcher;
import net.ethx.switchy.model.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IdeaItemMatcher<T> implements ItemMatcher<T> {
    @Override
    public List<Match<T>> match(List<T> values, Function<T, String> extracter, String input) {
        final Pattern pattern = patternize(input);
        return values.stream()
                .map(t -> match(t, extracter, pattern))
                .filter(m -> m != null)
                .collect(Collectors.toList());
    }

    static <T> Match<T> match(final T value, final Function<T, String> extracter, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(extracter.apply(value));
        if (matcher.matches()) {
            final List<Pair<Integer, Integer>> ranges = new ArrayList<>();
            for (int i = 1; i <= matcher.groupCount(); i++) {
                ranges.add(new Pair<>(matcher.start(i), matcher.end(i)));
            }

            return new Match<>(value, ranges);
        }

        return null;
    }

    static Pattern patternize(final String input) {
        final List<String> tokens = tokenize(input);

        final StringBuilder builder = new StringBuilder("(?i).*?");
        for (String token : tokens) {
            builder.append("(").append(Pattern.quote(token)).append(")").append(".*?");
        }

        return Pattern.compile(builder.toString());
    }

    static List<String> tokenize(final String input) {
        final List<String> ret = new ArrayList<>();

        final StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c) || !(Character.isAlphabetic(c) || Character.isDigit(c))) {
                ret.add(builder.toString());
                builder.setLength(0);
            }

            builder.append(c);
        }

        ret.add(builder.toString());

        return ret.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
