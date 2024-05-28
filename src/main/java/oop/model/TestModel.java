package oop.model;


/**
 * Этот класс представляет робота, который может перемещаться по двумерной плоскости.
 * Робот является наблюдаемым объектом, поэтому он может уведомлять наблюдателей об изменениях своего состояния.
 */
public class TestModel extends RobotBehavior {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;
    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    private static final double maxVelocity = 0.5;
    private static final double maxAngularVelocity = 0.1;

    /**
     * Конструктор по умолчанию.
     */
    public TestModel() {
        super();
    }

    /**
     * Устанавливает целевую позицию робота.
     *
     * @param x Целевая позиция по оси X.
     * @param y Целевая позиция по оси Y.
     */
    public void setTargetPosition(Integer x, Integer y) {
        m_targetPositionX = x;
        m_targetPositionY = y;
    }

    /**
     * Вычисляет расстояние между двумя точками.
     *
     * @param x1 Координата X первой точки.
     * @param y1 Координата Y первой точки.
     * @param x2 Координата X второй точки.
     * @param y2 Координата Y второй точки.
     * @return Расстояние между точками.
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Вычисляет угол между двумя точками.
     *
     * @param fromX Координата X первой точки.
     * @param fromY Координата Y первой точки.
     * @param toX   Координата X второй точки.
     * @param toY   Координата Y второй точки.
     * @return Угол между точками.
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * Обрабатывает событие обновления модели.
     */
    public void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY, m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (asNormalizedRadians(angleToTarget - m_robotDirection) < Math.PI) {
            angularVelocity = maxAngularVelocity;
        }
        if (asNormalizedRadians(angleToTarget - m_robotDirection) > Math.PI) {
            angularVelocity = -maxAngularVelocity;
        }
        if (unachievable(m_targetPositionX, m_targetPositionY)) {
            angularVelocity = 0;
        }
        moveRobot(velocity, angularVelocity, 10);
        setChanged();
        notifyObservers("Robot moved");
        clearChanged();
    }

    /**
     * Применяет ограничения к значению.
     *
     * @param value Значение.
     * @param min   Минимальное значение.
     * @param max   Максимальное значение.
     * @return Ограниченное значение.
     */
    private static double applyLimits(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Перемещение робота.
     *
     * @param velocity        Расстояние, которое проходит робот за такт.
     * @param angularVelocity Число радиан, на которые робот может повернуться за такт.
     * @param duration        Длительность перемещения.
     */

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity * (Math.sin(m_robotDirection
                + angularVelocity * duration) - Math.sin(m_robotDirection));


        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity * (Math.cos(m_robotDirection
                + angularVelocity * duration) - Math.cos(m_robotDirection));
        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
    }

    /**
     * Нормализует угол в радианах, приводя его к диапазону от -pi до pi.
     *
     * @param angle угол для нормализации
     * @return нормализованный угол
     */
    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    /**
     * Проверяет, недостижима ли целевая позиция.
     *
     * @param targetPositionX Целевая позиция по оси X.
     * @param targetPositionY Целевая позиция по оси Y.
     * @return `true` если недостижима, иначе `false`.
     */
    public boolean unachievable(double targetPositionX, double targetPositionY) {
        double dx = targetPositionX - m_robotPositionX;
        double dy = targetPositionY - m_robotPositionY;

        double newDX = Math.cos(m_robotDirection) * dx + Math.sin(m_robotDirection) * dy;
        double newDY = Math.cos(m_robotDirection) * dy - Math.sin(m_robotDirection) * dx;

        double maxCurve = maxVelocity / maxAngularVelocity;
        double dist1 = distance(newDX, newDY, 0, maxCurve);
        double dist2 = distance(newDX, newDY + maxCurve, 0, 0);

        return !(dist1 > maxCurve) || !(dist2 > maxCurve);
    }

    public double getM_robotPositionX() {
        return m_robotPositionX;
    }

    public double getM_robotPositionY() {
        return m_robotPositionY;
    }

    public double getM_robotDirection() {
        return m_robotDirection;
    }

    public int getM_targetPositionX() {
        return m_targetPositionX;
    }

    public int getM_targetPositionY() {
        return m_targetPositionY;
    }
}
