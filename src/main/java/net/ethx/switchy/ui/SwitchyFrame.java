package net.ethx.switchy.ui;

import net.ethx.switchy.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SwitchyFrame extends JFrame {
    public SwitchyFrame(final Controller controller) throws HeadlessException {
        setUndecorated(true);
        setMinimumSize(new Dimension(640, 480));
        setMaximumSize(getMinimumSize());
        setPreferredSize(getMinimumSize());
        setLocationRelativeTo(null);

        final JPanel panel = new SwitchyPanel(controller);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        //  change our visibility per the model
        controller.model().visible().pcs().addPropertyChangeListener(e -> {
            final GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            final GraphicsDevice device = devices[devices.length / 2];
            final Rectangle deviceBounds = device.getDefaultConfiguration().getBounds();
            setLocation((int)(deviceBounds.getCenterX() - (getWidth() / 2)), (int)(deviceBounds.getCenterY() - (getHeight() / 2)));
            setVisible(controller.model().visible().get());
        });

        //  hide on focus lost
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                controller.model().visible().set(false);
            }
        });
    }
}
