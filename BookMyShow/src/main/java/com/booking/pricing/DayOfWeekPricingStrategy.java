package com.booking.pricing;

import com.booking.enums.SeatType;
import com.booking.model.Show;

import java.time.DayOfWeek;

/**
 * Day-of-week strategy: weekends cost more.
 *
 * Mon–Thu → 1.0x
 * Fri     → 1.15x
 * Sat–Sun → 1.3x
 *
 * Decorates another strategy.
 */
public class DayOfWeekPricingStrategy implements PricingStrategy {

    private final PricingStrategy delegate;

    public DayOfWeekPricingStrategy(PricingStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public double calculatePrice(double basePrice, SeatType seatType, Show show) {
        double price = delegate.calculatePrice(basePrice, seatType, show);
        DayOfWeek day = show.getStartTime().getDayOfWeek();
        double multiplier = getMultiplier(day);
        return Math.round(price * multiplier * 100.0) / 100.0;
    }

    private double getMultiplier(DayOfWeek day) {
        return switch (day) {
            case SATURDAY, SUNDAY -> 1.30;
            case FRIDAY            -> 1.15;
            default                -> 1.00;
        };
    }
}
