package ru.shadar.models;

import ru.shadar.objects.*;

import java.util.*;

/**
 * Реализация интерфейса Record, создает перечень элементов (ПЭ3)
 */
public class ListOfElements implements Record {
    private Device device;
    private List<Map<String, String>> record;

    public ListOfElements(Device device) {
        this.device = device;
    }

    @Override
    public List<Map<String, String>> getRecord() {
        return record;
    }

    @Override
    public String getName() {
        return Device.getName();
    }

    @Override
    public String getDecimalNumber() {
        return Device.getDecimalNumber();
    }

    @Override
    public void record() {
        List<Map<String, String>> rsl = new ArrayList<>();
        for (Variant variant : device.getVariants()) {
            if (variant.getNumber() != 0) {
                Map<String, String> varName = new HashMap<>();
                String variantNumber;
                if (variant.getNumber() == 1) {
                    variantNumber = "";
                } else if (variant.getNumber() <= 10) {
                    variantNumber = "-0" + (variant.getNumber() - 1);
                } else {
                    variantNumber = "-" + (variant.getNumber() - 1);
                }
                varName.put("Н", Device.getName() + variantNumber);
                rsl.add(varName);
            }
            variant.getNodes().sort(Comparator.naturalOrder());
            if (variant.getNodes().size() == 1 ||
                    (variant.getNodes().size() < 3 && variant.getNodes().get(0).getPosition().equals(""))) {
                rsl.addAll(mapSimpleCreation(variant.getNodes()));
            } else {
                rsl.addAll(mapCreation(variant.getNodes()));
            }

        }
        validate(rsl);
        record = rsl;
    }

    private void validate(List<Map<String, String>> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).size() == 0 && list.get(i-1).size() == 0){
                list.remove(i);
                i--;
            }
        }
    }

    private List<Map<String, String>> mapSimpleCreation(List<Node> nods) {
        List<Map<String, String>> rsl = new ArrayList<>();
        for (Node node : nods) {
            var tempMap = createNodeMap(node, null, 1);
            if (tempMap != null) {
                rsl.add(tempMap);
            }
            rsl.addAll(getGroupList(node));
        }
        return rsl;
    }

    private List<Map<String, String>> mapCreation(List<Node> nods) {
        List<Map<String, String>> rsl = new ArrayList<>();
        for (int i = 1; i < nods.size(); i++) {
            int temp = i - 1;
            int count = 1;
            var positions = new ArrayList<Integer>();
            if (!nods.get(temp).getPosition()
                    .replaceAll("\\D", "").equals("")) {
                while (i < nods.size()
                        && nods.get(i).equalsWithoutPosition(nods.get(i - 1))) {
                    positions.add(Integer.parseInt(
                            nods.get(i).getPosition().replaceAll("\\D", "")));
                    count++;
                    i++;
                }
                var pos = getNodePositions(positions, nods.get(temp).getPosition());
                rsl.add(createNodeMap(nods.get(temp), pos, count));
                rsl.addAll(getGroupList(nods.get(temp)));
                if (i == nods.size() - 1
                        && positions.get(positions.size() - 1) != Integer.parseInt(
                        nods.get(i).getPosition().replaceAll("\\D", ""))) {
                    var lastNode = new ArrayList<Integer>();
                    lastNode.add(Integer.parseInt(nods.get(i).getPosition()));
                    rsl.add(createNodeMap(nods.get(i),
                            getNodePositions(lastNode, nods.get(i).getPosition()),
                            1));
                    rsl.addAll(getGroupList(nods.get(i)));
                }
            }
        }
        return rsl;
    }

    private Map<String, String> createNodeMap(Node node, String address, int count) {
        Map<String, String> nodeN = null;
        if (!node.getPosition().replaceAll(" ", "").equals("")) {
            nodeN = new HashMap<>();
            if (count == 1) {
                nodeN.put("ПН", node.getPosition());
            } else {
                nodeN.put("ПН", address);
            }
            nodeN.put("Н", node.getDecimalNumber() + " " + node.getName());
            nodeN.put("К", String.valueOf(count));
        }
        return nodeN;
    }

    private List<Map<String, String>> getGroupList(Node node) {
        List<Map<String, String>> rsl = new ArrayList<>();
        node.getElementGroups().sort(Comparator.naturalOrder());
        for (Group group : node.getElementGroups()) {
            rsl.addAll(group.record());
            rsl.add(new HashMap<>());
        }
        return rsl;
    }

    private String getNodePositions(List<Integer> positions, String nodePosition) {
        StringBuilder rsl = new StringBuilder();
        String node = nodePosition.replaceAll("\\d", "");
        if (positions.size() == 1) {
            rsl.append(node).append(positions.get(0));
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
                rsl.append(node)
                        .append(tmp);
            }
            if (count == 2) {
                rsl.append(node)
                        .append(tmp)
                        .append(",")
                        .append(node)
                        .append(groupEnd);
            }
            if (count > 2) {
                rsl.append(node)
                        .append(tmp)
                        .append("-")
                        .append(node)
                        .append(groupEnd);
            }
            if (i == positions.size() - 1 && groupEnd != positions.get(i)) {
                rsl.append(",")
                        .append(node)
                        .append(positions.get(i));
            }
        }
        return rsl.toString();
    }
}
