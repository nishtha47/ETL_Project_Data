#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
Feature: Data Validation

  Scenario: Validate missing patient data
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no missing fields or invalid claim amounts
    
 Scenario: Validate PatientID uniqueness
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no duplicate PatientIDs

  Scenario: Validate PatientName length
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no PatientName shorter than 2 characters or longer than 50 characters

  Scenario: Validate PatientName contains no special characters
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no PatientName containing special characters

  Scenario: Validate ClaimAmount greater than zero
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no ClaimAmount less than or equal to zero

  Scenario: Validate ClaimAmount not an outlier
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no ClaimAmount greater than 1,000,000

  Scenario: Validate ClaimAmount logic for specific ClaimID
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no ClaimAmount below $1000 for ClaimID starting with 'A'

  Scenario: Validate ClaimID format
    Given I have merged patient data
    When I check if any field is missing or invalid
    Then I should see no ClaimID containing invalid characters   


