package com.booking.payment;

import com.booking.model.Payment;

/**
 * Strategy interface for payment providers.
 * ISP: each provider implements only what it needs.
 */
public interface PaymentGateway {

    /**
     * Initiates a payment and returns a transaction reference on success.
     * Throws PaymentFailedException on failure.
     */
    String processPayment(Payment payment);

    /**
     * Initiates a refund for a previously completed payment.
     * Returns refund reference on success.
     */
    String processRefund(Payment payment);

    /**
     * Returns true if this gateway can handle the given payment method.
     */
    boolean supports(com.booking.enums.PaymentMethod method);
}
