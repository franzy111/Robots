package oop.serialization;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Менеджер для сохранения состояния компонентов
 */
public class StateSaverManager {
    /**
     * Имя компонента, состояние которого нужно сохранить
     */
    private final String name;

    /**
     * Конструктор класса
     *
     * @param name Имя компонента
     */
    public StateSaverManager(String name) {
        this.name = name;
    }

    /**
     * Сохраняет состояние указанного компонента
     *
     * @param states    Словарь состояний
     * @param component Компонент, состояние которого нужно сохранить
     */
    public void save(Map<String, String> states, Component component) {
        states.put(name + "_bounds",
                component.getBounds().x + "," + component.getBounds().y
                        + "," + component.getBounds().width + "," + component.getBounds().height);
        if (component instanceof JInternalFrame jInternalFrame)
            states.put(name + "_isIcon", String.valueOf(jInternalFrame.isIcon()));
    }
}
