package ru.shadar.objects;

import java.util.Objects;

/**
 * Класс - модель данных элементов.
 * Получается из исходных даннх XML файла
 */
public class SimpleElement {
    private int variantNumber;

    private String name;
    private String type;
    private String typeSize;
    private String datasheet;
    private String address;
    private String node;
    private String elementType;
    private String magn;
    private String magnData;

    private int position;

    public int getVariantNumber() {
        return variantNumber;
    }

    public void setVariantNumber(int variantNumber) {
        this.variantNumber = variantNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeSize() {
        return typeSize;
    }

    public void setTypeSize(String typeSize) {
        this.typeSize = typeSize;
    }

    public String getDatasheet() {
        return datasheet;
    }

    public void setDatasheet(String datasheet) {
        this.datasheet = datasheet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleElement that = (SimpleElement) o;
        if (position != that.position) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        if (!Objects.equals(type, that.type)) {
            return false;
        }
        if (!Objects.equals(typeSize, that.typeSize)) {
            return false;
        }
        if (!Objects.equals(datasheet, that.datasheet)) {
            return false;
        }
        if (!address.equals(that.address)) {
            return false;
        }
        if (!Objects.equals(node, that.node)) {
            return false;
        }
        return Objects.equals(elementType, that.elementType);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (typeSize != null ? typeSize.hashCode() : 0);
        result = 31 * result + (datasheet != null ? datasheet.hashCode() : 0);
        result = 31 * result + address.hashCode();
        result = 31 * result + (node != null ? node.hashCode() : 0);
        result = 31 * result + (elementType != null ? elementType.hashCode() : 0);
        result = 31 * result + position;
        return result;
    }

    @Override
    public String toString() {
        return System.lineSeparator() + "SimpleElement{" +
                "variantNumber=" + variantNumber +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", typeSize='" + typeSize + '\'' +
                ", datasheet='" + datasheet + '\'' +
                ", address='" + address + '\'' +
                ", node='" + node + '\'' +
                ", elementType='" + elementType + '\'' +
                ", position=" + position +
                '}';
    }
}
