package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import com.opencsv.exceptions.CsvValidationException;
import cucumber_maven.utils.Filemerger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Datavalidationsteps {

    private static final Logger logger = LoggerFactory.getLogger(Datavalidationsteps.class);
    List<Map<String, String>> mergedData;

    @Given("I have merged patient data")
    public void i_have_merged_patient_data() throws CsvValidationException {
        logger.info("Merging patient data");
        List<String> mergedFiles = new ArrayList<>();
        mergedData = Filemerger.mergeFiles(mergedFiles);
        logger.info("This is a test log message.");
        
        // Log the merged files
        logger.info("Merged the following files:");
        for (String file : mergedFiles) {
            logger.info(file);
        }

        logger.info("Merging patient data completed");
    }

    @When("I check if any field is missing or invalid")
    public void i_check_if_any_field_is_missing_or_invalid() {
        logger.info("Validating data fields");

        for (Map<String, String> data : mergedData) {
            // Validate PatientID
            String patientID = data.get("PatientID");

            if (patientID == null || patientID.isEmpty()) {
                logger.error("Missing PatientID in record");
                fail("PatientID is missing in record");
            } else if (!patientID.matches("^P\\d{3}$")) {
                logger.error("Invalid PatientID format: " + patientID);
                fail("Invalid PatientID format: " + patientID);
            }

            // Validate PatientName
            String patientName = data.get("PatientName");
            if (patientName == null || patientName.isEmpty()) {
                logger.error("Missing PatientName in record");
                fail("PatientName is missing in record");
            } else if (!patientName.matches("[A-Za-z\\s]+")) {
                logger.error("Invalid PatientName format: " + patientName);
                fail("Invalid PatientName format");
            }

            // Validate ClaimID
            String claimID = data.get("ClaimID");
            if (claimID == null || claimID.isEmpty()) {
                logger.error("Missing ClaimID in record");
                fail("ClaimID is missing in record");
            } else if (!claimID.matches("[A-Za-z0-9]+")) {
                logger.error("Invalid ClaimID format: " + claimID);
                fail("Invalid ClaimID format");
            }

            // Validate ClaimAmount
            String claimAmountStr = data.get("ClaimAmount");
            if (claimAmountStr == null || claimAmountStr.isEmpty()) {
                logger.error("Missing ClaimAmount in record");
                fail("ClaimAmount is missing in record");
            } else {
                try {
                    claimAmountStr = claimAmountStr.trim();
                    Double claimAmount = Double.parseDouble(claimAmountStr);
                    if (claimAmount <= 0) {
                        logger.error("ClaimAmount is less than or equal to zero: " + claimAmount);
                        fail("ClaimAmount must be greater than zero");
                    }
                } catch (NumberFormatException e) {
                    logger.error("Invalid ClaimAmount format: " + claimAmountStr, e);
                    fail("Invalid ClaimAmount format");
                }
            }
        }

        logger.info("Data validation completed");
    }

    @Then("I should see no missing fields or invalid claim amounts")
    public void i_should_see_no_missing_fields_or_invalid_claim_amounts() {
        logger.info("Data validation passed successfully.");
    }

    @Then("I should see no duplicate PatientIDs")
    public void i_should_see_no_duplicate_patient_ids() {
        Set<String> patientIDs = new HashSet<>();
        for (Map<String, String> data : mergedData) {
            String patientID = data.get("PatientID");
            if (patientIDs.contains(patientID)) {
                logger.error("Duplicate PatientID found: " + patientID);
                fail("Duplicate PatientID found: " + patientID);
            }
            patientIDs.add(patientID);
        }
        logger.info("PatientID uniqueness validated.");
    }

    @Then("I should see no PatientName shorter than 2 characters or longer than 50 characters")
    public void i_should_see_no_patientname_shorter_than_2_or_longer_than_50() {
        for (Map<String, String> data : mergedData) {
            String patientName = data.get("PatientName");
            if (patientName.length() < 2 || patientName.length() > 50) {
                logger.error("Invalid PatientName length: " + patientName);
                fail("PatientName must be between 2 and 50 characters");
            }
        }
        logger.info("PatientName length validated.");
    }

    @Then("I should see no PatientName containing special characters")
    public void i_should_see_no_patientname_containing_special_characters() {
        for (Map<String, String> data : mergedData) {
            String patientName = data.get("PatientName");
            if (!patientName.matches("[A-Za-z\\s]+")) {
                logger.error("PatientName contains special characters: " + patientName);
                fail("PatientName contains special characters");
            }
        }
        logger.info("PatientName validation for special characters passed.");
    }

    @Then("I should see no ClaimAmount less than or equal to zero")
    public void i_should_see_no_claimamount_less_than_or_equal_to_zero() {
        for (Map<String, String> data : mergedData) {
            String claimAmountStr = data.get("ClaimAmount");
            try {
                Double claimAmount = Double.parseDouble(claimAmountStr);
                if (claimAmount <= 0) {
                    logger.error("ClaimAmount is less than or equal to zero: " + claimAmount);
                    fail("ClaimAmount must be greater than zero");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid ClaimAmount format: " + claimAmountStr, e);
                fail("Invalid ClaimAmount format");
            }
        }
        logger.info("ClaimAmount greater than zero validated.");
    }

    @Then("I should see no ClaimAmount greater than 1,000,000")
    public void i_should_see_no_claimamount_greater_than_one_million() {
        for (Map<String, String> data : mergedData) {
            String claimAmountStr = data.get("ClaimAmount");
            try {
                Double claimAmount = Double.parseDouble(claimAmountStr);
                if (claimAmount > 1000000) {
                    logger.error("ClaimAmount is an outlier: " + claimAmount);
                    fail("ClaimAmount is unreasonably large: " + claimAmount);
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid ClaimAmount format: " + claimAmountStr, e);
                fail("Invalid ClaimAmount format");
            }
        }
        logger.info("ClaimAmount outlier validation passed.");
    }

    @Then("I should see no ClaimAmount below $1000 for ClaimID starting with 'A'")
    public void i_should_see_no_claimamount_below_1000_for_claimid_starting_with_A() {
        for (Map<String, String> data : mergedData) {
            String claimID = data.get("ClaimID");
            String claimAmountStr = data.get("ClaimAmount");
            try {
                Double claimAmount = Double.parseDouble(claimAmountStr);
                if (claimID.startsWith("A") && claimAmount < 1000) {
                    logger.error("ClaimAmount is below $1000 for ClaimID starting with 'A': " + claimAmount);
                    fail("ClaimAmount below $1000 for ClaimID starting with 'A'");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid ClaimAmount format: " + claimAmountStr, e);
                fail("Invalid ClaimAmount format");
            }
        }
        logger.info("ClaimAmount logic for ClaimID starting with 'A' validated.");
    }

    @Then("I should see no ClaimID containing invalid characters")
    public void i_should_see_no_claimid_containing_invalid_characters() {
        for (Map<String, String> data : mergedData) {
            String claimID = data.get("ClaimID");
            if (!claimID.matches("[A-Za-z0-9]+")) {
                logger.error("Invalid ClaimID format: " + claimID);
                fail("Invalid ClaimID format");
            }
        }
        logger.info("ClaimID format validated.");
    }
}
