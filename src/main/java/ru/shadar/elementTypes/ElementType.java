package ru.shadar.elementTypes;

/**
 * Абстркатный класс подтипа элемента (необходим для группировки элементов и получения данных об этих группах)
 */
public abstract class ElementType implements Comparable<ElementType> {
    private final String name;
    private final String names;
    private final String type;

    public ElementType(String type, String name, String names) {
        this.type = type;
        this.name = name;
        this.names = names;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getNames() {
        return names;
    }

    @Override
    public int compareTo(ElementType elt) {
        return this.getType().compareTo(elt.getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementType that = (ElementType) o;

        return getType().equals(that.getType());
    }

    @Override
    public int hashCode() {
        return getType().hashCode();
    }

    @Override
    public String toString() {
        return this.type;
    }
}
