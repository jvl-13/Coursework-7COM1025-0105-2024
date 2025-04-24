package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Treatment {
    private static int idCounter = 1;
    private String id;
    private String name;
    private Date dateTime;
    private Physiotherapist physiotherapist;
    private int roomNumber;
    private String status; // "available", "booked"

    public Treatment(String name, Date dateTime, Physiotherapist physio, int roomNumber) {
        this.id = String.format("T%04d", idCounter++);
        this.name = name;
        this.physiotherapist = physio;
        this.dateTime = dateTime;
        this.roomNumber = roomNumber;
        this.status = "available"; // Default status is available
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", physiotherapist=" + physiotherapist +
                ", roomNumber=" + roomNumber +
                ", status='" + status + '\'' +
                '}';
    }
}
