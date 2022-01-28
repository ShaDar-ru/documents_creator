package ru.shadar.modules;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.shadar.models.ListOfElements;
import ru.shadar.objects.Xml;

import java.io.*;
import java.net.URL;

import java.util.List;
import java.util.Map;

/**
 * Класс, реализующий запись в конечные файлы.
 * Принцип:
 * 1. взять шаблон,
 * 2. заполнить 1 страницу,
 * 3. сделать копию последней (пустой страницы),
 * 4. заполнить следующую.
 * 5. пункты 3, 4 повторять пока не кончатся входные данные.
 *
 * на текущий момент реализован не до конца
 * psvm используется для проверки корректности генерации
 */
public class PE3FileWriter {
    private static final int NUMBER_OF_LINES_FIRST_LIST = 25;
    private static final int NUMBER_OF_LINES_OTHER_LISTS = 32;
    private static final int CHARS_IN_CELL_POSITION = 7;
    private static final int CHARS_IN_CELL_NAME = 45;
    private static final int CHARS_IN_CELL_COUNT = 3;
    private static final int CHARS_IN_CELL_NOTE = 12;

    private File template;
    private File generate;

    private String address;

    private List<Map<String, String>> dataMap;

    public PE3FileWriter(List<Map<String, String>> dataMap, String address) throws IOException {
        this.dataMap = dataMap;
        this.address = address;
        getTemplate();
        createTempFile();
        copyFileUsingStream(template, generate);
    }

    public void setTemplate(File template) {
        this.template = template;
    }

    public File getGenerate() {
        return generate;
    }

    public void setGenerate(File generate) {
        this.generate = generate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private void getTemplate() {
        URL resource = PE3FileWriter.class.getResource("PE3_fixed_.xlsx");
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            this.template = new File(resource.getPath());
        }
    }

    private void createTempFile() throws IOException {
        File out = new File(this.address);
        generate = File.createTempFile("Перечень", ".xlsx", out);
    }

    private static void copyFileUsingStream(File source, File dest) {
        try (var in = new FileInputStream(source);
             var out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDoc() {

    }

    private XSSFWorkbook appendTemplateSheet(XSSFWorkbook workbook) {
        workbook.cloneSheet(workbook.getNumberOfSheets() - 1);
        return workbook;
    }

    private void initDataInSheet(List<Map<String, String>> dataMap,
                                 int indexOfDataLine, XSSFSheet sheet, int numberOfSheet) {
        int lines = 0;
        if (numberOfSheet == 0) {
            lines = NUMBER_OF_LINES_FIRST_LIST;
        } else {
            lines = NUMBER_OF_LINES_OTHER_LISTS;
        }
        int i = 1;
        breakLine:
        while (indexOfDataLine < dataMap.size() && i <= lines) {
            if (dataMap.get(indexOfDataLine).size() == 0) {
                indexOfDataLine++;
                i++;
                continue;
            }
            var tempMap = dataMap.get(indexOfDataLine);
            if (tempMap.get("К") == null
                    && dataMap.get(indexOfDataLine + 1).get("К") != null) {
                if (i + 1 + getMaxLinesToGroup(indexOfDataLine) > lines) {
                    break breakLine;
                }
            }
            var value = tempMap.get("ПН");
            if (value != null) {
                String[] values = createValuesArray(value, "ПН");
                if (values == null) {
                    simpleInit(value, "ПН", sheet, i);
                } else {
                    if (i + values.length > lines) {
                        break breakLine;
                    }
                    for (int j = 0; j < values.length; j++) {
                        simpleInit(values[j], "ПН", sheet, i);
                        if (j != values.length - 1) {
                            i++;
                        }
                    }
                }
            }
            value = tempMap.get("Н");
            if (value != null) {
                String[] values = createValuesArray(value, "Н");
                if (values == null) {
                    simpleInit(value, "Н", sheet, i);
                } else {
                    if (i + values.length > lines) {
                        break breakLine;
                    }
                    for (int j = 0; j < values.length; j++) {
                        simpleInit(values[j], "Н", sheet, i);
                        if (j != values.length - 1) {
                            i++;
                        }
                    }
                }
            }
            simpleInitDataInCell(tempMap, "К", sheet, i);
            simpleInitDataInCell(tempMap, "П", sheet, i);
            i++;
            indexOfDataLine++;
        }
    }

    private int getMaxLinesToGroup(int indexOfDataLine) {
        String[] positions = createValuesArray(dataMap.get(indexOfDataLine + 1).get("ПН"), "ПН");
        String[] names = createValuesArray(dataMap.get(indexOfDataLine + 1).get("Н"), "Н");
        return (positions == null ? 0 : positions.length) +
                (names == null ? 0 : names.length - 1);
    }

    private void simpleInit(String value, String key, XSSFSheet sheet, int i) {
        XSSFRow row = sheet.getRow(i);
        Cell cell = getCellByComment(row, key);
        cell.setCellValue(value);
    }

    private void simpleInitDataInCell(Map<String, String> tempMap,
                                      String key, XSSFSheet sheet, int i) {
        var value = tempMap.get(key);
        if (value != null) {
            simpleInit(value, key, sheet, i);
        }
    }

    private String[] createValuesArray(String value, String type) {
        String separator = "";
        int maxChars = 0;
        switch (type) {
            case "ПН" -> {
                separator = ",";
                maxChars = CHARS_IN_CELL_POSITION;
            }
            case "Н" -> {
                separator = " ";
                maxChars = CHARS_IN_CELL_NAME;
            }
        }
        String[] values = null;
        int k = 0;
        if (value.length() > maxChars) {
            var tempValues = value.split(separator);
            String[] temp = new String[tempValues.length];
            for (int i = 0; i < tempValues.length; i++) {
                StringBuilder builder = new StringBuilder();
                builder.append(tempValues[i]);
                while (i < tempValues.length - 1
                        && builder.toString().length() + 1 + tempValues[i + 1].length() <= maxChars) {
                    builder.append(separator).append(tempValues[i + 1]);
                    i++;
                }
                if (i != tempValues.length - 1) {
                    builder.append(separator);
                }
                temp[k] = builder.toString();
                k++;
            }
            values = new String[k];
            for (int j = 0; j < k; j++) {
                values[j] = temp[j];
            }
        }
        return values;
    }

    private Cell getCellByComment(XSSFRow row, String comment) {
        Cell rsl = null;
        for (Cell cell : row) {
            if (cell.getCellComment() != null
                    && cell.getCellComment().getString().getString().equals(comment)) {
                rsl = cell;
            }
        }
        return rsl;
    }

    public static void main(String[] args) throws IOException {
//        Xml xml = new Xml("/home/alex/Загрузки/EXAMPLE_test.xml");
//        Xml xml = new Xml("/home/alex/Загрузки/EXAMPLE_test1.xml");
        Xml xml = new Xml("/home/alex/Загрузки/EXAMPLE_good.xml");
//        Xml xml = new Xml("/home/alex/Загрузки/PCB_Project_1.xml");
        xml.parse();
        var data = xml.getParseResult();
        var xmlData = XmlDataGetter.getData(data);
        var device = DeviceGenerator.createDevice(XmlDataGetter.getVariantsNumber(data), xmlData);
        ListOfElements listOfElements = new ListOfElements(device);
        listOfElements.record();
        System.out.println(listOfElements.getRecord());
        var map = listOfElements.getRecord();
        PE3FileWriter writer = new PE3FileWriter(map, "/home/alex/Документы/");
        try (XSSFWorkbook workbook = new XSSFWorkbook(writer.generate);
             var out = new FileOutputStream(writer.generate, true)) {
            XSSFSheet cloneSheet = workbook.cloneSheet(1);
            workbook.setPrintArea(workbook.getSheetIndex(cloneSheet),
                    0,
                    15,
                    0,
                    36);
            System.out.println("++");
            writer.initDataInSheet(writer.dataMap, 0, workbook.getSheetAt(1), 1);
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            System.out.println("Class PE3FileWriter | Method main | Exception desc : " + e.getMessage());
        }
    }

}
