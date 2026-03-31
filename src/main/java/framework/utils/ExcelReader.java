package framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    /**
     * Đọc toàn bộ data từ một sheet Excel.
     * Dòng đầu tiên (index 0) là header — bị bỏ qua.
     * Trả về Object[][] để dùng với @DataProvider.
     */
    public static Object[][] getData(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            // Kiểm tra sheet có tồn tại không
            if (sheet == null) {
                throw new RuntimeException("❌ Không tìm thấy sheet: " + sheetName
                        + " trong file: " + filePath);
            }

            int lastRow = sheet.getLastRowNum();       // Dòng cuối cùng có data
            int lastCol = sheet.getRow(0).getLastCellNum(); // Số cột (đọc từ header)

            // lastRow dòng data (bỏ dòng 0 là header)
            Object[][] data = new Object[lastRow][lastCol];

            for (int r = 1; r <= lastRow; r++) { // Bắt đầu từ dòng 1: bỏ header
                Row row = sheet.getRow(r);
                if (row == null) continue; // Bỏ qua dòng trống

                for (int c = 0; c < lastCol; c++) {
                    Cell cell = row.getCell(c);
                    data[r - 1][c] = getCellValue(cell);
                }
            }

            System.out.println("[ExcelReader] ✅ Đọc sheet '" + sheetName
                    + "': " + lastRow + " dòng data");
            return data;

        } catch (IOException e) {
            throw new RuntimeException("❌ Lỗi đọc file Excel: " + filePath, e);
        }
    }

    /**
     * Xử lý từng kiểu dữ liệu trong cell.
     * Cell null → trả về "" thay vì NullPointerException.
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) return ""; // Cell rỗng → chuỗi rỗng

        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA ->
                // Với cell chứa công thức → lấy kết quả đã tính sẵn
                    cell.getCachedFormulaResultType() == CellType.NUMERIC
                            ? String.valueOf((long) cell.getNumericCellValue())
                            : cell.getStringCellValue().trim();
            default -> ""; // BLANK, ERROR, _NONE → chuỗi rỗng
        };
    }
}