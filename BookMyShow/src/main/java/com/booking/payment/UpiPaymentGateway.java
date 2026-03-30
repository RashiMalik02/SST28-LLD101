package com.booking.payment;

import com.booking.enums.PaymentMethod;
import com.booking.exception.PaymentFailedException;
import com.booking.model.Payment;

import java.util.UUID;

/**
 * Adapter for UPI external API.
 * In production this would call Razorpay/PayU UPI APIs.
 */
public class UpiPaymentGateway implements PaymentGateway {

    @Override
    public String processPayment(Payment payment) {
        // Simulate external UPI API call
        System.out.println("[UPI] Processing payment of ₹" + payment.getAmount()
                + " for booking: " + payment.getBookingId());
        // Simulate success (replace with real HTTP call)
        if (payment.getAmount() <= 0) {
            throw new PaymentFailedException("Invalid amount for UPI payment.");
        }
        return "UPI-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String processRefund(Payment payment) {
        System.out.println("[UPI] Initiating refund of ₹" + payment.getAmount()
                + " for txn: " + payment.getTransactionRef());
        return "UPI-REFUND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.UPI;
    }
}
