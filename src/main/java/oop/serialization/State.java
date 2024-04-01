package oop.serialization;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для сохранения и загрузки состояния компонентов
 */
public class State {
    /**
     * Путь к файлу конфигурации
     */
    private final File confPath;

    /**
     * Конструктор класса
     */
    public State() {
        this.confPath = new File(System.getProperty("user.home") +
                File.separator + "States.conf");
    }

    /**
     * Сохраняет состояние указанных компонентов в файл конфигурации
     *
     * @param windows Список компонентов, состояние которых нужно сохранить
     */
    public void saveStates(List<? extends Component> windows) {
        Map<String, String> states = new HashMap<>();
        for (Component component : windows) {
            if (component instanceof Storable window) {
                window.save(states);
            }
        }

        try (OutputStream outputStream = new FileOutputStream(confPath)) {
            for (Map.Entry<String, String> entry : states.entrySet()) {
                String line = entry.getKey() + "=" + entry.getValue() + "\n";
                outputStream.write(line.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось записать в файл: " + confPath.getPath());
        }
    }

    /**
     * Загружает состояние указанных компонентов из файла конфигурации
     *
     * @param windows Список компонентов, состояние которых нужно загрузить
     */
    public void loadStates(List<? extends Component> windows) {
        if (!confPath.exists()) {
            try {
                if (confPath.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException("Не удалось открыть файл: " + confPath.getPath());
            }
        }
        Map<String, String> states = new HashMap<>();
        try (InputStream inputStream = new FileInputStream(confPath)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    states.put(key, value);
                }
            }
            for (Component component : windows) {
                if (component instanceof Storable window) {
                    window.restore(states);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочить из файла: " + confPath.getPath());
        }
    }

}
