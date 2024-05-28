package oop.gui;

import oop.model.RobotBehavior;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.InvocationTargetException;

/**
 * Класс для загрузки классов из JAR-файлов.
 */
public class JarFileLoader {
    private final URLClassLoader urlClassLoader;

    /**
     * Конструктор инициализирует URLClassLoader для загрузки классов из указанного JAR файла.
     *
     * @param jarFile JAR файл, содержащий классы для загрузки.
     */
    public JarFileLoader(File jarFile) {
        if (!jarFile.exists() || !jarFile.isFile()) {
            JOptionPane.showMessageDialog(null, "Файл не существует или недоступен",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Файл не существует или недоступен");
        }
        if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
            JOptionPane.showMessageDialog(null, "Выбранный файл не является JAR-файлом",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Файл не является JAR-файлом");
        }
        try {
            URL[] urls = new URL[]{jarFile.toURI().toURL()};
            urlClassLoader = new URLClassLoader(urls);
        } catch (Exception e) {
            System.err.println("Указан неправильный адрес JAR файла: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при инициализации URLClassLoader", e);
        }
    }

    /**
     * Загружает класс из указанного JAR-файла.
     *
     * @param className Полное имя класса, который необходимо загрузить.
     * @return Экземпляр класса, реализующего абстрактный класс RobotBehavior.
     * @throws Exception Если происходит ошибка при загрузке класса, создании экземпляра или
     *                   приведении к абстрактному классу RobotBehavior.
     */

    public RobotBehavior loadClassFromJar(String className) {
        try {
            // Загружаем класс по имени
            Class<?> loadedClass = urlClassLoader.loadClass(className);

            // Проверяем, является ли загруженный класс подклассом абстрактного класса RobotBehavior
            if (!RobotBehavior.class.isAssignableFrom(loadedClass)) {
                throw new IllegalArgumentException("Загруженный класс должен быть подклассом" +
                        " абстрактного класса RobotBehavior");
            }

            // Создаем экземпляр загруженного класса
            Object instance = loadedClass.getDeclaredConstructor().newInstance();

            return (RobotBehavior) instance;
        } catch (ClassNotFoundException e) {
            System.err.println("Класс не найден: " + e.getMessage());
            throw new RuntimeException("Класс не найден", e);
        } catch (NoSuchMethodException e) {
            System.err.println("Конструктор не найден: " + e.getMessage());
            throw new RuntimeException("Конструктор не найден", e);
        } catch (InstantiationException e) {
            System.err.println("Ошибка при создании экземпляра: " + e.getMessage());
            throw new RuntimeException("Ошибка при создании экземпляра", e);
        } catch (IllegalAccessException e) {
            System.err.println("Недостаточно прав для создания экземпляра: " + e.getMessage());
            throw new RuntimeException("Недостаточно прав для создания экземпляра", e);
        } catch (InvocationTargetException e) {
            System.err.println("Ошибка при вызове конструктора: " + e.getMessage());
            throw new RuntimeException("Ошибка при вызове конструктора", e);
        } catch (ClassCastException e) {
            System.err.println("Ошибка приведения типов: " + e.getMessage());
            throw new RuntimeException("Ошибка приведения типов", e);
        }
    }

    /**
     * Загружает компонент из JAR-файла.
     *
     * @param className Имя класса компонента.
     * @param robot     Робот, который будет использоваться компонентом.
     * @return Загруженный компонент.
     */
    public JComponent loadCompFromJar(String className, RobotBehavior robot) {
        try {
            // Загружаем класс по имени
            Class<?> loadedClass = urlClassLoader.loadClass(className);


            // Создаем экземпляр загруженного класса
            JComponent instance = (JComponent) loadedClass.getDeclaredConstructor(RobotBehavior.class).newInstance(robot);
            instance.setVisible(true);
            return instance;
        } catch (ClassNotFoundException e) {
            System.err.println("Класс не найден: " + e.getMessage());
            throw new RuntimeException("Класс не найден", e);
        } catch (NoSuchMethodException e) {
            System.err.println("Конструктор не найден: " + e.getMessage());
            throw new RuntimeException("Конструктор не найден", e);
        } catch (InstantiationException e) {
            System.err.println("Ошибка при создании экземпляра: " + e.getMessage());
            throw new RuntimeException("Ошибка при создании экземпляра", e);
        } catch (IllegalAccessException e) {
            System.err.println("Недостаточно прав для создания экземпляра: " + e.getMessage());
            throw new RuntimeException("Недостаточно прав для создания экземпляра", e);
        } catch (InvocationTargetException e) {
            System.err.println("Ошибка при вызове конструктора: " + e.getMessage());
            throw new RuntimeException("Ошибка при вызове конструктора", e);
        } catch (ClassCastException e) {
            System.err.println("Ошибка приведения типов: " + e.getMessage());
            throw new RuntimeException("Ошибка приведения типов", e);
        }
    }

}

