package ru.shadar.objects;

import ru.shadar.elementTypes.ElementType;
import ru.shadar.elementTypes.*;

import java.util.*;

/**
 * Класс описывающий отдельный узел исполнения.
 */
public class Group implements Comparable<Group> {

    private final String name;
    private final ElementType elementType;
    private final List<Element> elements = new ArrayList<>();
    private boolean groupType = false;
    private boolean groupDatasheet = false;


    public Group(ElementType elementType) {
        this.elementType = elementType;
        this.name = elementType.getName();
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public List<Map<String, String>> record() {
        return group();
    }

    @Override
    public int compareTo(Group o) {
        return this.elementType.compareTo(o.elementType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Group group = (Group) o;

        if (!name.equals(group.name)) {
            return false;
        }
        if (!elementType.equals(group.elementType)) {
            return false;
        }
        return elements.equals(group.elements);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + elementType.hashCode();
        result = 31 * result + elements.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return System.lineSeparator()
                + "--G--"
                + System.lineSeparator()
                + "Group{"
                + "name='" + name + '\''
                + ", elementType=" + elementType
                + ", elements=" + elements
                + '}';
    }

    private String getGroupPositions(List<Integer> positions) {
        StringBuilder rsl = new StringBuilder();
        String type = elementType.getType();
        if (positions.size() == 1) {
            rsl.append(type).append(positions.get(0));
        }
        for (int i = 1; i < positions.size(); i++) {
            int tmp = positions.get(i - 1);
            int groupEnd = tmp;
            int count = 1;
            while (i < positions.size() && (positions.get(i) - 1 == positions.get(i - 1))) {
                groupEnd = positions.get(i);
                count++;
                i++;
            }
            if (!rsl.isEmpty()) {
                rsl.append(",");
            }
            if (count == 1) {
                rsl.append(type)
                        .append(tmp);
            }
            if (count == 2) {
                rsl.append(type)
                        .append(tmp)
                        .append(",")
                        .append(type)
                        .append(groupEnd);
            }
            if (count > 2) {
                rsl.append(type)
                        .append(tmp)
                        .append("-")
                        .append(type)
                        .append(groupEnd);
            }
            if (i == positions.size() - 1 && groupEnd != positions.get(i)) {
                rsl.append(",")
                        .append(type)
                        .append(positions.get(i));
            }
        }
        return rsl.toString();
    }

    private void setGroupDatasheet() {
        boolean rsl = true;
        Element random = elements.get(0);
        for (Element e : elements) {
            if (!e.getDatasheet().equals(random.getDatasheet())) {
                rsl = false;
                break;
            }
        }
        this.groupDatasheet = rsl;
    }

    private void setGroupType() {
        boolean rsl = true;
        Element random = elements.get(0);
        for (Element e : elements) {
            if (!e.getType().equals(random.getType())) {
                rsl = false;
                break;
            }
        }
        this.groupType = rsl;
    }

    private List<Map<String, String>> group() {
        elements.sort(Comparator.comparingInt(Element::getPosition));
        List<Map<String, String>> rsl = new ArrayList<>();
        setGroupType();
        setGroupDatasheet();
        Map<String, String> tempMap = new HashMap<>();
        if (elements.size() == 1) {
            List<String> tmp = new ArrayList<>();
            tempMap.put("ПН", elementType.getType() + elements.get(0).getPosition());
            if (elementType instanceof C || elementType instanceof R) {
                tempMap.put("Н", elementType.getName() + " " + elements.get(0).getName()
                        + " " + elements.get(0).getDatasheet());
            } else {
                tempMap.put("Н", elements.get(0).getName() + " " + elements.get(0).getDatasheet());
            }
            tempMap.put("К", "1");
            tempMap.put("П", elements.get(0).getTypeSize());
            rsl.add(tempMap);
        } else {
            StringBuilder stbl = new StringBuilder(elementType.getNames());
            if (groupType) {
                stbl.append(" ")
                        .append(elements.get(0).getType());
            }
            if (groupDatasheet) {
                stbl.append(" ")
                        .append(elements.get(0).getDatasheet());
            }
            tempMap.put("Н", stbl.toString());
            rsl.add(tempMap);
            for (int i = 1; i < elements.size(); i++) {
                int tempI = i - 1;
                int count = 1;
                List<Integer> positions = new ArrayList<>();
                positions.add(elements.get(tempI).getPosition());
                while (i < elements.size() &&
                        elements.get(i).getName().equals(elements.get(i - 1).getName())
                        && elements.get(i).getType().equals(elements.get(i - 1).getType())
                        && elements.get(i).getTypeSize().equals(elements.get(i - 1).getTypeSize())
                        && elements.get(i).getDatasheet().equals(elements.get(i - 1).getDatasheet())) {
                    positions.add(elements.get(i).getPosition());
                    count++;
                    i++;
                }
                String pos = getGroupPositions(positions);
                rsl.add(getElementsDataMap(elements.get(tempI), pos, count));
                if (i == elements.size() - 1
                        && positions.get(positions.size() - 1) != elements.get(i).getPosition()) {
                    List<Integer> lastElement = new ArrayList<>();
                    lastElement.add(elements.get(i).getPosition());
                    rsl.add(getElementsDataMap(elements.get(i), getGroupPositions(lastElement), 1));
                }
            }
            rsl.add(new HashMap<>());
        }
        return rsl;
    }

    private Map<String, String> getElementsDataMap(Element element, String positions, int count) {
        Map<String, String> tmp = new HashMap<>();
        tmp.put("ПН", positions);
        tmp.putAll(getElementBasePartMap(element, count));
        return tmp;
    }

    private Map<String, String> getElementBasePartMap(Element element, int count) {
        Map<String, String> rsl = new HashMap<>();
        if (groupType) {
            rsl.put("Н", element.getName().replaceAll(element.getType(), ""));
        } else {
            rsl.put("Н", element.getName() + " " + element.getDatasheet());
        }
        rsl.put("К", String.valueOf(count));
        rsl.put("typeSize", element.getTypeSize());
        if (!groupType) {
            rsl.put("type", element.getType());
        }
        return rsl;
    }
}
