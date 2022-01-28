package ru.shadar.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс описывающий отдельное устройство.
 * Устройство по структуре представляет собой дерево:
 * исполнения (variant) -> узлы (node) -> группы элементов ( group) -> элементы (Element)
 */
public class Device {
    private static String name;
    private static String decimalNumber;
    private int numberOfVariants;
    private Variant[] variants;

    public Device(int numberOfVariants) {
        this.numberOfVariants = numberOfVariants;
        variants = new Variant[this.numberOfVariants];
    }

    public static void setName(String name) {
        Device.name = name;
    }

    public static void setDecimalNumber(String decimalNumber) {
        Device.decimalNumber = decimalNumber;
    }

    public static String getName() {
        return name;
    }

    public static String getDecimalNumber() {
        return decimalNumber;
    }

    public Variant[] getVariants() {
        return variants;
    }

    public List<Node> getNodeList() {
        List<Node> rsl = new ArrayList<>();
        for (Variant v : variants) {
            rsl.addAll(v.getNodes());
        }
        return rsl;
    }

    @Override
    public String toString() {
        return System.lineSeparator()
                + "Device{"
                + "name=" + name
                + ", numberOfVariants=" + numberOfVariants
                + ", variants=" + Arrays.toString(variants)
                + '}';
    }
}
