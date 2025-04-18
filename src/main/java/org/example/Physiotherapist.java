package org.example;

import java.util.ArrayList;
import java.util.List;

public class Physiotherapist {
    private static int idCounter = 1;
    private String id;
    private String name;
    private String address;
    private String phone;
    private List<Expertise> expertiseList = new ArrayList<>();

    public Physiotherapist(String name, String address, String phone) {
        this.id = String.format("PH%03d", idCounter++);
        this.name = name;
        this.address = address;
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Expertise> getExpertiseList() {
        return expertiseList;
    }

    public void setExpertiseList(List<Expertise> expertiseList) {
        this.expertiseList = expertiseList;
    }

    public void addExpertise(Expertise expertise) {
        if (!expertiseList.contains(expertise)) {
            expertiseList.add(expertise);
        }
    }

    public boolean hasExpertise(String expertiseName) {
        return expertiseList.stream()
                .anyMatch(e -> e.getName().equalsIgnoreCase(expertiseName));
    }

    @Override
    public String toString() {
        return "Physiotherapist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", expertiseList=" + expertiseList +
                '}';
    }
}
