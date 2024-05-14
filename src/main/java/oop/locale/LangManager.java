package oop.locale;

import java.awt.*;
import java.util.*;

/**
 * Класс для управления локализацией.
 */
public class LangManager{
    private Locale currentLang = new Locale("ru");
    private static ResourceBundle resourceBundle;
    private LangManager() {
        resourceBundle = ResourceBundle.getBundle("locale", currentLang);
    }
    /**
     * Получение экземпляра класса.
     *
     * @return Экземпляр класса LangManager.
     */
    public static LangManager getInstance() {
        return ControlLangHolder.INSTANCE;
    }
    /**
     * Получение локализованной строки по ключу.
     *
     * @param key Ключ локализованной строки.
     * @return Локализованная строка.
     */
    public String getLocale(String key) {
        return resourceBundle.getString(key);
    }
    /**
     * Установка языка приложения.
     *
     * @param newLang Новый язык приложения.
     */
    public boolean wasChange(Locale newLang) {
        return !currentLang.equals(newLang);
    }

    public void setLocale(Locale newLang, Component components) {
        currentLang = newLang;
        resourceBundle = ResourceBundle.getBundle("locale", newLang);
        if (components instanceof Retranslate retranslate) {
            retranslate.translate();
        }
        if (components instanceof Container container) {
            for(Component component : container.getComponents()) {
                setLocale(newLang, component);
            }
        }
    }



    private static class ControlLangHolder {
        private static final LangManager INSTANCE = new LangManager();
    }
}
