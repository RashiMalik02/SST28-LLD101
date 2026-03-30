package com.booking.model;

import com.booking.enums.PaymentMethod;
import com.booking.enums.PaymentStatus;

import java.time.LocalDateTime;

public class Payment {
    private final String id;
    private final String bookingId;
    private final double amount;
    private final PaymentMethod method;
    private PaymentStatus status;
    private String transactionRef;
    private final LocalDateTime createdAt;

    public Payment(String id, String bookingId, double amount, PaymentMethod method) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void markSuccess(String transactionRef) {
        this.status = PaymentStatus.SUCCESS;
        this.transactionRef = transactionRef;
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
    }

    public void markRefunded() {
        this.status = PaymentStatus.REFUNDED;
    }

    public String getId() { return id; }
    public String getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public String getTransactionRef() { return transactionRef; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
