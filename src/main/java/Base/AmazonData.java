package Base;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;

public class AmazonData {

	static Map<String, String> dataMap = new HashMap<String, String>();

	// Constructor
	public AmazonData() {
		dataMap = new HashMap<>();
	}

	// Method to set data
	public static void setInputData(String key, String value) {
		dataMap.put(key, value);
	}

	// Method to get data by key
	public static String getOutData(String key) {
		return dataMap.getOrDefault(key, key + " :Key not found");
	}

	// Method to get Data from Excel by column name and row key
	public static String getInputData(String rowKey, String key) {
	    String filePath = "C:\\Users\\AMAR\\eclipse-workspace\\Amazon.Demo\\TestDataExcel\\AmazonUserData.xlsx";
	    String sheetName = "User";
	    return getInputDataFromExcel(filePath, sheetName, rowKey, key);
	}

	// Method to get data from Excel by column name and row key
	public static String getInputDataFromExcel(String filePath, String sheetName, String rowKey, String columnName) {
	    try (Workbook workbook = WorkbookFactory.create(new File(filePath))) {
	        Sheet sheet = workbook.getSheet(sheetName);
	        if (sheet == null) {
	            throw new IllegalArgumentException("Sheet not found: " + sheetName);
	        }

	        Row headerRow = sheet.getRow(0); // Assuming the first row is the header
	        if (headerRow == null) {
	            throw new IllegalArgumentException("Header row not found in sheet: " + sheetName);
	        }

	        int columnIndex = -1;

	        // Find the column index for the given column name
	        for (int cellIndex = 0; cellIndex < headerRow.getLastCellNum(); cellIndex++) {
	            if (headerRow.getCell(cellIndex).getStringCellValue().equalsIgnoreCase(columnName)) {
	                columnIndex = cellIndex;
	                break;
	            }
	        }

	        if (columnIndex == -1) {
	            throw new IllegalArgumentException("Column not found: " + columnName);
	        }

	        // Locate the row based on the rowKey
	        int rowIndex = -1;
	        for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
	            Row currentRow = sheet.getRow(rowNumber);
	            if (currentRow != null && currentRow.getCell(0) != null) { // Assuming the key is in the first column
	                Cell cell = currentRow.getCell(0);
	                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(rowKey)) {
	                    rowIndex = rowNumber;
	                    break;
	                }
	            }
	        }

	        if (rowIndex == -1) {
	            throw new IllegalArgumentException("Row not found for key: " + rowKey);
	        }

	        // Get the cell value from the specified row and column
	        Row dataRow = sheet.getRow(rowIndex);
	        Cell cell = dataRow.getCell(columnIndex);
	        if (cell == null) {
	            return "";
	        }

	        switch (cell.getCellType()) {
	            case STRING:
	            	System.out.println(cell.getStringCellValue());
	                return cell.getStringCellValue();
	            case NUMERIC:
	            	System.out.println(cell.getStringCellValue());
	            	System.out.println(String.valueOf(cell.getNumericCellValue()));
	                return String.valueOf(cell.getNumericCellValue());
	            case BLANK:
	                return "";
	            default:
	                return "Unsupported cell type";
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error reading data from Excel";
	    }
	}

}
