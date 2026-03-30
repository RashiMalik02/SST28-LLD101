package com.booking.notification;

import com.booking.model.Booking;

public class EmailNotificationObserver implements BookingObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.printf("[EMAIL] Booking confirmed — ID: %s | Seats: %s | Amount: ₹%.2f%n",
                booking.getId(), booking.getSeatIds(), booking.getTotalAmount());
        // In production: send via SES / SMTP
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.printf("[EMAIL] Booking cancelled — ID: %s. Refund of ₹%.2f initiated.%n",
                booking.getId(), booking.getTotalAmount());
    }

    @Override
    public void onBookingExpired(Booking booking) {
        System.out.printf("[EMAIL] Booking expired — ID: %s. Seats released.%n", booking.getId());
    }
}
