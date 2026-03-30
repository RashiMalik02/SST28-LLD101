package com.booking.concurrency;

import com.booking.enums.SeatStatus;
import com.booking.exception.SeatAlreadyLockedException;
import com.booking.exception.SeatNotAvailableException;
import com.booking.model.SeatLockEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton — manages seat status per show.
 *
 * Key design decisions:
 *  - ConcurrentHashMap<showId, Map<seatId, SeatLockEntry>> — fast per-show lookup
 *  - Per-seat ReentrantLock — two users competing for the SAME seat block each other,
 *    but two users choosing DIFFERENT seats never contend.
 *  - When a user proceeds to payment, all their seats are atomically locked under a
 *    single striped multi-lock acquisition (sorted key order to prevent deadlock).
 *  - Lock TTL = LOCK_DURATION_MINUTES; a background scheduler should call
 *    releaseExpiredLocks() periodically, or locks self-expire on next read.
 */
public class SeatLockManager {

    private static final long LOCK_DURATION_MINUTES = 5;

    // showId -> seatId -> SeatLockEntry
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, SeatLockEntry>> showSeatMap
            = new ConcurrentHashMap<>();

    // showId -> seatId -> ReentrantLock  (fine-grained per-seat mutex)
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ReentrantLock>> seatLocks
            = new ConcurrentHashMap<>();

    // Singleton
    private static volatile SeatLockManager instance;

    private SeatLockManager() {}

    public static SeatLockManager getInstance() {
        if (instance == null) {
            synchronized (SeatLockManager.class) {
                if (instance == null) {
                    instance = new SeatLockManager();
                }
            }
        }
        return instance;
    }

    /** Initialise all seats for a show as AVAILABLE. Called when a Show is added. */
    public void initializeShow(String showId, List<String> seatIds) {
        showSeatMap.putIfAbsent(showId, new ConcurrentHashMap<>());
        seatLocks.putIfAbsent(showId, new ConcurrentHashMap<>());

        ConcurrentHashMap<String, SeatLockEntry> entries = showSeatMap.get(showId);
        ConcurrentHashMap<String, ReentrantLock> locks = seatLocks.get(showId);

        for (String seatId : seatIds) {
            entries.putIfAbsent(seatId, new SeatLockEntry(seatId, showId));
            locks.putIfAbsent(seatId, new ReentrantLock(true)); // fair lock
        }
    }

    /**
     * Attempts to lock a list of seats for a user entering the payment page.
     * Uses sorted lock acquisition order to prevent deadlock between concurrent users.
     *
     * @throws SeatAlreadyLockedException if ANY seat is already locked/booked by another user
     */
    public void lockSeatsForPayment(String showId, List<String> seatIds, String userId) {
        ConcurrentHashMap<String, ReentrantLock> locks = seatLocks.get(showId);
        ConcurrentHashMap<String, SeatLockEntry> entries = showSeatMap.get(showId);

        if (locks == null || entries == null) {
            throw new SeatNotAvailableException("Show not found: " + showId);
        }

        // Sort seat IDs to enforce a global lock ordering — prevents deadlock
        List<String> sortedSeatIds = seatIds.stream().sorted().toList();

        List<ReentrantLock> acquiredLocks = new java.util.ArrayList<>();
        try {
            for (String seatId : sortedSeatIds) {
                ReentrantLock lock = locks.get(seatId);
                if (lock == null) throw new SeatNotAvailableException("Seat not found: " + seatId);

                lock.lock();
                acquiredLocks.add(lock);

                SeatLockEntry entry = entries.get(seatId);
                SeatStatus status = entry.getStatus(); // auto-expires stale locks on read

                if (status == SeatStatus.BOOKED) {
                    throw new SeatAlreadyLockedException("Seat " + seatId + " is already booked.");
                }
                if (status == SeatStatus.LOCKED && !entry.isLockExpired()) {
                    // Another user locked this seat and their TTL hasn't expired
                    throw new SeatAlreadyLockedException(
                            "Seat " + seatId + " is being held by another user. Please try again shortly.");
                }
            }

            // All seats are available — lock them all
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES);
            for (String seatId : sortedSeatIds) {
                entries.get(seatId).lock(userId, expiry);
            }

        } catch (SeatAlreadyLockedException | SeatNotAvailableException e) {
            throw e; // re-throw after finally releases acquired locks
        } finally {
            // Release intrinsic locks in reverse order
            for (int i = acquiredLocks.size() - 1; i >= 0; i--) {
                acquiredLocks.get(i).unlock();
            }
        }
    }

    /** Called on successful payment — permanently marks seats as BOOKED. */
    public void confirmBooking(String showId, List<String> seatIds, String bookingId) {
        ConcurrentHashMap<String, SeatLockEntry> entries = showSeatMap.get(showId);
        if (entries == null) throw new SeatNotAvailableException("Show not found: " + showId);

        for (String seatId : seatIds) {
            SeatLockEntry entry = entries.get(seatId);
            if (entry != null) entry.book(bookingId);
        }
    }

    /** Called on payment failure or cancellation — releases the temporary lock. */
    public void releaseSeats(String showId, List<String> seatIds) {
        ConcurrentHashMap<String, SeatLockEntry> entries = showSeatMap.get(showId);
        if (entries == null) return;
        for (String seatId : seatIds) {
            SeatLockEntry entry = entries.get(seatId);
            if (entry != null) entry.release();
        }
    }

    /** Returns the full seat-status map for a show (for seat map API). */
    public Map<String, SeatStatus> getSeatStatusMap(String showId) {
        ConcurrentHashMap<String, SeatLockEntry> entries = showSeatMap.get(showId);
        if (entries == null) return Map.of();
        Map<String, SeatStatus> result = new ConcurrentHashMap<>();
        entries.forEach((seatId, entry) -> result.put(seatId, entry.getStatus()));
        return result;
    }

    /** Periodic cleanup — can be called by a ScheduledExecutorService every minute. */
    public void releaseExpiredLocks() {
        showSeatMap.forEach((showId, seatEntries) ->
            seatEntries.forEach((seatId, entry) -> {
                if (entry.isLockExpired()) entry.release();
            })
        );
    }

    public int getAvailableSeatCount(String showId) {
        ConcurrentHashMap<String, SeatLockEntry> entries = showSeatMap.get(showId);
        if (entries == null) return 0;
        return (int) entries.values().stream()
                .filter(e -> e.getStatus() == SeatStatus.AVAILABLE)
                .count();
    }
}
