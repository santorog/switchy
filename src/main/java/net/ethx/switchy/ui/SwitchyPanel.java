package net.ethx.switchy.ui;

import javafx.util.Pair;
import net.ethx.switchy.Controller;
import net.ethx.switchy.Model;
import net.ethx.switchy.model.App;
import net.ethx.switchy.model.Match;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.BitSet;
import java.util.Objects;

public class SwitchyPanel extends JPanel {
    private final Controller controller;

    private final JTextField input = new JTextField();
    private final JList<Match<App>> matches = new JList<>();
    private final Model model;

    public SwitchyPanel(final Controller controller) {
        this.controller = controller;
        this.model = controller.model();
        initUI();

        //  focus the input whenever we are visible
        model.visible().pcs().addPropertyChangeListener(e -> {
            if (model.visible().get()) {
                input.setText(null);
                SwingUtilities.invokeLater(input::grabFocus);
            }
        });

        //  update the model when text changes
        input.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                model.input().set(input.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });

        //  update our text when the input changes
        model.input().pcs().addPropertyChangeListener(e -> {
            final String modelText = model.input().get();
            if (!Objects.equals(modelText, input.getText())) {
                input.setText(modelText);
            }
        });

        //  update our list of matches when it changes in the model
        model.matches().pcs().addPropertyChangeListener(e -> {
            matches.setModel(new MatchModel());
            matches.setSelectedIndex(0);
        });

        //  listen for enter, and take some action
        input.addKeyListener(new ForwardingKeyAdapter());
    }

    private void initUI() {
        input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 16));
        input.setBackground(Color.BLACK);
        input.setForeground(Color.WHITE);
        input.setFont(new Font("Consolas", Font.PLAIN, 28));

        matches.setBackground(Color.BLACK);
        matches.setForeground(Color.WHITE);
        matches.setSelectionBackground(new Color(0x42, 0x42, 0x42));
        matches.setSelectionForeground(Color.WHITE);

        matches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matches.setCellRenderer(new MatchRenderer());

        setLayout(new BorderLayout());
        add(input, BorderLayout.NORTH);
        add(matches, BorderLayout.CENTER);
    }

    private class ForwardingKeyAdapter extends KeyAdapter {
        private final BitSet forward = new BitSet();

        public ForwardingKeyAdapter() {
            forward.set(KeyEvent.VK_UP);
            forward.set(KeyEvent.VK_DOWN);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            handle(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            handle(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            handle(e);
        }

        void handle(final KeyEvent e) {
            if (forward.get(e.getKeyCode())) {
                matches.dispatchEvent(e);
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                final Match<App> selected = matches.getSelectedValue();
                if (selected != null) {
                    model.selectedMatch().set(selected);
                } else {
                    final Model.Prop<java.util.List<Match<App>>> matches = model.matches();
                    if (matches.get().size() == 1) {
                        model.selectedMatch().set(matches.get().get(0));
                    }
                }
            }
        }
    }

    class MatchModel extends AbstractListModel<Match<App>> {
        @Override
        public int getSize() {
            return controller.model().matches().get().size();
        }

        @Override
        public Match<App> getElementAt(int index) {
            return controller.model().matches().get().get(index);
        }
    }

    class MatchRenderer extends JTextArea implements ListCellRenderer<Match<App>> {
        private final Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(0xFF, 0x98, 0));

        public MatchRenderer() {
            setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            setFont(new Font("Consolas", Font.PLAIN, 22));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Match<App>> list, Match<App> value, int index, boolean isSelected, boolean cellHasFocus) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setText(value.value().name());

            final Highlighter highlighter = getHighlighter();
            highlighter.removeAllHighlights();

            for (Pair<Integer, Integer> range : value.ranges()) {
                try {
                    highlighter.addHighlight(range.getKey(), range.getValue(), painter);
                } catch (BadLocationException ignored) {}
            }

            return this;
        }
    }
}
