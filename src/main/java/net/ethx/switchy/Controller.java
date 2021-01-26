package net.ethx.switchy;

import net.ethx.switchy.match.IdeaItemMatcher;
import net.ethx.switchy.model.App;
import net.ethx.switchy.model.Match;
import net.ethx.switchy.model.SwitchyPlatform;
import net.ethx.switchy.ui.SwitchyFrame;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class Controller {
    private final Model model;

    public Controller(final SwitchyPlatform platform) {
        this.model = new Model(platform.appSource(), new IdeaItemMatcher<>());
        platform.initialise(this);

        //  register for escape key to hide ourselves
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                model.visible().set(false);
            }
            return false;
        });

        //  when we get a selection, activate it and hide ourselves
        model.selectedMatch().pcs().addPropertyChangeListener(e -> {
            final Match<App> match = model.selectedMatch().get();
            if (match != null) {
                match.value().activate();
                model.visible().set(false);
            }
        });

        //  set up the frame
        new SwitchyFrame(this);
    }


    public Model model() {
        return model;
    }
}

