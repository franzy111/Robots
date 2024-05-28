package oop.model;

import java.util.Observable;

/**
 * Этот абстрактный класс представляет поведение робота в модели.
 */
public abstract class RobotBehavior extends Observable {
    /**
     * Обрабатывает событие обновления модели.
     */
    public abstract void onModelUpdateEvent();

    /**
     * Устанавливает целевую позицию робота.
     *
     * @param x Целевая позиция по оси X.
     * @param y Целевая позиция по оси Y.
     */
    public abstract void setTargetPosition(Integer x, Integer y);


    /**
     * Возвращает текущую позицию робота по координате X.
     *
     * @return текущая координата X позиции робота.
     */
    public abstract double getM_robotPositionX();

    /**
     * Возвращает текущую позицию робота по координате Y.
     *
     * @return текущая координата Y позиции робота.
     */
    public abstract double getM_robotPositionY();

    /**
     * Возвращает текущее направление робота.
     *
     * @return текущее направление робота.
     */
    public abstract double getM_robotDirection();

    /**
     * Возвращает координату X цели.
     *
     * @return координата X цели.
     */
    public abstract int getM_targetPositionX();

    /**
     * Возвращает координату Y цели.
     *
     * @return координата Y цели.
     */
    public abstract int getM_targetPositionY();
}
