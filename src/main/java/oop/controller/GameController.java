package oop.controller;

import oop.model.RobotBehavior;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Контроллер игры.
 */
public class GameController {
    private final RobotBehavior m_robot;
    private final Timer m_timer = new Timer("events generator", true);

    /**
     * Конструктор контроллера игры.
     *
     * @param robot Робот, которым управляет контроллер.
     */
    public GameController(RobotBehavior robot) {
        m_robot = robot;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateEvent();
            }
        }, 0, 10);
    }

    /**
     * Обрабатывает событие обновления модели.
     */
    protected void UpdateEvent() {
        m_robot.onModelUpdateEvent();
    }

    /**
     * Устанавливает целевую позицию робота.
     *
     * @param point Целевая позиция.
     */
    public void setTargetPositionRobot(Point point) {
        m_robot.setTargetPosition(point.x, point.y);
    }
}
