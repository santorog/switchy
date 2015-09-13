package net.ethx.switchy.model;

public abstract class App {
    private final String name;

    public App(final String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public abstract void activate();
}