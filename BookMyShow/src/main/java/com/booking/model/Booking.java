package com.booking.model;

import com.booking.enums.BookingStatus;
import com.booking.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

// Builder Pattern for Booking construction
public class Booking {
    private final String id;
    private final String userId;
    private final String showId;
    private final List<String> seatIds;
    private final double totalAmount;
    private BookingStatus status;
    private PaymentMethod paymentMethod;
    private String paymentId;
    private final LocalDateTime createdAt;

    private Booking(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.showId = builder.showId;
        this.seatIds = builder.seatIds;
        this.totalAmount = builder.totalAmount;
        this.status = builder.status;
        this.paymentMethod = builder.paymentMethod;
        this.createdAt = LocalDateTime.now();
    }

    // Mutable state transitions
    public void confirm(String paymentId, PaymentMethod method) {
        this.paymentId = paymentId;
        this.paymentMethod = method;
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public void expire() {
        this.status = BookingStatus.EXPIRED;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getShowId() { return showId; }
    public List<String> getSeatIds() { return seatIds; }
    public double getTotalAmount() { return totalAmount; }
    public BookingStatus getStatus() { return status; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getPaymentId() { return paymentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Builder
    public static class Builder {
        private String id;
        private String userId;
        private String showId;
        private List<String> seatIds;
        private double totalAmount;
        private BookingStatus status = BookingStatus.PENDING_PAYMENT;
        private PaymentMethod paymentMethod;

        public Builder id(String id) { this.id = id; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder showId(String showId) { this.showId = showId; return this; }
        public Builder seatIds(List<String> seatIds) { this.seatIds = seatIds; return this; }
        public Builder totalAmount(double totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder status(BookingStatus status) { this.status = status; return this; }
        public Builder paymentMethod(PaymentMethod method) { this.paymentMethod = method; return this; }

        public Booking build() {
            if (id == null || userId == null || showId == null || seatIds == null)
                throw new IllegalStateException("Booking requires id, userId, showId, seatIds");
            return new Booking(this);
        }
    }
}
