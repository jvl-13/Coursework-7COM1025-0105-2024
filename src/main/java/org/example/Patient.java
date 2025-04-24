package org.example;

public class Patient {
    private static int idCounter = 1;
    private String id;
    private String name;
    private String address;
    private String phone;

    public Patient(String name, String address, String phone) {
        this.id = String.format("P%03d", idCounter++);
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}' + '\n';
    }
}
