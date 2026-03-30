package com.booking.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

// Show = one screening of a Movie in a Hall at a specific time
public class Show {
    private final String id;
    private final String movieId;
    private final String hallId;
    private final String theaterId;
    private final LocalDateTime startTime;
    private final int totalSeats;

    // Thread-safe counter for demand-based pricing
    private final AtomicInteger bookedSeatCount = new AtomicInteger(0);

    public Show(String id, String movieId, String hallId, String theaterId,
                LocalDateTime startTime, int totalSeats) {
        this.id = id;
        this.movieId = movieId;
        this.hallId = hallId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.totalSeats = totalSeats;
    }

    public void incrementBookedCount(int count) {
        bookedSeatCount.addAndGet(count);
    }

    public void decrementBookedCount(int count) {
        bookedSeatCount.addAndGet(-count);
    }

    public double getOccupancyRatio() {
        if (totalSeats == 0) return 0;
        return (double) bookedSeatCount.get() / totalSeats;
    }

    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getHallId() { return hallId; }
    public String getTheaterId() { return theaterId; }
    public LocalDateTime getStartTime() { return startTime; }
    public int getTotalSeats() { return totalSeats; }
    public int getBookedSeatCount() { return bookedSeatCount.get(); }
}
