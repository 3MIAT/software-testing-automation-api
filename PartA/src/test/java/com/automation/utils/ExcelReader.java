package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    private Workbook workbook;
    private Sheet sheet;
    private static final String EXCEL_PATH =
            "src/test/resources/testdata.xlsx";

    public ExcelReader(String sheetName) {
        try {
            FileInputStream fis = new FileInputStream(EXCEL_PATH);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("❌ Sheet '" + sheetName + "' not found in testdata.xlsx");
            }
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("❌ testdata.xlsx not found at: " + EXCEL_PATH, e);
        }
    }

    /**
     * Returns the total number of data rows (excludes header row)
     */
    public int getRowCount() {
        return sheet.getLastRowNum(); // row 0 is header
    }

    /**
     * Returns cell value as String — handles String, Numeric, Boolean cells
     */
    public String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) return "";

        Cell cell = row.getCell(colNum);
        if (cell == null) return "";

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    /**
     * Returns cell value by column header name (row 0 is header)
     */
    public String getCellDataByHeader(int rowNum, String headerName) {
        Row headerRow = sheet.getRow(0);
        int colIndex = -1;

        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().trim().equalsIgnoreCase(headerName)) {
                colIndex = cell.getColumnIndex();
                break;
            }
        }

        if (colIndex == -1) {
            throw new RuntimeException("❌ Column header '" + headerName + "' not found in sheet.");
        }

        return getCellData(rowNum, colIndex);
    }

    public void close() {
        try {
            if (workbook != null) workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
