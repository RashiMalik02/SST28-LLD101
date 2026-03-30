package com.booking.pricing;

import com.booking.enums.SeatType;
import com.booking.model.Show;

import java.util.Map;

/**
 * Default strategy: just applies seat-type multipliers over the theater base price.
 * Silver = 1.0x  |  Gold = 1.5x  |  Platinum = 2.0x
 */
public class BasePricingStrategy implements PricingStrategy {

    private static final Map<SeatType, Double> SEAT_MULTIPLIER = Map.of(
            SeatType.SILVER,   1.0,
            SeatType.GOLD,     1.5,
            SeatType.PLATINUM, 2.0
    );

    @Override
    public double calculatePrice(double basePrice, SeatType seatType, Show show) {
        return basePrice * SEAT_MULTIPLIER.getOrDefault(seatType, 1.0);
    }
}
