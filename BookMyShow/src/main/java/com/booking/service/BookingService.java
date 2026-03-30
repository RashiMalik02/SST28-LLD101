package com.booking.service;

import com.booking.concurrency.SeatLockManager;
import com.booking.enums.BookingStatus;
import com.booking.enums.PaymentMethod;
import com.booking.enums.SeatStatus;
import com.booking.enums.SeatType;
import com.booking.exception.NotFoundException;
import com.booking.exception.UnauthorizedException;
import com.booking.model.*;
import com.booking.notification.BookingObserver;
import com.booking.pricing.PricingEngine;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class BookingService {

    private static volatile BookingService instance;

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    // Observer list — thread-safe copy-on-write
    private final List<BookingObserver> observers = Collections.synchronizedList(new ArrayList<>());

    private final SeatLockManager seatLockManager = SeatLockManager.getInstance();
    private final ShowService showService         = ShowService.getInstance();
    private final TheaterService theaterService   = TheaterService.getInstance();
    private final PaymentService paymentService   = PaymentService.getInstance();
    private final PricingEngine pricingEngine     = PricingEngine.getInstance();

    private BookingService() {}

    public static BookingService getInstance() {
        if (instance == null) {
            synchronized (BookingService.class) {
                if (instance == null) instance = new BookingService();
            }
        }
        return instance;
    }


    public void registerObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public void deregisterObserver(BookingObserver observer) {
        observers.remove(observer);
    }

    public Booking initiateBooking(String userId, String showId, List<String> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("At least one seat must be selected.");
        }

        Show show = showService.getShow(showId);
        Theater theater = theaterService.getTheater(show.getTheaterId());
        Hall hall = theaterService.getHall(show.getHallId());

        // Build seatId → Seat map for price calculation
        Map<String, Seat> seatMap = hall.getSeats().stream()
                .collect(Collectors.toMap(Seat::getId, s -> s));

        for (String seatId : seatIds) {
            if (!seatMap.containsKey(seatId)) {
                throw new NotFoundException("Seat " + seatId + " does not belong to this show's hall.");
            }
        }

        // Atomically lock seats — throws if any seat is unavailable
        seatLockManager.lockSeatsForPayment(showId, seatIds, userId);

        List<SeatType> seatTypes = seatIds.stream()
                .map(id -> seatMap.get(id).getType())
                .collect(Collectors.toList());
        double totalAmount = pricingEngine.getTotalPrice(theater, show, seatTypes);

        // Create PENDING_PAYMENT booking
        Booking booking = new Booking.Builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .showId(showId)
                .seatIds(new ArrayList<>(seatIds))
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING_PAYMENT)
                .build();

        bookings.put(booking.getId(), booking);
        return booking;
    }

    // ─── Step 2: complete payment & confirm booking ───────────────────────────

    /**
     * Called after the user completes payment on the payment page.
     * On success: marks seats BOOKED, increments show counter, notifies observers.
     * On failure: releases seat locks, notifies observers of expiry.
     */
    public Booking confirmBooking(String bookingId, PaymentMethod paymentMethod) {
        Booking booking = getBooking(bookingId);

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking " + bookingId + " is not in PENDING_PAYMENT state.");
        }

        try {
            Payment payment = paymentService.processPayment(
                    bookingId, booking.getTotalAmount(), paymentMethod);

            // Payment succeeded — confirm seats and booking
            seatLockManager.confirmBooking(booking.getShowId(), booking.getSeatIds(), bookingId);
            booking.confirm(payment.getId(), paymentMethod);

            // Update show's booked seat counter (for demand pricing)
            Show show = showService.getShow(booking.getShowId());
            show.incrementBookedCount(booking.getSeatIds().size());

            notifyConfirmed(booking);
            return booking;

        } catch (Exception e) {
            // Payment failed — release seat locks so other users can book them
            seatLockManager.releaseSeats(booking.getShowId(), booking.getSeatIds());
            booking.expire();
            notifyExpired(booking);
            throw e;
        }
    }

    // ─── Cancellation (Command Pattern intent: reversible action) ─────────────

    /**
     * Cancels a confirmed booking and refunds via the original payment method.
     * Only the booking owner or an admin can cancel.
     */
    public Booking cancelBooking(String bookingId, String requestingUserId, boolean isAdmin) {
        Booking booking = getBooking(bookingId);

        if (!isAdmin && !booking.getUserId().equals(requestingUserId)) {
            throw new UnauthorizedException("You can only cancel your own bookings.");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled.");
        }

        // Process refund via same payment method
        paymentService.processRefund(bookingId);

        // Release seats back to AVAILABLE
        seatLockManager.releaseSeats(booking.getShowId(), booking.getSeatIds());

        // Decrement show's booked count
        Show show = showService.getShow(booking.getShowId());
        show.decrementBookedCount(booking.getSeatIds().size());

        booking.cancel();
        notifyCancelled(booking);
        return booking;
    }

    // ─── Seat map query ───────────────────────────────────────────────────────

    /**
     * Returns the current status of every seat in the show's hall.
     * Used to render the seat-selection UI.
     */
    public Map<String, SeatStatus> getSeatMap(String showId) {
        return seatLockManager.getSeatStatusMap(showId);
    }

    // ─── Queries ──────────────────────────────────────────────────────────────

    public Booking getBooking(String bookingId) {
        Booking b = bookings.get(bookingId);
        if (b == null) throw new NotFoundException("Booking not found: " + bookingId);
        return b;
    }

    public List<Booking> getBookingsForUser(String userId) {
        return bookings.values().stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // ─── Observer notifications ───────────────────────────────────────────────

    private void notifyConfirmed(Booking booking) {
        synchronized (observers) {
            for (BookingObserver o : observers) o.onBookingConfirmed(booking);
        }
    }

    private void notifyCancelled(Booking booking) {
        synchronized (observers) {
            for (BookingObserver o : observers) o.onBookingCancelled(booking);
        }
    }

    private void notifyExpired(Booking booking) {
        synchronized (observers) {
            for (BookingObserver o : observers) o.onBookingExpired(booking);
        }
    }
}
