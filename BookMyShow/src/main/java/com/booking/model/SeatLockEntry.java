package com.booking.model;

import com.booking.enums.SeatStatus;

import java.time.LocalDateTime;

// Value object representing a seat's status for a specific show
public class SeatLockEntry {
    private final String seatId;
    private final String showId;
    private SeatStatus status;
    private String lockedByUserId;
    private String bookingId;
    private LocalDateTime lockExpiry;

    public SeatLockEntry(String seatId, String showId) {
        this.seatId = seatId;
        this.showId = showId;
        this.status = SeatStatus.AVAILABLE;
    }

    public boolean isLockExpired() {
        return status == SeatStatus.LOCKED &&
               lockExpiry != null &&
               LocalDateTime.now().isAfter(lockExpiry);
    }

    public void lock(String userId, LocalDateTime expiry) {
        this.status = SeatStatus.LOCKED;
        this.lockedByUserId = userId;
        this.lockExpiry = expiry;
    }

    public void book(String bookingId) {
        this.status = SeatStatus.BOOKED;
        this.bookingId = bookingId;
        this.lockExpiry = null;
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
        this.lockedByUserId = null;
        this.lockExpiry = null;
        this.bookingId = null;
    }

    public SeatStatus getStatus() {
        // Auto-expire lock on read
        if (isLockExpired()) {
            release();
        }
        return status;
    }

    public String getSeatId() { return seatId; }
    public String getShowId() { return showId; }
    public String getLockedByUserId() { return lockedByUserId; }
    public String getBookingId() { return bookingId; }
    public LocalDateTime getLockExpiry() { return lockExpiry; }
}
