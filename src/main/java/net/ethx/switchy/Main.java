package net.ethx.switchy;

import net.ethx.switchy.model.SwitchyPlatform;
import net.ethx.switchy.win32.WindowsSwitchyPlatform;

public class Main {
    public static void main(String[] args) {
        final String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            init(new WindowsSwitchyPlatform());
        } else {
            System.err.println("Unsupported OS: " + os);
            System.exit(-1);
        }
    }

    static void init(final SwitchyPlatform platform) {
        new Controller(platform).model().visible().set(true);
    }
}