package org.example;

import java.util.*;

public class ExpertiseRegistry {
    private static Map<String, Expertise> expertiseMap = new HashMap<>();

    public static void addExpertise(Expertise expertise) {
        expertiseMap.put(expertise.getId(), expertise);
    }

    public static Expertise getById(String id) {
        return expertiseMap.get(id);
    }

    public static List<Expertise> getAll() {
        return new ArrayList<>(expertiseMap.values());
    }

    public static Expertise findByName(String name) {
        for (Expertise e : expertiseMap.values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
