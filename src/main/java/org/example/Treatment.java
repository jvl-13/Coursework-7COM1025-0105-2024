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
    private String status; // "available", "booked", "cancelled"

    public Treatment(String name, String dateTime, Physiotherapist physio, int roomNumber) {
        this.id = String.format("T%04d", idCounter++);
        this.name = name;
        this.physiotherapist = physio;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            this.dateTime = sdf.parse(dateTime);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
            this.dateTime = null;
        }
        this.roomNumber = roomNumber;
        this.status = "available"; // Default status is available
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public void setPhysiotherapist(Physiotherapist physiotherapist) {
        this.physiotherapist = physiotherapist;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
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
