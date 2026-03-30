package com.booking.notification;

import com.booking.model.Booking;

/**
 * Observer Pattern — any component that needs to react to booking lifecycle events
 * implements this interface. The BookingService notifies all registered observers.
 *
 * OCP: new observers (push notification, analytics, loyalty points) can be added
 * without changing BookingService.
 */
public interface BookingObserver {
    void onBookingConfirmed(Booking booking);
    void onBookingCancelled(Booking booking);
    void onBookingExpired(Booking booking);
}
