package Base;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.lang.reflect.Method;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;

import utilities.TestInfo;
import utilities.TestInfoContext;

public class AmazonData {

	static Map<String, String> dataMap = new HashMap<String, String>();
	private static final String DEFAULT_PATH = "C:\\Users\\AMAR\\eclipse-workspace\\Amazon.Demo\\TestDataExcel"
			+ "\\";
    private static final String DEFAULT_EXTENSION = ".xlsx";
    private static final Map<String, String> filePathCache = new HashMap<>();
    private static final Map<String, Workbook> workbookCache = new HashMap<>();
    private static final Map<String, Map<String, Integer>> headerIndexCache = new HashMap<>();


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
	// Method to construct the full file path
	private static String constructFilePath(String fileName) {
	    return filePathCache.computeIfAbsent(fileName, key -> {
	        if (!key.endsWith(DEFAULT_EXTENSION)) {
	            key += DEFAULT_EXTENSION;
	        }
	        return DEFAULT_PATH + key;
	    });
	}
	
	public static String getInputData(String columnName) {
	    try {
	        // Retrieve the TestInfo from the context
	        TestInfo testInfo = TestInfoContext.getTestInfo();
	        String filePath = constructFilePath(testInfo.ExcelFileName());
	        String sheetName = testInfo.SheetName();
	        String rowKey = testInfo.DataKey();

	        // Fetch data from Excel using the provided parameters
	        return getInputDataFromExcel(filePath, sheetName, rowKey, columnName);
	    } catch (Exception e) {
	        throw new RuntimeException("Error fetching input data: " + e.getMessage(), e);
	    }
	}

 // Get Workbook instance from cache or load
    private static Workbook getWorkbook(String filePath) throws Exception {
        return workbookCache.computeIfAbsent(filePath, path -> {
            try {
                return WorkbookFactory.create(new File(path));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load workbook: " + path, e);
            }
        });
    }

    // Method to get data dynamically
    public static String getInputDataFromExcel(String filePath, String sheetName, String rowKey, String columnName) {
        try {
            Workbook workbook = getWorkbook(filePath);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            // Cache header indices for faster column lookup
            Map<String, Integer> headerIndices = headerIndexCache.computeIfAbsent(sheetName, sheetKey -> {
                Row headerRow = sheet.getRow(0); // Assuming the first row is the header
                if (headerRow == null) {
                    throw new IllegalArgumentException("Header row not found in sheet: " + sheetKey);
                }

                Map<String, Integer> map = new HashMap<>();
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    Cell cell = headerRow.getCell(i);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        map.put(cell.getStringCellValue().toLowerCase(), i);
                    }
                }
                return map;
            });

            Integer columnIndex = headerIndices.get(columnName.toLowerCase());
            if (columnIndex == null) {
                throw new IllegalArgumentException("Column not found: " + columnName);
            }

            // Find the row by rowKey
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) { // Assuming the key is in the first column
                    Cell keyCell = row.getCell(0);
                    if (keyCell.getCellType() == CellType.STRING && keyCell.getStringCellValue().equalsIgnoreCase(rowKey)) {
                        Cell dataCell = row.getCell(columnIndex);
                        return getCellValue(dataCell);
                    }
                }
            }

            throw new IllegalArgumentException("Row not found for key: " + rowKey);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching data";
        }
    }

    // Utility to get cell value
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return "Unsupported cell type";
        }
    }
}

