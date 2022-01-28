package ru.shadar.objects;

import javafx.beans.property.SimpleStringProperty;
import ru.shadar.elementTypes.ElementType;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывающий отдельный узел исполнения.
 */
public class Node implements Comparable<Node> {
    private SimpleStringProperty name = new SimpleStringProperty("<input data>");
    private SimpleStringProperty position = new SimpleStringProperty();
    private SimpleStringProperty decimalNumber = new SimpleStringProperty("<input data>");

    private final List<Group> elements = new ArrayList<>();

    public Node(String position) {
        this.setPosition(position);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPosition() {
        return position.get();
    }

    public SimpleStringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getDecimalNumber() {
        return decimalNumber.get();
    }

    public SimpleStringProperty decimalNumberProperty() {
        return decimalNumber;
    }

    public void setDecimalNumber(String decimalNumber) {
        this.decimalNumber.set(decimalNumber);
    }

    public List<Group> getElementGroups() {
        return elements;
    }

    public static Node createNode(SimpleElement simpleElement) {
        int varNumber = simpleElement.getVariantNumber();
        String varNum;
        if (varNumber == 0) {
            varNum = "-base";
        } else if (varNumber > 10) {
            varNum = "-" + (varNumber - 1);
        } else {
            varNum = "-0" + (varNumber - 1);
        }
        Node node = new Node(simpleElement.getNode());
        node.setDecimalNumber(node.getDecimalNumber() + varNum);
        return node;
    }

    public void addElement(ElementType elementType, Element element) {
        for (Group g : elements) {
            if (g.getElementType().equals(elementType)) {
                g.addElement(element);
                return;
            }
        }
        Group group = new Group(elementType);
        group.addElement(element);
        elements.add(group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return getPosition().equals(node.getPosition());
    }

    @Override
    public int hashCode() {
        return getPosition().hashCode();
    }

    @Override
    public int compareTo(Node n) {
        if (this.getPosition().equals("") && n.getPosition().equals("")) {
            return 0;
        }
        if (this.getPosition().equals("")) {
            return -1;
        }
        if (n.getPosition().equals("")) {
            return 1;
        }
        String thisChar = this.getPosition().replaceAll("\\d", "");
        int thisInt = Integer.parseInt(this.getPosition().replaceAll("\\D", ""));
        String anotherChar = n.getPosition().replaceAll("\\d", "");
        int anotherInt = Integer.parseInt(n.getPosition().replaceAll("\\D", ""));
        return thisChar.compareTo(anotherChar) != 0
                ? thisChar.compareTo(anotherChar) : Integer.compare(thisInt, anotherInt);
    }


    public boolean equalsWithoutPosition(Node n) {
        boolean rsl = this.getName().equals(n.getName())
                || n.getName().replaceAll(" ", "").equals("");
        if (!this.getDecimalNumber().equals(n.getDecimalNumber())
                || n.getDecimalNumber().replaceAll(" ", "").equals("")) {
            rsl = false;
        }
        if (!this.getElementGroups().equals(n.getElementGroups())) {
            rsl = false;
        }
        return rsl;
    }

    @Override
    public String toString() {
        return System.lineSeparator()
                + "--N--"
                + System.lineSeparator()
                + "Node{" +
                "name=" + name +
                ", position=" + position +
                ", decimalNumber=" + decimalNumber +
                ", elementsGroup=" + elements
                + '}';
    }
}
