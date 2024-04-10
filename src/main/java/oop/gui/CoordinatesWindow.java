package oop.gui;

import oop.model.Robot;
import oop.serialization.StateRestoreManager;
import oop.serialization.StateSaverManager;
import oop.serialization.Storable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends JInternalFrame implements Storable, Observer {
    private final String name = "CoordinatesWindow";
    private Robot robot;
    private JTextArea jTextArea = new JTextArea();
    public CoordinatesWindow(Robot robot) {
        super("Координаты робота", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(jTextArea, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        this.robot = robot;
        robot.addObserver(this);
    }

    /**
     * Сохраняет состояние объекта
     *
     * @param states Map, в которой будет сохранено состояние объекта
     */
    @Override
    public void save(Map<String, String> states) {
        StateSaverManager stateSaverManager = new StateSaverManager(name);
        stateSaverManager.save(states, this);
    }

    /**
     * Восстанавливает состояние объекта
     *
     * @param states Map из которой будет восстановлено состояние объекта
     */
    @Override
    public void restore(Map<String, String> states) {
        StateRestoreManager stateRestoreManager = new StateRestoreManager(name);
        stateRestoreManager.restore(states, this);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(robot))
            if (arg.equals("Robot moved")){
                jTextArea.setText("x: " + robot.getM_robotPositionX() + " y: " + robot.getM_robotPositionY());
            }
    }
}
