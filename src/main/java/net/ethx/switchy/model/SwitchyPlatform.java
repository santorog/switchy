package net.ethx.switchy.model;

import net.ethx.switchy.Controller;

public interface SwitchyPlatform {
    AppSource appSource();

    void initialise(final Controller controller);
}