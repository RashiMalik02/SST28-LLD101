package com.booking.payment;

import com.booking.enums.PaymentMethod;
import com.booking.exception.PaymentFailedException;
import com.booking.model.Payment;

import java.util.Set;
import java.util.UUID;

/**
 * Adapter for credit/debit card payment (e.g. Stripe, Razorpay Cards).
 */
public class CardPaymentGateway implements PaymentGateway {

    private static final Set<PaymentMethod> SUPPORTED =
            Set.of(PaymentMethod.CREDIT_CARD, PaymentMethod.DEBIT_CARD);

    @Override
    public String processPayment(Payment payment) {
        System.out.println("[CARD] Processing ₹" + payment.getAmount()
                + " via " + payment.getMethod()
                + " for booking: " + payment.getBookingId());
        if (payment.getAmount() <= 0) {
            throw new PaymentFailedException("Invalid card payment amount.");
        }
        return "CARD-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String processRefund(Payment payment) {
        System.out.println("[CARD] Refund ₹" + payment.getAmount()
                + " to card for txn: " + payment.getTransactionRef());
        return "CARD-REFUND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean supports(PaymentMethod method) {
        return SUPPORTED.contains(method);
    }
}
