package com.booking.notification;

import com.booking.model.Booking;

public class SmsNotificationObserver implements BookingObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.printf("[SMS] Your booking %s is confirmed! Seats: %s%n",
                booking.getId(), booking.getSeatIds());
        // In production: call Twilio / SNS
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.printf("[SMS] Booking %s cancelled. Refund in 3–5 business days.%n",
                booking.getId());
    }

    @Override
    public void onBookingExpired(Booking booking) {
        System.out.printf("[SMS] Your seat hold for booking %s has expired.%n", booking.getId());
    }
}
