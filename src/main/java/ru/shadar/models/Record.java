package ru.shadar.models;


import java.util.List;
import java.util.Map;

/**
 * Интерфейс для создания моделей данных, которые потом записываются в excel файлы
 */
public interface Record {

    String getName();

    String getDecimalNumber();

    void record();

    List<Map<String, String>> getRecord();
}
