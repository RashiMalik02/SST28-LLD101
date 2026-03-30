package com.booking.payment;

import com.booking.enums.PaymentMethod;
import com.booking.exception.PaymentFailedException;
import com.booking.model.Payment;

import java.util.UUID;

/**
 * Adapter for net banking external API.
 */
public class NetBankingPaymentGateway implements PaymentGateway {

    @Override
    public String processPayment(Payment payment) {
        System.out.println("[NETBANKING] Processing ₹" + payment.getAmount()
                + " for booking: " + payment.getBookingId());
        if (payment.getAmount() <= 0) {
            throw new PaymentFailedException("Invalid net banking amount.");
        }
        return "NB-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String processRefund(Payment payment) {
        System.out.println("[NETBANKING] Refund ₹" + payment.getAmount()
                + " for txn: " + payment.getTransactionRef());
        return "NB-REFUND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.NET_BANKING;
    }
}
