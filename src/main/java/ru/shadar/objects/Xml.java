package ru.shadar.objects;

import ru.shadar.modules.SaxParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Класс описывающий XML файл
 */
public class Xml {
    private final String path;

    private List<Map<String, String>> parseResult;
    private List<String> elementParams;

    public Xml(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    /**
     * Метод реализующий парсинг файла XML.
     * Создает внутреннюю переменную List< Map< String, String>> результатов
     */
    public void parse() {
        List<Map<String, String>> rsl;
        try {
            SaxParser parser = new SaxParser();
            rsl = parser.parse(this);
        } catch (IllegalArgumentException e) {
            rsl = null;
            System.out.println("Результат отсутствует.");
//            MainController.fileLoadError(e.getMessage());
        }
        this.parseResult = rsl;
        if (rsl != null) {
            createElementsParams();
            //TODO delNextLine
            System.out.println("Парсинг прошел удачно");
        }
    }

    /**
     * Метод возвращающий результаты парсинга
     *
     * @return List<Map < String, String>> результат
     */
    public List<Map<String, String>> getParseResult() {
        return parseResult;
    }

    /**
     * Генерация параметров будущих элементов
     */
    private void createElementsParams() {
        elementParams = new ArrayList<>();
        for (Map<String, String> m : parseResult) {
            for (String s : m.keySet()) {
                if (!elementParams.contains(s)) {
                    elementParams.add(s);
                }
            }
        }
        System.out.println(elementParams);
    }

    /**
     * Метод возвращает параметры элементов
     *
     * @return elementParams - параметры, по которым будут собраны элементы.
     */
    public List<String> getElementParams() {
        return elementParams;
    }
}
