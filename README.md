# Project-ETL-Cucumber-TestNG

Data Extraction and Loading Pipeline with Cucumber and MySQL
This project demonstrates a robust and reproducible data extraction and loading pipeline using Cucumber for behavior-driven development (BDD) and MySQL as the target database. It showcases best practices for data handling, transformation, and database interaction within a structured, testable framework.
________________________________________
**Project Components**
1.	data_extraction.feature: This Cucumber feature file defines the acceptance criteria for the data pipeline using Gherkin syntax. It outlines a scenario where data is extracted from a CSV file, transformed, and loaded into the database.
2.	DataExtractionStepDefs.java: This Java class contains the step definitions that implement the steps described in the feature file. It handles:
•	Data extraction from the CSV file.
•	Transformation into structured DataRecord objects.
•	Insertion into the MySQL database.
•	Verification of successful data loading.
3.	DataRecord.java: Represents a single record of data, with attributes for id, name, and value. This ensures a structured format for handling transformed data.
4.	TestRunner.java : This file is for executing test where  path of the feature file and step definition file is mentioned.
________________________________________
**Key Features and Benefits**
•	Behavior-Driven Development (BDD): The use of Cucumber ensures clear understanding of desired behavior through well-defined acceptance criteria.
•	Structured Data Transformation: Converts raw CSV data into structured DataRecord objects for efficient processing.
•	Efficient Database Interaction: Demonstrates best practices for MySQL connections, query execution, and result handling.
•	Testability and Maintainability: Modular design ensures easy testing and updates.
________________________________________
**Technical Implementation**
•	Data Extraction: Reads data from a CSV file, parsing each row for processing.
•	Data Transformation: Converts extracted data into DataRecord objects.
•	Database Loading: Inserts transformed data into a MySQL data_table using prepared statements.
•	Verification: Validates successful data loading by comparing expected and actual database record counts.
________________________________________
**Getting Started**
1. Prerequisites
•	Java Development Kit (JDK) (Version 8 or above).
•	Maven Build Tool.
•	TestNG Installed from Eclipse marketplace
•	MySQL Server installed and running.
•	Required dependencies for the project:
•	Cucumber for BDD.
•	JUnit for test execution.
•	MySQL Connector/J for database interaction.
2. Maven Project Setup
Create and set up a Maven project with the following steps:
1.	Generate the Maven Project:
•	Use an IDE (e.g., IntelliJ IDEA, Eclipse) or the terminal:
•	Create a Maven Project <cucumber-maven>
•	Navigate to the project directory.
•	Add package <featuretest>  at  src/test/java level.
•	Add package <runnerFiles> at  src/test/java level
•	Add package <stepDefinitions> at src/test/java level

2.	Add Dependencies to pom.xml: Add the required dependencies for Cucumber, JUnit, MySQL

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
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <scope>test</scope>
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
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit</artifactId>
        <version>7.10.0</version> <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version> <scope>test</scope>
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



3.	Create Feature  file <DataExtraction.feature> under src\test\java\cucumber_maven\featurestest\

4.	Create StepDefinitions file <DataExtractionStepDefs> under src\test\java\stepDefinitions


5.	Create DataRecord file <DataRecord> under src\test\java\stepDefinitions

6.	Create a TestRunner file under src\test\java\runnerFiles

		1.Mention path of feature file where it is placed under @CucumberOptions
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
  
8.  **Database Setup**
     Create the Database: Log in to MySQL and create the required database <etldb> and table:

       CREATE DATABASE etldb; USE etldb; CREATE TABLE data_table ( id INT PRIMARY KEY, name VARCHAR(255), value DOUBLE ); 


9.	**Update Connection Details**: In DataExtractionStepDefs.java, update the connection details:
    
      	String url = "jdbc:mysql://localhost:3306/etldb"; String username = "root"; String password = "root"; 


11.  **Running the Tests**
        Run the Cucumber tests using your IDE or the Maven command or using testNG :
                  Mvn test


## Snapshot of Cucumber Report


![CucumberReport](https://github.com/user-attachments/assets/1cb5a0e7-7813-41a6-82f9-a99bd3750629)
