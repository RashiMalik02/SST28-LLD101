package com.booking.model;

import java.util.ArrayList;
import java.util.List;

public class City {
    private final String id;
    private final String name;
    private final List<Theater> theaters;

    public City(String id, String name) {
        this.id = id;
        this.name = name;
        this.theaters = new ArrayList<>();
    }

    public void addTheater(Theater theater) {
        theaters.add(theater);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Theater> getTheaters() { return new ArrayList<>(theaters); }
}
