package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.opencsv.exceptions.CsvValidationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cucumber_maven.utils.Filemerger;
import cucumber_maven.utils.Logbook;

import static org.junit.Assert.*;

public class Datavalidationsteps {

    List<Map<String, String>> mergedData;
   // private static ExtentReports extent;
  //  private static ExtentTest test;
    // Initialize ExtentReports and create a report file
   
    @Given("I have merged patient data")
    public void i_have_merged_patient_data() throws CsvValidationException {
        Logbook.logStepStart("Merging patient data");
        List<String> mergedFiles = new ArrayList<>();
        mergedData = Filemerger.mergeFiles(mergedFiles);
     // Log the merged files
     //   test = extent.createTest("Merging patient data");  // Create a test for this scenario
    //    test.info("Merged the following files:");
        System.out.println("Merged the following files:");
        for (String file : mergedFiles) {
            System.out.println(file);  
         //   test.info(file);// This prints the files merged with their extensions
        }

        Logbook.logStepEnd("Merging patient data");
    }
    



    @When("I check if any field is missing or invalid")
    public void i_check_if_any_field_is_missing_or_invalid() {
        Logbook.logStepStart("Validating data fields");

        // Validate the various conditions in each scenario
        for (Map<String, String> data : mergedData) {
            // Validate PatientID
            String patientID = data.get("PatientID");

            // Check if PatientID is missing or empty
            if (patientID == null || patientID.isEmpty()) {
                Logbook.logError("Missing PatientID in record");
                fail("PatientID is missing in record");
            }
            
            // Validate PatientID format (e.g., must start with 'P' followed by 3 digits)
            else if (!patientID.matches("^P\\d{3}$")) {  // Regex for PatientID starting with 'P' followed by exactly 3 digits
                Logbook.logError("Invalid PatientID format: " + patientID);
                fail("Invalid PatientID format: " + patientID);
            }
            
            // If needed, check if PatientID is alphanumeric (optional, as you might have a specific format)
            else if (!patientID.matches("[A-Za-z0-9]+")) {
                Logbook.logError("PatientID contains invalid characters: " + patientID);
                fail("PatientID contains invalid characters");
            }
        


            // Validate PatientName
            String patientName = data.get("PatientName");
            if (patientName == null || patientName.isEmpty()) {
                Logbook.logError("Missing PatientName in record");
                fail("PatientName is missing in record");
            } else if (!patientName.matches("[A-Za-z\\s]+")) { // Only alphabets and spaces allowed
                Logbook.logError("Invalid PatientName format: " + patientName);
                fail("Invalid PatientName format");
            }

            // Validate ClaimID
            String claimID = data.get("ClaimID");
            if (claimID == null || claimID.isEmpty()) {
                Logbook.logError("Missing ClaimID in record");
                fail("ClaimID is missing in record");
            } else if (!claimID.matches("[A-Za-z0-9]+")) { // Alphanumeric ClaimID
                Logbook.logError("Invalid ClaimID format: " + claimID);
                fail("Invalid ClaimID format");
            }

            // Validate ClaimAmount
            String claimAmountStr = data.get("ClaimAmount");
            if (claimAmountStr == null || claimAmountStr.isEmpty()) {
                Logbook.logError("Missing ClaimAmount in record");
                fail("ClaimAmount is missing in record");
            } else {
                try {
                    // Trim any accidental spaces around the value
                    claimAmountStr = claimAmountStr.trim();
                    Double claimAmount = Double.parseDouble(claimAmountStr);
                    if (claimAmount <= 0) {
                        Logbook.logError("ClaimAmount is less than or equal to zero: " + claimAmount);
                        fail("ClaimAmount must be greater than zero");
                    }
                } catch (NumberFormatException e) {
                    Logbook.logError("Invalid ClaimAmount format: " + claimAmountStr);
                    fail("Invalid ClaimAmount format");
                }
            }
        }

        Logbook.logStepEnd("Validating data fields");
    }


    // Scenario: Validate missing patient data
    @Then("I should see no missing fields or invalid claim amounts")
    public void i_should_see_no_missing_fields_or_invalid_claim_amounts() {
        Logbook.log("Data validation passed successfully.");
    }

    // Scenario: Validate PatientID uniqueness
    @Then("I should see no duplicate PatientIDs")
    public void i_should_see_no_duplicate_patient_ids() {
        Set<String> patientIDs = new HashSet<>();
        for (Map<String, String> data : mergedData) {
            String patientID = data.get("PatientID");
            if (patientIDs.contains(patientID)) {
                Logbook.logError("Duplicate PatientID found: " + patientID);
                fail("Duplicate PatientID found: " + patientID);
            }
            patientIDs.add(patientID);
        }
        Logbook.log("PatientID uniqueness validated.");
    }

    // Scenario: Validate PatientName length
    @Then("I should see no PatientName shorter than 2 characters or longer than 50 characters")
    public void i_should_see_no_patientname_shorter_than_2_or_longer_than_50() {
        for (Map<String, String> data : mergedData) {
            String patientName = data.get("PatientName");
            if (patientName.length() < 2 || patientName.length() > 50) {
                Logbook.logError("Invalid PatientName length: " + patientName);
                fail("PatientName must be between 2 and 50 characters");
            }
        }
        Logbook.log("PatientName length validated.");
    }

    // Scenario: Validate PatientName contains no special characters
    @Then("I should see no PatientName containing special characters")
    public void i_should_see_no_patientname_containing_special_characters() {
        for (Map<String, String> data : mergedData) {
            String patientName = data.get("PatientName");
            if (!patientName.matches("[A-Za-z\\s]+")) { // Only alphabets and spaces allowed
                Logbook.logError("PatientName contains special characters: " + patientName);
                fail("PatientName contains special characters");
            }
        }
        Logbook.log("PatientName validation for special characters passed.");
    }

    // Scenario: Validate ClaimAmount greater than zero
    @Then("I should see no ClaimAmount less than or equal to zero")
    public void i_should_see_no_claimamount_less_than_or_equal_to_zero() {
        for (Map<String, String> data : mergedData) {
            String claimAmountStr = data.get("ClaimAmount");
            try {
                Double claimAmount = Double.parseDouble(claimAmountStr);
                if (claimAmount <= 0) {
                    Logbook.logError("ClaimAmount is less than or equal to zero: " + claimAmount);
                    fail("ClaimAmount must be greater than zero");
                }
            } catch (NumberFormatException e) {
                Logbook.logError("Invalid ClaimAmount format: " + claimAmountStr);
                fail("Invalid ClaimAmount format");
            }
        }
        Logbook.log("ClaimAmount greater than zero validated.");
    }

    // Scenario: Validate ClaimAmount not an outlier
    @Then("I should see no ClaimAmount greater than 1,000,000")
    public void i_should_see_no_claimamount_greater_than_one_million() {
        for (Map<String, String> data : mergedData) {
            String claimAmountStr = data.get("ClaimAmount");
            try {
                Double claimAmount = Double.parseDouble(claimAmountStr);
                if (claimAmount > 1000000) {
                    Logbook.logError("ClaimAmount is an outlier: " + claimAmount);
                    fail("ClaimAmount is unreasonably large: " + claimAmount);
                }
            }
        
     catch (NumberFormatException e) {
        Logbook.logError("Invalid ClaimAmount format: " + claimAmountStr);
        fail("Invalid ClaimAmount format");
    
}
Logbook.log("ClaimAmount outlier validation passed.");
}}

// Scenario: Validate ClaimAmount logic for specific ClaimID
@Then("I should see no ClaimAmount below $1000 for ClaimID starting with 'A'")
public void i_should_see_no_claimamount_below_1000_for_claimid_starting_with_A() {
for (Map<String, String> data : mergedData) {
    String claimID = data.get("ClaimID");
    String claimAmountStr = data.get("ClaimAmount");
    try {
        Double claimAmount = Double.parseDouble(claimAmountStr);
        if (claimID.startsWith("A") && claimAmount < 1000) {
            Logbook.logError("ClaimAmount is below $1000 for ClaimID starting with 'A': " + claimAmount);
            fail("ClaimAmount below $1000 for ClaimID starting with 'A'");
        }
    } catch (NumberFormatException e) {
        Logbook.logError("Invalid ClaimAmount format: " + claimAmountStr);
        fail("Invalid ClaimAmount format");
    }
}
Logbook.log("ClaimAmount logic for ClaimID starting with 'A' validated.");
}

// Scenario: Validate ClaimID format
@Then("I should see no ClaimID containing invalid characters")
public void i_should_see_no_claimid_containing_invalid_characters() {
for (Map<String, String> data : mergedData) {
    String claimID = data.get("ClaimID");
    if (!claimID.matches("[A-Za-z0-9]+")) { // Alphanumeric only
        Logbook.logError("Invalid ClaimID format: " + claimID);
        fail("Invalid ClaimID format");
    }
}
Logbook.log("ClaimID format validated.");
}
}
