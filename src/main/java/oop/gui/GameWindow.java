package oop.gui;

import oop.serialization.StateRestoreManager;
import oop.serialization.StateSaverManager;
import oop.serialization.Storable;

import java.awt.*;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;


public class GameWindow extends JInternalFrame implements Storable {
    private final String name = "GameWindow";

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void save(Map<String, String> states) {
        StateSaverManager stateSaverManager = new StateSaverManager(name);
        stateSaverManager.save(states, this);
    }

    @Override
    public void restore(Map<String, String> states) {
        StateRestoreManager stateRestoreManager = new StateRestoreManager(name);
        stateRestoreManager.restore(states, this);
    }
}
