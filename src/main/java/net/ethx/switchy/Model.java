package net.ethx.switchy;

import net.ethx.switchy.model.App;
import net.ethx.switchy.model.AppSource;
import net.ethx.switchy.model.ItemMatcher;
import net.ethx.switchy.model.Match;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.List;

public class Model {
    private final AppSource source;
    private final ItemMatcher<App> matcher;

    private final Prop<Boolean> visible = new Prop<>(false);
    private final Prop<String> input = new Prop<>("");
    private final Prop<List<App>> apps = new Prop<>(Collections.emptyList());
    private final Prop<List<Match<App>>> matches = new Prop<>(Collections.emptyList());
    private final Prop<Match<App>> accepted = new Prop<>(null);

    public Model(final AppSource source, final ItemMatcher<App> matcher) {
        this.source = source;
        this.matcher = matcher;

        //  when the visibility changes to true, update the apps
        visible.pcs().addPropertyChangeListener(e -> {
            if (visible.get()) {
                apps.set(source.get());
            }
        });

        //  when the apps or input update, update the matches
        final PropertyChangeListener updateMatches = e -> matches.set(matcher.match(apps.get(), App::name, input.get()));
        apps.pcs().addPropertyChangeListener(updateMatches);
        input.pcs().addPropertyChangeListener(updateMatches);

        //  whenever our matches change, clear the accepted value
        matches.pcs().addPropertyChangeListener(e -> accepted.clear());
    }

    public Prop<Boolean> visible() {
        return visible;
    }

    public Prop<String> input() {
        return input;
    }

    public Prop<List<App>> apps() {
        return apps;
    }

    public Prop<List<Match<App>>> matches() {
        return matches;
    }

    public Prop<Match<App>> selectedMatch() {
        return accepted;
    }

    public static class Prop<T> {
        private final PropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);

        private volatile T value;

        public Prop(final T value) {
            this.value = value;
        }

        public PropertyChangeSupport pcs() {
            return pcs;
        }

        public void clear() {
            set(null);
        }

        public T get() {
            return value;
        }

        public void set(final T newValue) {
            pcs.firePropertyChange("this", get(), this.value = newValue);
        }
    }
}
