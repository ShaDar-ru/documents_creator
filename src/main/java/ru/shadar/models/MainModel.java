package ru.shadar.models;

import ru.shadar.controllers.MainController;
import ru.shadar.modules.DeviceGenerator;
import ru.shadar.modules.PE3FileWriter;
import ru.shadar.modules.XmlDataGetter;
import ru.shadar.objects.Device;
import ru.shadar.objects.Xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Основная модель JAVAFX приложения
 */
public class MainModel {
    private static MainModel instance = null;
    private MainController controller;

    private Map<String, String> elementSheet = new HashMap<>();
    private Device device;

//    private List<Node> nods;

    private Xml xml;

    /**
     * Конструктор-заглушка
     */
    private MainModel() {
    }

    /**
     * Реализация паттерна Синглтон
     *
     * @return единственный объект MainModel
     */
    public static MainModel getInstance() {
        if (instance == null) {
            instance = new MainModel();
        }
        return instance;
    }

    /**
     * Метод устанавливающий связь модель-контроллер для реализации MVC
     *
     * @param controller объект контроллера
     */
    public void setController(MainController controller) {
        this.controller = controller;
    }



    /**
     * Метод собирающий мап ключей (реализует связь: поле из ГПИ - поле из ХМЛ)
     *
     * @param valueFromView    поле из ГПИ
     * @param valueFromElement поле из ХМЛ
     */
    @Deprecated
    public void addEntryToMap(String valueFromView, String valueFromElement) {
        elementSheet.put(valueFromView, valueFromElement);
    }

    /**
     * Возвращает мап пар: поля ГПИ-ХМЛ
     *
     * @return Map<String, String> ключи соответствия ГПИ-ХМЛ
     */
    @Deprecated
    public Map<String, String> getElementSheet() {
        return elementSheet;
    }

    /**
     * Метод собирающий устройство
     *
     * @param parseData - данные парсинга
     */
    public void generateDevice(List<Map<String, String>> parseData) {
        var data = XmlDataGetter.getData(parseData);
        device = DeviceGenerator.createDevice(XmlDataGetter.getVariantsNumber(parseData), data);
    }

    public Record createRecord(){
        Record listOfElements = new ListOfElements(device);
        listOfElements.record();
        return listOfElements;
    }

    public PE3FileWriter createWriter(Record record, String address) throws IOException {
        var map = record.getRecord();
        return new PE3FileWriter(map, address);
    }


}
