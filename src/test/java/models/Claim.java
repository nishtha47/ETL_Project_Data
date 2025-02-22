package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a claim made by a patient for a medical service.
 */
@XmlRootElement
public final class Claim {

    private final String patientId;
    private final String patientName;
    private final String claimId;
    private final double claimAmount;

    /**
     * Constructs a new Claim object with the specified details.
     *
     * @param patientId   The unique identifier of the patient.
     * @param patientName The name of the patient.
     * @param claimId     The unique identifier for the claim.
     * @param claimAmount The amount of the claim. Must be positive.
     * @throws IllegalArgumentException If any field is null/empty or if claimAmount is non-positive.
     */
    public Claim(String patientId, String patientName, String claimId, double claimAmount) {
        if (patientId == null || patientId.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty.");
        }
        if (patientName == null || patientName.isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be null or empty.");
        }
        if (claimId == null || claimId.isEmpty()) {
            throw new IllegalArgumentException("Claim ID cannot be null or empty.");
        }
        if (claimAmount <= 0) {
            throw new IllegalArgumentException("Claim amount must be positive.");
        }

        this.patientId = patientId;
        this.patientName = patientName;
        this.claimId = claimId;
        this.claimAmount = claimAmount;
    }

    /**
     * Gets the unique identifier for the patient.
     * 
     * @return The patient ID.
     */
    @XmlElement
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the name of the patient.
     * 
     * @return The patient's name.
     */
    @XmlElement
    public String getPatientName() {
        return patientName;
    }

    /**
     * Gets the unique identifier for the claim.
     * 
     * @return The claim ID.
     */
    @XmlElement
    public String getClaimId() {
        return claimId;
    }

    /**
     * Gets the claim amount.
     * 
     * @return The amount of the claim.
     */
    @XmlElement
    public double getClaimAmount() {
        return claimAmount;
    }
}
