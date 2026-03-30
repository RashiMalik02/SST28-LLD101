package com.booking.pricing;

import com.booking.enums.SeatType;
import com.booking.model.Show;

/**
 * Week-of-month strategy: first and last week of the month are premium.
 *
 * Days 1–7   (first week)  → 1.1x
 * Days 8–21  (mid month)   → 1.0x
 * Days 22–31 (last week)   → 1.05x
 *
 * Decorates another strategy.
 */
public class WeekOfMonthPricingStrategy implements PricingStrategy {

    private final PricingStrategy delegate;

    public WeekOfMonthPricingStrategy(PricingStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public double calculatePrice(double basePrice, SeatType seatType, Show show) {
        double price = delegate.calculatePrice(basePrice, seatType, show);
        int dayOfMonth = show.getStartTime().getDayOfMonth();
        double multiplier = getMultiplier(dayOfMonth);
        return Math.round(price * multiplier * 100.0) / 100.0;
    }

    private double getMultiplier(int dayOfMonth) {
        if (dayOfMonth <= 7)  return 1.10;
        if (dayOfMonth >= 22) return 1.05;
        return 1.00;
    }
}
