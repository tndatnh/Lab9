package framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    
    /**
     * Đọc dữ liệu từ file Excel và trả về mảng 2 chiều cho TestNG DataProvider
     * @param filePath Đường dẫn file Excel
     * @param sheetName Tên sheet cần đọc
     * @return Object[][] với mỗi row chứa [username, password, expected, description]
     */
    public static Object[][] getData(String filePath, String sheetName) {
        List<Object[]> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.err.println("Sheet '" + sheetName + "' không tồn tại trong file: " + filePath);
                return new Object[0][4]; // Trả về mảng rỗng với 4 cột
            }
            
            // Bắt đầu từ row 1 (bỏ qua header row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                // Đảm bảo mỗi row có ĐÚNG 4 cột
                Object[] rowData = new Object[4];
                
                for (int j = 0; j < 4; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING); // Force đọc là String
                        rowData[j] = cell.getStringCellValue().trim(); // Trim khoảng trắng thừa
                    } else {
                        rowData[j] = ""; // Giá trị mặc định nếu cell trống
                    }
                }
                
                dataList.add(rowData);
            }
            
        } catch (IOException e) {
            System.err.println("Lỗi đọc file Excel: " + filePath);
            e.printStackTrace();
            return new Object[0][4];
        }
        
        // Chuyển List<Object[]> sang Object[][] cho TestNG
        return dataList.toArray(new Object[0][]);
    }
    
    /**
     * Utility: Đếm số row data trong sheet (không tính header)
     */
    public static int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            return sheet != null ? sheet.getLastRowNum() : 0;
            
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
