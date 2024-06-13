package Reuseable;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFromXL {
//    public static void main(String[] args) {
//        ReadFromXL xl = new ReadFromXL();
//        int rowsinXl = xl.getExcelRowCount("CardDetails","CreditCards");
//        System.out.println(rowsinXl);
//    }
public String read_And_Print_XL_AsPerTestData(String testcasename , String columnName){

    String data = null;
    try{
        String XLFilePath = System.getProperty("user.dir")+"/Input/CardDetails.xlsx";
        FileInputStream myxlfile = new FileInputStream(XLFilePath);
        Workbook workbook = new XSSFWorkbook(myxlfile);
        Sheet sheet = workbook.getSheet("CreditCards");
        int lastRow = sheet.getLastRowNum();
        // System.out.println("The last row which has data =="+lastRow);

        // Loop for Row Iteration...
        for(int i=0;i<=lastRow;i++){
            Row row = sheet.getRow(i);
            // Get the last Column which has data
            int lastCell = row.getLastCellNum();
            Cell cell = row.getCell(0);
            String runtimeTestCaseName = cell.getStringCellValue();
            //   System.out.println("The First Column Value is ==>"+runtimeTestCaseName);
            if(runtimeTestCaseName.equals(testcasename)) {
                // Loop for Column Iteration ...
                Row RowNew = sheet.getRow(0);
                for(int j=0;j<lastCell;j++){
                    Cell cellnew = RowNew.getCell(j);
                    String RunTimeCallValue = cellnew.getStringCellValue();
                    if(RunTimeCallValue.equals(columnName)) {
                        data = sheet.getRow(i).getCell(j).toString();
                        // System.out.println("The XL value is ==>" + data);
                    }
                }
            }
        }
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return data;
}
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
