package com.booking.model;

import com.booking.enums.SeatStatus;
import com.booking.enums.SeatType;

// Seat is a value object; status is managed by SeatLockManager per show
public class Seat {
    private final String id;       // e.g. "A3"
    private final String hallId;
    private final String rowLabel;
    private final int seatNumber;
    private final SeatType type;

    // NOTE: SeatStatus is NOT stored on Seat itself.
    // Status is per-show and managed in SeatLockManager (avoids mutable shared state on the model).
    // This makes Seat effectively immutable and thread-safe.

    public Seat(String id, String hallId, String rowLabel, int seatNumber, SeatType type) {
        this.id = id;
        this.hallId = hallId;
        this.rowLabel = rowLabel;
        this.seatNumber = seatNumber;
        this.type = type;
    }

    public String getId() { return id; }
    public String getHallId() { return hallId; }
    public String getRowLabel() { return rowLabel; }
    public int getSeatNumber() { return seatNumber; }
    public SeatType getType() { return type; }
}
