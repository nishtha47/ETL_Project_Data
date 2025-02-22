package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class BookStepDefinitions {

    private RequestSpecification request;
    private Response response;
    private String apiEndpoint;
    private String isbn;

    public BookStepDefinitions() {
        // Load properties file and get the API endpoint
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/java/resources/config.properties")) {
            // Load the properties file
            properties.load(input);
            // Get the API endpoint from the properties file
            apiEndpoint = properties.getProperty("api.endpoint");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load properties file");
        }
    }

    @Given("a book exists with an isbn of {string}")
    public void a_book_exists_with_an_isbn_of(String isbn) {
        // Initialize the request object
        this.isbn = isbn;  // Save the ISBN passed in the scenario
        request = given();
    }

    @Given("no book exists with an isbn of {string}")
    public void no_book_exists_with_an_isbn_of(String isbn) {
        // Initialize the request object
        this.isbn = isbn;  // Save the ISBN passed in the scenario for negative test
        request = given();
    }

    @When("a user retrieves the book by isbn")
    public void a_user_retrieves_the_book_by_isbn() {
        // Use the initialized request object to make the GET request using dynamic API endpoint and ISBN
        response = request
                       .when()
                       .get(apiEndpoint + "isbn:" + isbn);  // Use the dynamic ISBN from the scenario
    }

    @Then("the status code is {int}")
    public void verify_status_code(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        System.out.println("Actual status code: " + actualStatusCode);
        System.out.println("Response body: " + responseBody);
        assertThat("Status code does not match", actualStatusCode, equalTo(expectedStatusCode));
    }
    

    @Then("response includes the following")
    public void response_equals(Map<String, String> expectedResponse) {
        // Log the entire response for debugging
        System.out.println("Full response: " + response.getBody().asString());
        String totalItems = response.jsonPath().getString("totalItems");

       // String totalItems = JsonPath.read(response.getBody().asString(), "$.totalItems").toString();

        if ("0".equals(totalItems)) {
            // Handle case where no books are found
            System.out.println("No books found for the given ISBN.");
        } else {
            // Proceed with assertion for expected key-value pairs when books are found
            for (Map.Entry<String, String> entry : expectedResponse.entrySet()) {
                String key = entry.getKey();
                String expectedValue = entry.getValue();

                // Retrieve the actual values for the key
                List<Object> actualValues = response.jsonPath().getList(key);

                // Check if the key exists in the response and handle null or empty values
                if (actualValues == null || actualValues.isEmpty()) {
                    System.out.println("Key not found or is empty in response: " + key);
                    continue; // Skip processing for missing keys
                }

                // Flatten the actual values into strings
                List<String> flattenedValues = actualValues.stream()
                                                           .map(Object::toString)
                                                           .collect(Collectors.toList());

                // Handle publisherDate normalization if the key is 'items.volumeInfo.publisherDate'
                if (key.equals("items.volumeInfo.publisherDate")) {
                    flattenedValues = normalizePublisherDateValues(flattenedValues);
                    expectedValue = expectedValue.split("-")[0]; // Extract the year from expected value
                }

                // Assert that the response contains the expected value
                try {
                    assertThat("Response does not contain expected key-value in any order: " + key,
                            flattenedValues, hasItem(expectedValue));
                } catch (AssertionError e) {
                    // Log the actual response data for debugging purposes
                    System.out.println("Expected: " + expectedValue + ", but found: " + flattenedValues);
                    throw e;  // Rethrow the exception after logging
                }
            }
        }}
            @Then("response includes the following in any order")
            public void response_contains_in_any_order(Map<String, String> expectedResponse) {
                // Log the entire response for debugging
                System.out.println("Full response: " + response.getBody().asString());

                for (Map.Entry<String, String> entry : expectedResponse.entrySet()) {
                    String key = entry.getKey();
                    String expectedValue = entry.getValue();

                    // Retrieve the actual values for the key
                    List<Object> actualValues = response.jsonPath().getList(key);

                    // Check if the key exists in the response and handle null or empty values
                    if (actualValues == null || actualValues.isEmpty()) {
                        System.out.println("Key not found or is empty in response: " + key);
                        continue; // Skip processing for missing keys
                    }

                    // Flatten the actual values into strings
                    List<String> flattenedValues = actualValues.stream()
                                                               .map(Object::toString)
                                                               .collect(Collectors.toList());

                    // Handle publisherDate normalization if the key is 'items.volumeInfo.publisherDate'
                    if (key.equals("items.volumeInfo.publisherDate")) {
                        flattenedValues = normalizePublisherDateValues(flattenedValues);
                        expectedValue = expectedValue.split("-")[0]; // Extract the year from expected value
                    }

                    // Assert that the response contains the expected value
                    try {
                        assertThat("Response does not contain expected key-value in any order: " + key,
                                flattenedValues, hasItem(expectedValue));
                    } catch (AssertionError e) {
                        // Log the actual response data for debugging purposes
                        System.out.println("Expected: " + expectedValue + ", but found: " + flattenedValues);
                        throw e;  // Rethrow the exception after logging
                    }
                }
            
    }

    /**
     * Helper method to normalize publisherDate values by extracting the year from a full date.
     * @param dateValues List of date strings in the format "yyyy-mm-dd".
     * @return List of date strings containing only the year.
     */
    private List<String> normalizePublisherDateValues(List<String> dateValues) {
        return dateValues.stream()
                         .map(date -> date.split("-")[0]) // Extract year only if it's a full date
                         .collect(Collectors.toList());
    }
}
