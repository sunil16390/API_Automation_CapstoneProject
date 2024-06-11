package Reuseable;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFromXL {
//    public static void main(String[] args) {
//        ReadFromXL xl = new ReadFromXL();
//        int rowsinXl = xl.getExcelRowCount("CardDetails","CreditCards");
//        System.out.println(rowsinXl);
//    }
    public String getCellData(int rowNum, int colNum) {
        String value = null;
        try {
            String XLFilePath = System.getProperty("user.dir")+"/Input/CardDetails.xlsx";
            FileInputStream myxlfile = new FileInputStream(XLFilePath);
            Workbook workbook = new XSSFWorkbook(myxlfile);
            Sheet sheet = workbook.getSheet("CreditCards");
            DataFormatter formatter = new DataFormatter();
            value = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum)).toString();
            // System.out.println("value: " + value);
        }
        catch (Exception e) {
            System.out.println("Exception Message: "+e.getMessage());
            System.out.println("Exception cause: "+e.getCause());
        }
        return value;
    }

public int getExcelRowCount(String XLname, String XlSheetName) {
    int lastRow = 0;
    try {
        String XLFilePath = System.getProperty("user.dir") + "/Input/"+XLname+".xlsx";
        FileInputStream myxlfile = new FileInputStream(XLFilePath);
        Workbook workbook = new XSSFWorkbook(myxlfile);
        Sheet sheet = workbook.getSheet(XlSheetName);
        lastRow = sheet.getLastRowNum();
    }
    catch (Exception e) {
        System.out.println("Exception Message: " + e.getMessage());
        System.out.println("Exception cause: " + e.getCause());
    }
    return lastRow;
}

}
