# Data Validation in Cucumber Framework


This repository contains automated data validation scenarios implemented using the Cucumber framework. The tests ensure data accuracy, consistency, and integrity across various use cases. The framework is built using Java, Selenium, Cucumber, and TestNG/JUnit, along with data handling utilities for robust validation.

Feature File 1: Data Validation Scenarios after Merging 3 Files into One 

Feature File 2: Validating the API Response data using cucumber framework

________________________________________

**Project Components**
1.	MergeClaims.feature: The MergeClaims.feature file is part of a Cucumber-based data validation framework. It validates merged patient claim data by ensuring correctness, completeness, and adherence to business rules.

    **Feature: Data Validation**
The feature ensures merged patient data is clean, accurate, and meets specific validation criteria

**Scenario Breakdown**

 *Validate Missing Patient Data*
 
   Purpose: Ensures that no mandatory fields are missing and that claim amounts are valid.
   
     🔹Steps:

		1.Load merged patient data
		2.Check if any field is missing or invalid
		3.Verify that all required fields exist and claim amounts are valid
	📌 Expectation: No missing fields, and all claim amounts must be valid.
 
   *Validate PatientID Uniqueness*
   
Purpose: Ensures that every patient has a unique PatientID.
 
	🔹 Steps:

		Extract PatientID values from merged data
		Check for duplicate entries
	📌 Expectation: No duplicate PatientIDs should exist.
 
   *Validate PatientName Length*
   
 Purpose: Ensures that patient names have a reasonable length (not too short or too long).
 
		🔹 Steps:

			Extract PatientName
			Check if it is shorter than 2 characters or longer than 50 characters
	📌 Expectation: Patient names must be between 2 and 50 characters.
 
*Validate PatientName Contains No Special Characters*

 Purpose: Ensures PatientName only contains alphabetic characters (no special symbols like @, $, %, &, etc.).
 
	🔹 Steps:
	
		Extract PatientName
		Apply regex validation to check for special characters
	📌 Expectation: No patient name should contain special characters.
 
*Validate ClaimAmount Greater Than Zero*

 Purpose: Ensures that ClaimAmount is always greater than zero.
 
		🔹 Steps:

			Retrieve ClaimAmount from data
			Check if ClaimAmount <= 0
	📌 Expectation: All claim amounts should be greater than zero.
 
*Validate ClaimAmount Not an Outlier*

Purpose: Prevents unrealistically high claim amounts.

		🔹 Steps:

			Retrieve ClaimAmount
			Check if ClaimAmount > 1,000,000
	📌 Expectation: No claim amount should exceed 1,000,000.
 
*Validate ClaimAmount Logic for Specific ClaimID*

 Purpose: Ensures that ClaimAmount rules are followed for specific ClaimID values.
 
		🔹 Steps:

			Retrieve ClaimAmount
			If ClaimID starts with "A", ensure ClaimAmount >= $1000
	📌 Expectation: If a ClaimID starts with "A", the corresponding ClaimAmount must be at least $1000.

 *Validate ClaimID Format*
 
 Purpose: Ensures that ClaimIDs have a valid format.
 
		🔹 Steps:

			Extract ClaimID
			Check for invalid characters using regex validation
	📌 Expectation: No ClaimID should contain invalid characters.

 **How the Feature Works in Cucumber**

 	The Step Definitions implement validation logic.
	Data can be fetched from a database, an API, or a CSV/JSON file.
	Assertions ensure compliance with rules.
	Failures are logged for debugging.
________________________________________

2.	ApiResponse.feature: The ApiResponse.feature file is part of a Cucumber-based API testing framework that validates the response of a web service retrieving book details by ISBN. It ensures that API responses are accurate, structured correctly, and follow expected  business logic.

   **Feature: Get Book by ISBN**
   
	This feature file contains multiple test cases that validate API responses when fetching book details using ISBN.

 
✅ Scenario 1: User calls web service to get a book by its ISBN

🔹 Purpose: Ensures API returns a valid book record for a correct ISBN.

🔹 Steps:

	1.A book exists with ISBN: "9781451648546".
	2.User retrieves the book via API.
	3.Validate status code is 200 (successful response).
	4.Ensure totalItems = 1 and kind = "books#volumes".
	5.Validate book details (title, publisher, page count).
 
📌 Expectation: API returns correct details in response.

✅ Scenario 2: Validate the Publisher Date Field

🔹 Purpose: Checks if the published date is correctly returned.

🔹 Steps:

	1.Retrieve book details by ISBN.
	2.Validate publishedDate = 2011.
 
📌 Expectation: publishedDate should be 2011.

✅ Scenario 3: Validate Language Field in Response

🔹 Purpose: Ensures the language field in response is correct.

🔹 Steps:

	1.Retrieve book details using ISBN: "9780451495081".
	2.Validate totalItems = 1 and kind = "books#volumes".
	3.Ensure title = "Steve Jobs" and language = "en".
 
📌 Expectation: language field should be "en" (English).

✅ Scenario 4: Validate ISBN Field in Response

🔹 Purpose: Ensures the correct ISBN is returned in the API response.

🔹 Steps:

	1.Retrieve book details for ISBN: "9781451648546".
	2.Validate response includes ISBN = 9781451648546.
 
📌 Expectation: API must return correct ISBN in response.

❌ Scenario 5: Validate Response for an Invalid ISBN (Negative Test)

🔹 Purpose: Ensures API correctly handles invalid ISBN requests.
🔹 Steps:

	1.Use an invalid ISBN ("0000000000000").
	2.Retrieve book details via API.
	3.Validate status code is 400 (Bad Request).
	4.Ensure response contains error message = "Invalid ISBN".
 
📌 Expectation: API should return a 400 error with a message "Invalid ISBN" for incorrect ISBNs.


3.	Datavalidationsteps.java: The Datavalidationsteps.java file defines step definitions for validating patient data after merging files. It ensures that the merged data adheres to specific business rules, such as uniqueness of PatientID, proper ClaimAmount values, and valid ClaimID formats.

    **Key Responsibilities of Datavalidationsteps.java**
  	
	1.Merge Patient Data using Filemerger.mergeFiles().
  	
	2.Check for missing or invalid fields (e.g., PatientID, ClaimAmount, PatientName).
  	
	3.Enforce data validation rules, including:
  	
		-Uniqueness of PatientID
		-No special characters in PatientName
		-ClaimAmount greater than zero and within a reasonable range
	   	-Correct format of ClaimID

	**Package Imports**
  	
		**Key Libraries Used:**
  	
			1.Cucumber Annotations (@Given, @When, @Then) – Define step mappings.
			2.JUnit Assertions (Assert.fail()) – Enforce test failures when validation fails.
			3.Logging (SLF4J Logger) – Tracks execution flow and errors.
			4.File Processing (Filemerger.mergeFiles()) – Handles merging CSV files.
			5.Collections (List, Set, Map) – Store and process patient data.

5. BookStepDefinitions.java : This class implements Cucumber step definitions for testing a book retrieval API using RestAssured. It follows BDD (Behavior-Driven Development) principles to validate API responses.

   Key Components:
   
	1. Class-Level Variables
    
			request: Stores the RestAssured request specification.
			response: Stores the API response.
			apiEndpoint: The base API URL, loaded from a properties file.
			isbn: Stores the ISBN of the book being queried.

	3. Initialization & Configuration
    
		The constructor BookStepDefinitions() loads API properties from config.properties.
		loadProperties(): Reads the API base URL from the properties file.

        3. Step Definitions
           
				Given Steps
				@Given("a book exists with an isbn of {string}")
					Sets up a request for a valid book with the given ISBN.
				@Given("no book exists with an isbn of {string}")
					Sets up a request for an invalid ISBN to test negative scenarios.
				When Steps
				@When("a user retrieves the book by isbn")
					Sends a GET request to fetch book details using ISBN.
				Then Steps
				@Then("the status code is {int}")
					Verifies the response status code.
				@Then("response includes the following")
					Asserts that the response contains expected key-value pairs.
				@Then("response includes the following in any order")
					Similar to the previous step but allows any order for values.
					Additional Helper Methods

	4.normalizePublisherDateValues(List<String> dateValues): Extracts only the year from a full date (e.g., "2022-05-10" → "2022"). 

  

4. Filemerger.java: The Filemerger class is responsible for reading and merging data from multiple file formats (CSV, Excel, Text, JSON) and writing the merged data to a new output file (merged_data.txt).

    **Key Features**
   
	Reads data from different file types:

		CSV (.csv) → Uses OpenCSV to read tabular data.
		Excel (.xls, .xlsx) → Uses Apache POI to extract data from spreadsheets.
		Text (.txt) → Reads tab-separated values using a BufferedReader.
		JSON (.json) → Uses Jackson ObjectMapper to parse JSON.

	Validates and Processes Data:

		Ensures PatientID exists and is in a valid format (A-Za-z0-9).
		Skips header rows and handles BOM characters (Byte Order Mark).
		Converts different cell types (string, numeric, boolean, formula) in Excel to strings.

	Merges Data from All Files:

		Extracts PatientID, PatientName, ClaimID, and ClaimAmount.
		Stores data in a List of Maps (List<Map<String, String>>).
	Writes Merged Data to a File (merged_data.txt):

		Saves formatted output as tab-separated values.


 **Workflow of Filemerger**
 
 1.mergeFiles(List<String> mergedFiles):

	Checks for existing files (patient.csv, patient.xls, patient.txt, patient.json).
	Calls corresponding methods (readCSVFile, readExcelFile, etc.).
	Collects data in a list (mergedData).
 
2. File-Specific Methods:

		readCSVFile() → Reads data using OpenCSV.
		readExcelFile() → Extracts data from Excel using Apache POI.
		readTextFile() → Reads tab-separated text files using BufferedReader.
		readJSONFile() → Parses JSON data using Jackson.

4. writeMergedFile()

	Logs all processed files.
	Writes merged data to merged_data.txt.

4.main() Method

	Initiates file reading, merging, and writing.
	Prints confirmation message after completion.

5. Logbook.java: The Logbook class is a utility for logging messages, step start/end events, and errors using SLF4J (Simple Logging Facade for Java) to standardize logging in a test automation framework.
   
7. Claim.java: The Claim class is an immutable model representing a patient's medical claim, ensuring data integrity with validations and supporting XML serialization via @XmlRootElement and @XmlElement annotations.

8. config.properties : The config.properties file stores the API endpoint URL for fetching book details using ISBN from the Google Books API.  

9. TestRunner.java : This file is for executing test where  path of the feature file and step definition file is mentioned.

________________________________________

**Getting Started**

Prerequisites

Before running the tests, ensure you have the following installed:

	Java 11+ (Ensure JAVA_HOME is set)
	Maven (For dependency management)
	Cucumber (Integrated with JUnit/TestNG)
	Selenium WebDriver (For UI-based data validation)
	Test Data Source (Excel, CSV, JSON, or API endpoints)

2. Maven Project Setup
   
	Create and set up a Maven project with the following steps:

   		 Generate the Maven Project:
			Use an IDE (e.g., IntelliJ IDEA, Eclipse) or the terminal:
			Create a Maven Project <cucumber-maven>
			Navigate to the project directory.
			Add package <featuretest>  at  src/test/java level.
			Add package <runnerFiles> at  src/test/java level
			Add package <stepDefinitions> at src/test/java level

2.	Add Dependencies to pom.xml: Add the required dependencies for Cucumber, extentreport, Restassured and log4j

   ________________________________________

**Pom.xml**

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>cucumber-maven</groupId>
  <artifactId>cucumber-maven</artifactId>
  <version>0.0.1-SNAPSHOT</version>

  <name>cucumber-maven</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.release>17</maven.compiler.release>
  </properties>
<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.junit</groupId>
        <artifactId>junit-bom</artifactId>
        <version>5.11.0</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
<dependencies>
  <!-- Log4j API -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.20.0</version> <!-- Use the latest version -->
    </dependency>

    <!-- Log4j Core (Required for Log4j2 functionality) -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.20.0</version> <!-- Use the latest version -->
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.32</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.11</version>
</dependency>
<!-- Logback Core -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>1.2.11</version>
    </dependency>
    <!-- Optionally: parameterized tests support -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-params</artifactId>
      <scope>test</scope>
    </dependency>
     <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.28</version> </dependency>
         <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.10.0</version>  <scope>test</scope>
    </dependency>
     <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.3.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit</artifactId>
        <version>7.10.0</version> <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version> <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
        <version>2.3.1</version>
    </dependency>

    <!-- JAXB Runtime (needed for implementation) -->
    <dependency>
        <groupId>org.glassfish.jaxb</groupId>
        <artifactId>jaxb-runtime</artifactId>
        <version>2.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.3</version> <!-- Use the latest version available -->
    </dependency>
 <dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>4.1.0</version>
</dependency>



    <!-- Apache POI - For working with older Excel formats (XLS) -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>5.2.3</version> <!-- Use the latest version available -->
    </dependency>

    <!-- OpenCSV - For working with CSV files -->
    <dependency>
        <groupId>com.opencsv</groupId>
        <artifactId>opencsv</artifactId>
        <version>5.7.1</version> <!-- Use the latest version available -->
    </dependency>

     <!-- TestNG Dependency -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.0.0</version> <!-- Use the latest stable version -->
        <scope>test</scope>
    </dependency>
    <dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-testng</artifactId>
    <version>7.0.0</version> <!-- Replace with the latest version -->
</dependency>


    <!-- Cucumber Reporting Plugin (Optional) -->
    <dependency>
        <groupId>net.masterthought</groupId>
        <artifactId>cucumber-reporting</artifactId>
        <version>5.8.2</version>
    </dependency>
    </dependencies>

  <build>
   <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>logback.xml</include>
            </includes>
        </resource>
    </resources>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.4.0</version>
        </plugin>
        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
        <plugin>
          <artifactId>maven-resources-plugin</artifactId>
          <version>3.3.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.13.0</version>
        </plugin>
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>3.3.0</version>
          <configuration>
        <redirectTestOutputToFile>false</redirectTestOutputToFile>
    </configuration>

        </plugin>
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.4.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-install-plugin</artifactId>
          <version>3.1.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>3.1.2</version>
        </plugin>
        <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
        <plugin>
          <artifactId>maven-site-plugin</artifactId>
          <version>3.12.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>3.6.1</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>

________________________________________

3.	Create Feature  file under src\test\java\cucumber_maven\featurestest\

4.	Create StepDefinitions file  under src\test\java\stepDefinitions

5.	Create model file <DataRecord> under src\test\java\models

6.	create utils file under src\main\java\cucumber_maven_utils

7.	create properties and test data files under src\main\java\resources

8.	Create a TestRunner file under src\test\java\runnerFiles

   		1. Mention path of feature file where it is placed under @CucumberOptions
		2.In glue mention stepDefinitions file
		3.Add plugin for cucumber reporting

		package runnerFiles;

		import io.cucumber.testng.AbstractTestNGCucumberTests;

		import io.cucumber.testng.CucumberOptions;


		@CucumberOptions (features = {"src/test/java/cucumber_maven/featurestest"},
        		glue = {"stepDefinitions"},
      		 // tags = "@tag1",
        		plugin = {"pretty", "html: target/cucumber-reports.html"},
       		monochrome = true
		)

		public class TestRunner extends AbstractTestNGCucumberTests {
	


		}


7.	**Build the Project**: Run the following command to download dependencies and build the project:
   
      				mvn clean install 
  
9.  **Input Files Setup**

     Create patient.csv,patient.json,patient.xls and patient.txt files with column details

    - patient id
    - patient name
    - claim id
    - claim amount
  

11.  **Running the Tests**
        Run the Cucumber tests using your IDE or the Maven command or using testNG :
                  Mvn test


## Snapshot of Cucumber Report

![image](https://github.com/user-attachments/assets/a35dc4ed-bf83-4a8e-b52c-7341e0bf1971)

