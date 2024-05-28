package oop.gui;

import oop.locale.Retranslate;
import oop.model.RobotBehavior;
import oop.serialization.StateRestoreManager;
import oop.serialization.StateSaverManager;
import oop.serialization.Storable;
import oop.locale.LangManager;

import java.awt.*;
import java.util.Map;

import javax.swing.*;


public class GameWindow extends JInternalFrame implements Storable, Retranslate {
    private final String name = "GameWindow";
    private static final LangManager control = LangManager.getInstance();

    public GameWindow(JComponent jComponent) {
        super(control.getLocale("GAME_WINDOW"), true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(jComponent, BorderLayout.CENTER);
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

    /**
     * Перевод на текущий язык.
     */
    @Override
    public void translate() {
        setTitle(control.getLocale("GAME_WINDOW"));
    }
    public void setBehavior(RobotBehavior robot){

    }
}
