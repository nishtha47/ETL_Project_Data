package stepDefinitions;

public class DataRecord {
    private int id;
    private String name;
    private double value;

    public DataRecord(int id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

}