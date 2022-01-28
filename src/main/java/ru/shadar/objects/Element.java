package ru.shadar.objects;

import java.util.Objects;

/**
 * Класс описывающий модель данных - элемент
 */
public class Element {
    private String name;
    private String type;
    private String typeSize;
    private String datasheet;
    private String magn;
    private String magnData;
    private int position;

    private Element(int position, String name, String type, String typeSize, String datasheet) {
        this.position = position;
        this.name = name;
        this.type = type;
        this.typeSize = typeSize;
        this.datasheet = datasheet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTypeSize() {
        return typeSize;
    }

    public String getDatasheet() {
        return datasheet;
    }

    public int getPosition() {
        return position;
    }

    public String getMagn() {
        return magn;
    }

    public void setMagn(String magn) {
        this.magn = magn;
    }

    public String getMagnData() {
        return magnData;
    }

    public void setMagnData(String magnData) {
        this.magnData = magnData;
    }

    public static Element createElement(SimpleElement simpleElement) {
        Element element = new Element(
                simpleElement.getPosition(),
                validatePoint(simpleElement.getName()),
                validatePoint(simpleElement.getType()),
                validatePoint(simpleElement.getTypeSize()),
                validatePoint(simpleElement.getDatasheet()));
        if (simpleElement.getMagn() != null || simpleElement.getMagnData() != null) {
            element.setMagn(validatePoint(simpleElement.getMagn()));
            element.setMagnData(validatePoint(simpleElement.getMagnData()));
        }
        validate(element);
        return element;
    }

    private static String validatePoint(String string) {
        return string == null ? "" : string;
    }

    private static void validate(Element element) {
        if (element.getName().equals("-") || element.getName().equalsIgnoreCase("отсутствует")) {
            element.setName("Отсутствует");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Element element = (Element) o;

        if (position != element.position) {
            return false;
        }
        if (!name.equals(element.name)) {
            return false;
        }
        if (!Objects.equals(type, element.type)) {
            return false;
        }
        if (!typeSize.equals(element.typeSize)) {
            return false;
        }
        return datasheet.equals(element.datasheet);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + typeSize.hashCode();
        result = 31 * result + datasheet.hashCode();
        result = 31 * result + position;
        return result;
    }

    @Override
    public String toString() {
        return System.lineSeparator()
                + "Element{"
                + "position= " + position
                + " name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", typeSize='" + typeSize + '\'' +
                ", datasheet='" + datasheet + '\'' +
                '}';
    }
}
