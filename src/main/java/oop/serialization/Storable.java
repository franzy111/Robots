package oop.serialization;

import java.util.Map;

/**
 * Интерфейс для классов, которые могут сохранять и восстанавливать свое состояние
 */
public interface Storable {
    /**
     * Сохраняет состояние объекта
     *
     * @param states Map, в которой будет сохранено состояние объекта
     */
    void save(Map<String, String> states);

    /**
     * Восстанавливает состояние объекта
     *
     * @param states Map из которой будет восстановлено состояние объекта
     */
    void restore(Map<String, String> states);
}
