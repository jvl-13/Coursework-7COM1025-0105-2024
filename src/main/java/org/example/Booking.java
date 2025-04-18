package org.example;

public class Booking {
    private static int idCounter = 1;
    private String id;
    private Patient patient;
    private Treatment treatment;
    private String status;

    public Booking(Patient patient, Treatment treatment) {
        this.id = String.format("B%05d", idCounter++);
        this.patient = patient;
        this.treatment = treatment;
        this.status = "booked";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", patient=" + patient +
                ", treatment=" + treatment +
                ", status='" + status + '\'' +
                '}';
    }
}
