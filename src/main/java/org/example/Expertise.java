package org.example;

import java.util.Objects;

public class Expertise {
    private String name;

    public Expertise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expertise)) return false;
        Expertise ex = (Expertise) o;
        return name.equalsIgnoreCase(ex.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
