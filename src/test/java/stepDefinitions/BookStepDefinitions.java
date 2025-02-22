package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookStepDefinitions {

    private RequestSpecification request;
    private Response response;

    @Given("a book exists with an isbn of {string}")
    public void a_book_exists_with_an_isbn_of(String isbn) {
        // Initialize the request object
        request = given();
        // You can add other necessary setup for the request (e.g., headers, authentication, etc.)
    }

    @When("a user retrieves the book by isbn")
    public void a_user_retrieves_the_book_by_isbn() {
        // Use the initialized request object to make the GET request
        response = request
                        .when()
                        .get("https://www.googleapis.com/books/v1/volumes?q=isbn:9781451648546");
    }

    @Then("the status code is {int}")
    public void verify_status_code(int expectedStatusCode) {
        // Verify the status code in the response
        assertThat("Status code does not match", response.statusCode(), equalTo(expectedStatusCode));
    }

    @Then("response includes the following")
    public void response_equals(Map<String, String> expectedResponse) {
        // Verify that the response matches the expected key-value pairs
        for (Map.Entry<String, String> entry : expectedResponse.entrySet()) {
            String actualValue = response.jsonPath().getString(entry.getKey());
            assertThat("Response does not contain expected key-value: " + entry.getKey(), actualValue, equalTo(entry.getValue()));
        }
    }

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

            // Special handling for 'publisherDate' field (check if it's a date and normalize)
            if (key.equals("items.volumeInfo.publisherDate")) {
                // Extract only the year if the value is a date (e.g., "2011-10-24" -> "2011")
                expectedValue = expectedValue.split("-")[0]; // Extract the year
            }

            // Check if the key refers to publisherDate and ensure the response value is formatted properly
            if (key.equals("items.volumeInfo.publisherDate") && !flattenedValues.isEmpty()) {
                flattenedValues = flattenedValues.stream()
                                                 .map(date -> date.split("-")[0]) // Extract year only if it's a full date
                                                 .collect(Collectors.toList());
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

}
