package stepDefinitions;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import com.mysql.cj.xdevapi.Statement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataExtractionStepDefs {

    private List<String[]> data;
    private List<DataRecord> transformedData;
    private String[][] extractedData;

    @Given("a CSV file {string} with data")
    public void a_csv_file_with_data(String filename) throws IOException {
        data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        reader.readLine(); // Skip header line
        while ((line = reader.readLine()) != null) {
            data.add(line.split(","));
        }
        reader.close();
    }



        @When("the data is extracted and transformed")
        public void the_data_is_extracted_and_transformed() throws Exception {
            BufferedReader br = new BufferedReader(new FileReader("data.csv"));
            extractedData = br.lines()
                              .map(line -> line.split(","))
                              .toArray(String[][]::new);
            br.close();

            for (String[] row : extractedData) {
                if (isNumeric(row[0])) {
                    int id = Integer.parseInt(row[0]);
                    String name = row[1];
                    System.out.println("ID: " + id + ", Name: " + name);
                } else {
                    System.err.println("Non-numeric value found: " + row[0]);
                }
            }
        }

        public static boolean isNumeric(String str) {
            return str != null && str.matches("\\d+");
        }
    


    @Then("the transformed data should be loaded into the database")
    public void the_transformed_data_should_be_loaded_into_the_database() throws SQLException {
    	// Ensure the list is initialized
        if (transformedData == null) {
            transformedData = new ArrayList<>();
        }
    	
        // Make sure there is data to insert
        if (!transformedData.isEmpty()) {
    	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etldb", "root", "root");
     // Create table if it doesn't exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS data_table (" +
                                "id INT PRIMARY KEY, " +
                                "name VARCHAR(255), " +
                                "value DOUBLE)";
        java.sql.Statement statement1 = connection.createStatement();
        statement1.executeUpdate(createTableSQL);  // Execute the CREATE TABLE statement
        String sql = "INSERT INTO data_table (id, name, value) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        for (DataRecord record : transformedData) {
            statement.setInt(1, record.getId());
            statement.setString(2, record.getName());
            statement.setDouble(3, record.getValue());
            statement.executeUpdate();
        }}
    
    else {
        System.out.println("No transformed data available to load into the database.");
    } 
}
}