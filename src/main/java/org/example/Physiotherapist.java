package org.example;

import java.util.ArrayList;
import java.util.List;

public class Physiotherapist {
    private static int idCounter = 1;
    private String id;
    private String name;
    private String address;
    private String phone;
    private List<String> expertiseIds;

    public Physiotherapist(String name, String address, String phone) {
        this.id = String.format("PH%03d", idCounter++);
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.expertiseIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addExpertise(String expertiseId) {
        if (!expertiseIds.contains(expertiseId)) {
            expertiseIds.add(expertiseId);
        }
    }

    public List<Expertise> getExpertiseList() {
        List<Expertise> result = new ArrayList<>();
        for (String id : expertiseIds) {
            Expertise expertise = ExpertiseRegistry.getById(id);
            if (expertise != null) {
                result.add(expertise);
            }
        }
        return result;
    }

    public void printExpertise() {
        System.out.println("Expertise of " + name + ":");
        for (Expertise e : getExpertiseList()) {
            System.out.println("- " + e.getName());
        }
    }

    public boolean hasExpertise(String name) {
        for (String id : expertiseIds) {
            Expertise exp = ExpertiseRegistry.getById(id);
            if (exp != null && exp.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getExpertiseIds() {
        return expertiseIds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " (ID: " + id + ")");
        sb.append("\n  Address: ").append(address);
        sb.append("\n  Phone: ").append(phone);
        sb.append("\n  Expertise: ");
        if (expertiseIds.isEmpty()) {
            sb.append("None");
        } else {
            for (String eid : expertiseIds) {
                Expertise e = ExpertiseRegistry.getById(eid);
                sb.append("\n    - ").append(e != null ? e.getName() : "(Unknown)");
            }
        }
        return sb.toString();
    }
}
