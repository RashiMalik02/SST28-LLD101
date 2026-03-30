package com.booking.pricing;

import com.booking.enums.SeatType;
import com.booking.model.Show;
import com.booking.model.Theater;

/**
 * Strategy Pattern — OCP: adding a new pricing algorithm = new class, zero changes to existing code.
 */
public interface PricingStrategy {
    /**
     * Calculate the final price for a seat.
     *
     * @param basePrice   theater's base price for this seat type
     * @param seatType    Silver / Gold / Platinum
     * @param show        the show being priced (carries occupancy & start time)
     * @return            final price in INR
     */
    double calculatePrice(double basePrice, SeatType seatType, Show show);
}
