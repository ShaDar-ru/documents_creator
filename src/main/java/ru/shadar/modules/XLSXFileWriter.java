package ru.shadar.modules;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * Интерфейс для объдинения нескольких записыватель в один, для удобства пользования и расширения.
 * Пока только набросок
 */
public interface XLSXFileWriter {

    void write(String address, List<List<String>> data) throws Exception;

    XSSFWorkbook getTemplate() throws IOException;

    void generateTemplateList() throws Exception;

    XSSFWorkbook appendSheet (XSSFWorkbook workbook, XSSFSheet sheet);

    default XSSFWorkbook getLastListTemplate() throws IOException {
        XSSFWorkbook workbook = null;
        URL resource = XLSXFileWriter.class.getResource("LReg_fixed_.xlsx");
        if (resource == null) {
            throw new FileNotFoundException("LastListTemplate not found!");
        } else {
            workbook = new XSSFWorkbook(resource.getFile());
        }
        return workbook;
    };
}
