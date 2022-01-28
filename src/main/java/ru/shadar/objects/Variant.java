package ru.shadar.objects;

import java.util.*;

/**
 * Класс описывающий отдельное исполнение изделия.
 */
public class Variant implements Comparable<Variant> {
    private final int number;

    private List<Node> nodes = new ArrayList<>();

    public Variant(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void addNodeIfNotExist(Node node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    private void validateNods() {

    }

    @Override
    public int compareTo(Variant o) {
        return Integer.compare(this.getNumber(), o.getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variant variant = (Variant) o;

        return getNumber() == variant.getNumber();
    }

    @Override
    public int hashCode() {
        return getNumber();
    }

    @Override
    public String toString() {
        return System.lineSeparator()
                + System.lineSeparator()
                + "--V--"
                + System.lineSeparator()
                + "Variant{" +
                "number=" + number +
                ", nodes=" + nodes +
                '}';
    }
}
