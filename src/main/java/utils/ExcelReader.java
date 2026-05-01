package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static Object[][] readExcelData(String filePath, String sheetName) {
        List<Object[]> dataList = new ArrayList<>();
        FileInputStream file = null;
        Workbook workbook = null;
        try {
            file = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' does not exist in " + filePath);
            }

            int rowCount = sheet.getLastRowNum();
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                int colCount = row.getLastCellNum();
                Object[] rowData = new Object[colCount];

                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        rowData[j] = "";
                    } else {
                        switch (cell.getCellType()) {
                            case STRING:
                                rowData[j] = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                rowData[j] = String.valueOf(cell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                rowData[j] = String.valueOf(cell.getBooleanCellValue());
                                break;
                            default:
                                rowData[j] = "";
                        }
                    }
                }
                dataList.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try { workbook.close(); } catch (IOException e) {}
            }
            if (file != null) {
                try { file.close(); } catch (IOException e) {}
            }
        }

        Object[][] dataArray = new Object[dataList.size()][];
        return dataList.toArray(dataArray);
    }
}
