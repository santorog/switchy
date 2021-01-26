package net.ethx.switchy.ui;

import net.ethx.switchy.Controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
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
            pack();
            setLocationRelativeTo(null);

            final boolean visible = controller.model().visible().get();
            setVisible(visible);
            if (visible) {
                toFront();
                repaint();
            }
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
