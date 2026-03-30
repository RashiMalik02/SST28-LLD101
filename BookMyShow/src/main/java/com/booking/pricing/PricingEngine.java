package com.booking.pricing;

import com.booking.enums.SeatType;
import com.booking.model.Show;
import com.booking.model.Theater;

/**
 * PricingEngine builds and applies a decorator chain of pricing strategies.
 *
 * Chain (innermost → outermost):
 *   BasePricing → DemandBased → DayOfWeek → WeekOfMonth
 *
 * Each layer reads from the previous and adds its own multiplier.
 * New pricing factors can be added by wrapping with another decorator — OCP.
 */
public class PricingEngine {

    private static volatile PricingEngine instance;

    private PricingEngine() {}

    public static PricingEngine getInstance() {
        if (instance == null) {
            synchronized (PricingEngine.class) {
                if (instance == null) instance = new PricingEngine();
            }
        }
        return instance;
    }

    /**
     * Returns the final price for a seat.
     *
     * @param theater  needed for base price per seat type
     * @param show     needed for occupancy, start time
     * @param seatType Silver / Gold / Platinum
     */
    public double getPrice(Theater theater, Show show, SeatType seatType) {
        double basePrice = theater.getBasePrice(seatType);

        // Decorator chain — order matters: base → demand → day → week
        PricingStrategy strategy =
            new WeekOfMonthPricingStrategy(
                new DayOfWeekPricingStrategy(
                    new DemandBasedPricingStrategy(
                        new BasePricingStrategy()
                    )
                )
            );

        return strategy.calculatePrice(basePrice, seatType, show);
    }

    /**
     * Calculate total price for a mixed list of seat types.
     */
    public double getTotalPrice(Theater theater, Show show, java.util.List<SeatType> seatTypes) {
        return seatTypes.stream()
                .mapToDouble(type -> getPrice(theater, show, type))
                .sum();
    }
}
