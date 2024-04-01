package oop.serialization;


import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Менеджер для восстановления состояния компонентов
 */
public class StateRestoreManager {
    /**
     * Имя компонента, состояние которого нужно восстановить
     */
    private final String name;

    /**
     * Конструктор класса
     *
     * @param name Имя компонента
     */
    public StateRestoreManager(String name) {
        this.name = name;
    }

    /**
     * Восстанавливает состояние указанного компонента
     *
     * @param states    Словарь состояний
     * @param component Компонент, состояние которого нужно восстановить
     */
    public void restore(Map<String, String> states, Component component) {
        String bounds = states.get(name + "_bounds");
        String icon = states.get(name + "_isIcon");
        if (bounds != null) {
            Rectangle rectangle = getRectangle(bounds);
            component.setBounds(rectangle);
            if (component instanceof JInternalFrame jInternalFrame) {
                try {
                    jInternalFrame.setIcon(Boolean.parseBoolean(icon));
                } catch (PropertyVetoException e) {
                    throw new RuntimeException("Ошибка при установке: " + e);
                }
            }
        }
    }

    /**
     * Преобразует строку в объект Rectangle
     *
     * @param str Строка, представляющая прямоугольник в формате "x,y,width,height"
     * @return Объект Rectangle
     */
    private Rectangle getRectangle(String str) {
        Pattern pattern = Pattern.compile("^(-?\\d+),(-?\\d+),(-?\\d+),(-?\\d+)$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int width = Integer.parseInt(matcher.group(3));
            int height = Integer.parseInt(matcher.group(4));
            return new Rectangle(x, y, width, height);
        } else {
            throw new IllegalArgumentException("Invalid rectangle string: " + str);
        }
    }
}
