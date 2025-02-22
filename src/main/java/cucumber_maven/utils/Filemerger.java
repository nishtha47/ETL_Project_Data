package cucumber_maven.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class Filemerger {

	public static List<Map<String, String>> mergeFiles(List<String> mergedFiles) throws CsvValidationException {
	    List<Map<String, String>> mergedData = new ArrayList<>();

	    // 1. Read CSV file
	    String csvFile = "src/main/java/resources/patient.csv";
	    if (new File(csvFile).exists()) {
	        mergedFiles.add(csvFile);  // Add the file to the list
	        mergedData.addAll(readCSVFile(csvFile, mergedFiles));
	    }

	    // 2. Read Excel file
	    String excelFile = "src/main/java/resources/patient.xls";
	    if (new File(excelFile).exists()) {
	        mergedFiles.add(excelFile);  // Add the file to the list
	      //  mergedData.addAll(readExcelFile(excelFile));
	        mergedData.addAll(readExcelFile(excelFile, mergedFiles)); 
	    }

	    // 3. Read Text file
	    String textFile = "src/main/java/resources/patient.txt";
	    if (new File(textFile).exists()) {
	        mergedFiles.add(textFile);  // Add the file to the list
	        mergedData.addAll(readTextFile(textFile, mergedFiles));
	    }

	    // 4. Read JSON file
	    String jsonFile = "src/main/java/resources/patient.json";
	    if (new File(jsonFile).exists()) {
	        mergedFiles.add(jsonFile);  // Add the file to the list
	        mergedData.addAll(readJSONFile(jsonFile, mergedFiles));  
	    }

	    return mergedData;
	}

	private static List<Map<String, String>> readCSVFile(String fileName, List<String> mergedFiles) throws CsvValidationException {
	    List<Map<String, String>> data = new ArrayList<>();
	    try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
	        System.out.println("Processing CSV file: " + fileName);
	        mergedFiles.add(fileName); // Log the file being processed

	        String[] line;
	        boolean isFirstLine = true;

	        while ((line = reader.readNext()) != null) {
	            if (isFirstLine) {
	                isFirstLine = false;
	                continue;
	            }

	            // Remove BOM if present
	            if (line[0].startsWith("\uFEFF")) {
	                line[0] = line[0].substring(1);
	            }

	            String patientID = line[0];
	            if (patientID == null || patientID.isEmpty()) {
	                System.err.println("Error: Missing PatientID in CSV file");
	                continue;
	            } else if ("PatientID".equals(patientID)) {
	                continue;
	            } else if (!patientID.matches("[A-Za-z0-9]+")) {
	                System.err.println("Error: Invalid PatientID format in CSV file: " + patientID);
	                continue;
	            }

	            Map<String, String> patientData = new HashMap<>();
	            patientData.put("PatientID", patientID);
	            patientData.put("PatientName", line[1]);
	            patientData.put("ClaimID", line[2]);
	            patientData.put("ClaimAmount", line[3]);

	            data.add(patientData);
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading CSV file: " + e.getMessage());
	    }
	    return data;
	}
	// Helper method to get cell value as a String (handling both NUMERIC and STRING cells)
	private static String getCellValueAsString(Cell cell) {
	    if (cell == null) {
	        return "";  // Return empty string if cell is null
	    }
	    
	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue();
	        case NUMERIC:
	            // Handle numeric cells by converting to string
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return cell.getDateCellValue().toString(); // If it's a date, convert to string
	            } else {
	                return String.valueOf(cell.getNumericCellValue()); // Convert number to string
	            }
	        case BOOLEAN:
	            return String.valueOf(cell.getBooleanCellValue());
	        case FORMULA:
	            return cell.getCellFormula();  // If the cell contains a formula, return the formula
	        default:
	            return "";  // Default return if the cell type is not recognized
	    }
	}


	private static List<Map<String, String>> readExcelFile(String fileName, List<String> mergedFiles) {
	    List<Map<String, String>> data = new ArrayList<>();
	    try (FileInputStream fis = new FileInputStream(fileName);
	         Workbook workbook = new XSSFWorkbook(fis)) {

	        System.out.println("Processing Excel file: " + fileName);
	        mergedFiles.add(fileName); // Log the file being processed

	        Sheet sheet = workbook.getSheetAt(0);

	        for (Row row : sheet) {
	            String patientID = getCellValueAsString(row.getCell(0));
	            if (patientID == null || patientID.isEmpty()) {
	                System.err.println("Error: Missing PatientID in Excel file");
	                continue;
	            } else if ("PatientID".equals(patientID)) {
	                continue;
	            } else if (!patientID.matches("[A-Za-z0-9]+")) {
	                System.err.println("Error: Invalid PatientID format in Excel file: " + patientID);
	                continue;
	            }

	            Map<String, String> patientData = new HashMap<>();
	            patientData.put("PatientID", patientID);
	            patientData.put("PatientName", getCellValueAsString(row.getCell(1)));
	            patientData.put("ClaimID", getCellValueAsString(row.getCell(2)));
	            patientData.put("ClaimAmount", getCellValueAsString(row.getCell(3)));

	            data.add(patientData);
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading Excel file: " + e.getMessage());
	    }
	    return data;
	}

	private static List<Map<String, String>> readTextFile(String fileName, List<String> mergedFiles) {
	    List<Map<String, String>> data = new ArrayList<>();
	    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	        System.out.println("Processing Text file: " + fileName);
	        mergedFiles.add(fileName); // Log the file being processed

	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] fields = line.split("\t");
	            if (fields.length >= 4) {
	                String patientID = fields[0];
	                if (patientID == null || patientID.isEmpty()) {
	                    System.err.println("Error: Missing PatientID in Text file");
	                    continue;
	                } else if ("PatientID".equals(patientID)) {
	                    continue;
	                } else if (!patientID.matches("[A-Za-z0-9]+")) {
	                    System.err.println("Error: Invalid PatientID format in Text file: " + patientID);
	                    continue;
	                }

	                Map<String, String> patientData = new HashMap<>();
	                patientData.put("PatientID", patientID);
	                patientData.put("PatientName", fields[1]);
	                patientData.put("ClaimID", fields[2]);
	                patientData.put("ClaimAmount", fields[3]);

	                data.add(patientData);
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading Text file: " + e.getMessage());
	    }
	    return data;
	}

    // Method to read JSON file
	private static List<Map<String, String>> readJSONFile(String fileName, List<String> mergedFiles) {
	    List<Map<String, String>> data = new ArrayList<>();
	    ObjectMapper mapper = new ObjectMapper();

	    try {
	        System.out.println("Processing JSON file: " + fileName);
	        mergedFiles.add(fileName); // Log the file being processed

	        JsonNode rootNode = mapper.readTree(new File(fileName));
	        for (JsonNode node : rootNode) {
	            String patientID = node.get("PatientID").asText();
	            if (patientID == null || patientID.isEmpty()) {
	                System.err.println("Error: Missing PatientID in JSON file");
	                continue;
	            } else if ("PatientID".equals(patientID)) {
	                continue;
	            } else if (!patientID.matches("[A-Za-z0-9]+")) {
	                System.err.println("Error: Invalid PatientID format in JSON file: " + patientID);
	                continue;
	            }

	            Map<String, String> patientData = new HashMap<>();
	            patientData.put("PatientID", patientID);
	            patientData.put("PatientName", node.get("PatientName").asText());
	            patientData.put("ClaimID", node.get("ClaimID").asText());
	            patientData.put("ClaimAmount", node.get("ClaimAmount").asText());

	            data.add(patientData);
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading JSON file: " + e.getMessage());
	    }

	    return data;
	}


    // Method to write merged data to a new file
    public static void writeMergedFile(List<Map<String, String>> mergedData,List<String> mergedFiles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("merged_data.txt"))) {
        	System.out.println("Merged the following files:");
            for (String file : mergedFiles) {
                System.out.println(file);
            }

            for (Map<String, String> data : mergedData) {
                writer.write(data.get("PatientID") + "\t" +
                        data.get("PatientName") + "\t" +
                        data.get("ClaimID") + "\t" +
                        data.get("ClaimAmount") + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing merged file: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws CsvValidationException {
    	List<String> mergedFiles = new ArrayList<>();
        List<Map<String, String>> mergedData = mergeFiles(mergedFiles);
        writeMergedFile(mergedData,mergedFiles);
        System.out.println("Data merge complete. Output written to merged_data.txt.");
    }
}
