package oop.locale;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class LangManager extends Observable {
    public static final String PROPERTY_LANG = "PROPERTY_LANG";
    private Locale currentLang = new Locale("ru");
    private static ResourceBundle resourceBundle;

    private LangManager() {
        resourceBundle = ResourceBundle.getBundle("locale", currentLang);
    }

    public static LangManager getInstance() {
        return ControlLangHolder.INSTANCE;
    }

    public String getLocale(String key) {
        return resourceBundle.getString(key);
    }

    public void setLocale(Locale newLang) {
        if (!currentLang.equals(newLang)) {
            currentLang = newLang;
            resourceBundle = ResourceBundle.getBundle("locale", newLang);
            setChanged();
            notifyObservers(PROPERTY_LANG);
            clearChanged();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    private static class ControlLangHolder {
        private static final LangManager INSTANCE = new LangManager();
    }
}
