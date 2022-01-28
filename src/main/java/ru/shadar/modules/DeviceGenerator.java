package ru.shadar.modules;

import ru.shadar.elementTypes.*;
import ru.shadar.objects.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Класс реалищующий генерацию устройства (сборка всех исполнений,
 * наполнение узлами, наполнение узлов внутренними данными и т.д.)
 */
public class DeviceGenerator {

    private static void sortInputData(List<SimpleElement> data) {
        data.sort(Comparator
                .comparingInt(SimpleElement::getVariantNumber)
                .thenComparing(SimpleElement::getNode)
                .thenComparing(SimpleElement::getElementType)
                .thenComparing(SimpleElement::getPosition));
    }

    private static List<SimpleElement> createBase(int variantsNumber, List<SimpleElement> data) {
        List<SimpleElement> rsl = new ArrayList<>();
        sortInputData(data);
        for (int i = 0; i < data.size(); i++) {
            SimpleElement el = data.get(i);
            int count = 0;
            for (int j = i; j < data.size(); j++) {
                if (data.get(i).equals(data.get(j))) {
                    count++;
                }
            }
            if (count == variantsNumber + 1) {
                el.setVariantNumber(0);
                rsl.add(el);
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).equals(el)) {
                        data.remove(j);
                        j--;
                    }
                }
                i--;
            }
        }
        for (SimpleElement element : data) {
            element.setVariantNumber(element.getVariantNumber() + 1);
            rsl.add(element);
        }
        return rsl;
    }

    public static Device createDevice(int variantsNumber, List<SimpleElement> data) {
        int variants;
        if (variantsNumber == 0) {
            variants = 1;
        } else {
            variants = variantsNumber + 2;
        }
        Device device = new Device(variants);
        List<SimpleElement> base = createBase(variantsNumber, data);
        sortInputData(base);
        base.forEach(x -> {
            int varNumber = x.getVariantNumber();
            Variant variant = device.getVariants()[varNumber];
            if (variant == null) {
                variant = new Variant(varNumber);
                device.getVariants()[varNumber] = variant;
            }
            Node node = Node.createNode(x);
            variant.addNodeIfNotExist(node);
            node = variant.getNodes().get(variant.getNodes().indexOf(node));
            node.addElement(createElType(x), Element.createElement(x));
        });
        return device;
    }

    private static ElementType createElType(SimpleElement element) {
        String type = element.getElementType().toUpperCase();
        ElementType elementType;
        switch (type) {
            case "C" -> elementType = new C();
            case "DA" -> elementType = new DA();
            case "DD" -> elementType = new DD();
            case "L" -> elementType = new L();
            case "R" -> elementType = new R();
            case "TA" -> elementType = new TA();
            case "TV" -> elementType = new TV();
            case "VD" -> elementType = new VD();
            case "VT" -> elementType = new VT();
            default -> elementType = new Other(type, "", "");
        }
        return elementType;
    }

    public static void main(String[] args) {
//        Xml xml = new Xml("/home/alex/Загрузки/EXAMPLE_test1.xml");
        Xml xml = new Xml("/home/alex/Загрузки/EXAMPLE_good.xml");
//        Xml xml = new Xml("/home/alex/Загрузки/PCB_Project_1.xml");
        xml.parse();
        var data = xml.getParseResult();
        var xmlData = XmlDataGetter.getData(data);
        var device = DeviceGenerator.createDevice(XmlDataGetter.getVariantsNumber(data), xmlData);
//        System.out.println(device);
        for (Variant v : device.getVariants()) {
            v.getNodes().forEach(x -> x.getElementGroups().forEach(y -> System.out.println(y.record())));
        }
    }


}
