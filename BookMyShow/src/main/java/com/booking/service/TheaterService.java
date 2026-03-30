package com.booking.service;

import com.booking.exception.NotFoundException;
import com.booking.model.Hall;
import com.booking.model.Theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SRP: manages theater and hall data only.
 */
public class TheaterService {

    private static volatile TheaterService instance;
    private final Map<String, Theater> theaters = new ConcurrentHashMap<>();
    private final Map<String, Hall> halls = new ConcurrentHashMap<>();

    private TheaterService() {}

    public static TheaterService getInstance() {
        if (instance == null) {
            synchronized (TheaterService.class) {
                if (instance == null) instance = new TheaterService();
            }
        }
        return instance;
    }

    public void addTheater(Theater theater) {
        theaters.put(theater.getId(), theater);
        // Register each hall
        for (Hall hall : theater.getHalls()) {
            halls.put(hall.getId(), hall);
        }
    }

    public void addHallToTheater(String theaterId, Hall hall) {
        Theater theater = getTheater(theaterId);
        theater.addHall(hall);
        halls.put(hall.getId(), hall);
    }

    public Theater getTheater(String theaterId) {
        Theater t = theaters.get(theaterId);
        if (t == null) throw new NotFoundException("Theater not found: " + theaterId);
        return t;
    }

    public Hall getHall(String hallId) {
        Hall h = halls.get(hallId);
        if (h == null) throw new NotFoundException("Hall not found: " + hallId);
        return h;
    }

    public List<Theater> getAllTheaters() {
        return new ArrayList<>(theaters.values());
    }

    public List<Hall> getHallsForTheater(String theaterId) {
        return getTheater(theaterId).getHalls();
    }
}
