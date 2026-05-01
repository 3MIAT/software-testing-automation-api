package com.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public final class ExcelReader {
    private static final String DEFAULT_WORKBOOK = "testdata/testdata.xlsx";

    private ExcelReader() {
    }

    public static Object[][] readSheetAsMaps(String sheetName) throws IOException {
        return readSheetAsMaps(DEFAULT_WORKBOOK, sheetName);
    }

    public static Object[][] readSheetAsMaps(String resourcePath, String sheetName) throws IOException {
        try (InputStream input = ExcelReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException(resourcePath + " was not found on the test classpath");
            }
            try (Workbook workbook = WorkbookFactory.create(input)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet not found: " + sheetName);
                }
                return toDataProvider(sheet);
            }
        }
    }

    private static Object[][] toDataProvider(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Header row missing in sheet: " + sheet.getSheetName());
        }

        DataFormatter formatter = new DataFormatter();
        List<String> headers = new ArrayList<>();
        headerRow.forEach(cell -> headers.add(formatter.formatCellValue(cell)));

        List<Map<String, String>> rows = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            Map<String, String> data = new HashMap<>();
            for (int columnIndex = 0; columnIndex < headers.size(); columnIndex++) {
                data.put(headers.get(columnIndex), formatter.formatCellValue(row.getCell(columnIndex)));
            }
            rows.add(data);
        }

        Object[][] result = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            result[i][0] = rows.get(i);
        }
        return result;
    }
}
