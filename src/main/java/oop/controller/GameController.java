package oop.controller;

import oop.model.Robot;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private final Robot m_robot;
    private final Timer m_timer = new Timer("events generator", true);

    public GameController(Robot robot) {
        m_robot = robot;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateEvent();
            }
        }, 0, 10);
    }

    protected void UpdateEvent() {
        m_robot.onModelUpdateEvent();
    }

    public void setTargetPositionRobot(Point point) {
        m_robot.setTargetPosition(point.x, point.y);
    }
}
