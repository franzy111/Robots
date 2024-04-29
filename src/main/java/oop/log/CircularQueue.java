package oop.log;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

/**
 * Циклическая очередь, которая использует буфер фиксированного размера для хранения элементов.
 * Когда буфер заполняется, новые элементы перезаписывают самые старые элементы.
 *
 * @param <T> тип элементов, хранящихся в очереди
 */
public class CircularQueue<T> {
    private final T[] queue;
    private final int capacity;
    private int size;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = (T[]) new Object[capacity];
        this.size = 0;

    }

    /**
     * Добавляет элемент в конец очереди.
     * Если очередь заполнена, самый старый элемент перезаписывается.
     *
     * @param element добавляемый элемент
     */
    public synchronized void put(T element) {
        queue[size % capacity] = element;
        size++;
    }

    /**
     * Извлекает элемент по указанному индексу в очереди.
     * Если индекс выходит за пределы допустимого диапазона, выбрасывается исключение {@link IndexOutOfBoundsException}.
     *
     * @param index индекс извлекаемого элемента
     * @return элемент по указанному индексу
     */
    public synchronized T take(int index) {
        if (index >= capacity || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + "out of range");
        }
        return size < capacity ? queue[index] : queue[(size + index) % capacity];
    }

    /**
     * Возвращает текущий размер очереди.
     * Размер - это количество элементов, которые в данный момент хранятся в очереди.
     *
     * @return размер очереди
     */
    public int size() {
        return min(size, capacity);
    }

    /**
     * Возвращает подсписок элементов в очереди.
     * Подсписок начинается с указанного начального индекса и заканчивается указанным конечным индексом.
     * Если какой-либо индекс выходит за пределы допустимого диапазона,
     * выбрасывается исключение {@link IndexOutOfBoundsException}.
     *
     * @param startFrom начальный индекс подсписка
     * @param indexTo   конечный индекс подсписка
     * @return подсписок элементов
     */
    public Iterable<T> subList(int startFrom, int indexTo) {
        if (startFrom < 0 || startFrom > size() || indexTo < 0 || indexTo > size()) {
            throw new IndexOutOfBoundsException("Index out of range");
        }
        int subListCapacity = indexTo - startFrom;
        List<T> subList = new ArrayList<>(subListCapacity);
        for (int i = 0; i < subListCapacity; i++) {
            subList.add(size < capacity ? queue[i + startFrom] : queue[(size + i + startFrom) % capacity]);
        }
        return subList;
    }
}
