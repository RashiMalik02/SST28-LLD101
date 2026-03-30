package com.booking.pricing;

import com.booking.enums.SeatType;
import com.booking.model.Show;

/**
 * Demand-based strategy: price increases as occupancy rises.
 *
 * Occupancy tiers:
 *   < 50%  → 1.0x (base)
 *   50-75% → 1.2x
 *   75-90% → 1.5x
 *   > 90%  → 1.8x
 *
 * This wraps a delegate strategy (typically BasePricingStrategy) and multiplies the result.
 * Decorator Pattern: adds demand pricing on top of any existing strategy.
 */
public class DemandBasedPricingStrategy implements PricingStrategy {

    private final PricingStrategy delegate;

    public DemandBasedPricingStrategy(PricingStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public double calculatePrice(double basePrice, SeatType seatType, Show show) {
        double price = delegate.calculatePrice(basePrice, seatType, show);
        double occupancy = show.getOccupancyRatio();
        double surgeMultiplier = getSurgeMultiplier(occupancy);
        return Math.round(price * surgeMultiplier * 100.0) / 100.0;
    }

    private double getSurgeMultiplier(double occupancy) {
        if (occupancy >= 0.90) return 1.8;
        if (occupancy >= 0.75) return 1.5;
        if (occupancy >= 0.50) return 1.2;
        return 1.0;
    }
}
