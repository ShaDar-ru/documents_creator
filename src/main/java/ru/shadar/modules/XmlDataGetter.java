package ru.shadar.modules;

import ru.shadar.objects.SimpleElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс преобразующий данные после парсинга в список SimpleElement
 */
public class XmlDataGetter {

    public static int getVariantsNumber(List<Map<String, String>> parseData) {
        return setVariantsNumber(parseData);
    }

    /**
     * Метод собирает POJO Element в список элементов
     *
     * @param parseData данные из XML
     * @return List&lt{@link SimpleElement}&gt
     */
    public static List<SimpleElement> getData(List<Map<String, String>> parseData) {
        int variants = setVariantsNumber(parseData);
        return createInnerData(parseData, variants);
    }

    /**
     * Метод устанавливает количество исполнений изделия
     *
     * @param parseData данные из ХМЛ
     */
    private static int setVariantsNumber(List<Map<String, String>> parseData) {
        return parseData.get(0)
                .keySet()
                .stream()
                .map(x -> {
                    int rsl = 0;
                    if (x.contains("_")) {
                        String[] a = x.split("_");
                        String b = a[1].replaceAll("\\D", "")
                                .replaceAll(" ", "")
                                .replaceAll("_", "");
                        rsl = b.equals("") ? 0 : Integer.parseInt(b);
                    }
                    return rsl;
                })
                .max(Integer::compare)
                .orElse(0);
    }

    /**
     * Оюъединяет элементы полученные из разных мап
     *
     * @param parseData входные данные
     * @param variants  количество исполнений изделия
     * @return финальный список данных
     */
    private static List<SimpleElement> createInnerData(List<Map<String, String>> parseData, int variants) {
        List<SimpleElement> rsl = new ArrayList<>();
        parseData.forEach(x ->
                rsl.addAll(createElementsFromMap(x, variants)));
        return rsl;
    }

    /**
     * Генерирует элементы из отдельной мапы
     *
     * @param parseMap мапа для генерации элементов
     * @param variants количество исполнений
     * @return список элементов отдельной мапы
     */
    private static List<SimpleElement> createElementsFromMap(Map<String, String> parseMap, int variants) {
        List<SimpleElement> rsl = new ArrayList<>();
        SimpleElement baseElem = createElement(parseMap, 0);
        rsl.add(baseElem);
        if (parseMap.containsKey("name_1")) {
            for (int i = 1; i <= variants; i++) {
                SimpleElement element = createElement(parseMap, i);
                if (element.getName().replaceAll(" ", "").equals("")) {
                    element.setName(baseElem.getName());
                    element.setAddress(baseElem.getAddress());
                    element.setNode(baseElem.getNode());
                    element.setElementType(baseElem.getElementType());
                    element.setPosition(baseElem.getPosition());
                    element.setDatasheet(baseElem.getDatasheet());
                    element.setType(baseElem.getType());
                    element.setTypeSize(baseElem.getTypeSize());
                    element.setMagn(baseElem.getMagn());
                    element.setMagnData(baseElem.getMagnData());
                }
                rsl.add(element);
            }
        }
        return rsl;
    }

    private static SimpleElement createElement(Map<String, String> parseMap, int variantIndex) {
        String postfix = "";
        if (variantIndex != 0) {
            postfix = "_" + variantIndex;
        }
        SimpleElement element = new SimpleElement();
        element.setVariantNumber(variantIndex);
        String address = parseMap.get("address");
        element.setAddress(address);
        element.setNode(createNodeAsString(address));
        element.setElementType(createElementType(address));
        element.setPosition(createPosition(address));
        element.setName(parseMap.get("name" + postfix));
        element.setDatasheet(parseMap.get("datasheet" + postfix));
        element.setType(parseMap.get("type" + postfix));
        element.setTypeSize(parseMap.get("case" + postfix));
        element.setMagn(parseMap.get("magn" + postfix));
        element.setMagnData(parseMap.get("magndata" + postfix));
        return element;
    }

    private static String createNodeAsString(String address) {
        String rsl = "";
        if (address.contains("-")) {
            String[] temp = address.split("-");
            rsl = temp[0];
        }
        return rsl;
    }

    private static int createPosition(String address) {
        String rsl;
        if (address.contains("-")) {
            String[] temp = address.split("-");
            rsl = temp[1];
        } else {
            rsl = address;
        }
        return Integer.parseInt(rsl.replaceAll("\\D", ""));
    }


    private static String createElementType(String address) {
        String rsl;
        if (address.contains("-")) {
            String[] temp = address.split("-");
            rsl = temp[1];
        } else {
            rsl = address;
        }
        return rsl.replaceAll("\\d", "");
    }
}
