package com.booking.model;

import com.booking.enums.SeatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// SRP: Theater only holds theater-level data
public class Theater {
    private final String id;
    private final String name;
    private final String cityId;
    private final String address;
    private final List<Hall> halls;

    // Base price per seat type for THIS theater (varies per theater)
    private final Map<SeatType, Double> basePricing;

    public Theater(String id, String name, String cityId, String address, Map<SeatType, Double> basePricing) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.address = address;
        this.basePricing = basePricing;
        this.halls = new ArrayList<>();
    }

    public void addHall(Hall hall) {
        halls.add(hall);
    }

    public double getBasePrice(SeatType seatType) {
        return basePricing.getOrDefault(seatType, 0.0);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCityId() { return cityId; }
    public String getAddress() { return address; }
    public List<Hall> getHalls() { return new ArrayList<>(halls); }
    public Map<SeatType, Double> getBasePricing() { return basePricing; }
}
