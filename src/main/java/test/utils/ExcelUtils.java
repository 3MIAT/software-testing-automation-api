package test.utils;

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

public final class ExcelUtils {
    private ExcelUtils() {
    }

    public static Object[][] readSheetAsMaps(String sheetName) throws IOException {
        try (InputStream input = ExcelUtils.class.getClassLoader().getResourceAsStream("testdata/testdata.xlsx")) {
            if (input == null) {
                throw new IllegalStateException("testdata.xlsx not found under src/test/resources/testdata");
            }
            try (Workbook workbook = WorkbookFactory.create(input)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet not found: " + sheetName);
                }

                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    throw new IllegalArgumentException("Header row missing in sheet: " + sheetName);
                }

                DataFormatter formatter = new DataFormatter();
                List<String> headers = new ArrayList<>();
                headerRow.forEach(cell -> headers.add(formatter.formatCellValue(cell)));

                List<Map<String, String>> rows = new ArrayList<>();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    Map<String, String> data = new HashMap<>();
                    for (int c = 0; c < headers.size(); c++) {
                        String value = formatter.formatCellValue(row.getCell(c));
                        data.put(headers.get(c), value);
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
    }
}
