package com.booking.service;

import com.booking.enums.PaymentMethod;
import com.booking.enums.PaymentStatus;
import com.booking.exception.NotFoundException;
import com.booking.exception.PaymentFailedException;
import com.booking.model.Payment;
import com.booking.payment.PaymentGateway;
import com.booking.payment.PaymentGatewayFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SRP: processes payments and refunds via the PaymentGatewayFactory.
 * DIP: depends on PaymentGateway interface, not concrete gateways.
 */
public class PaymentService {

    private static volatile PaymentService instance;

    private final Map<String, Payment> payments = new ConcurrentHashMap<>();
    // bookingId → paymentId
    private final Map<String, String> bookingToPayment = new ConcurrentHashMap<>();

    private final PaymentGatewayFactory gatewayFactory = PaymentGatewayFactory.getInstance();

    private PaymentService() {}

    public static PaymentService getInstance() {
        if (instance == null) {
            synchronized (PaymentService.class) {
                if (instance == null) instance = new PaymentService();
            }
        }
        return instance;
    }

    /**
     * Initiates payment for a booking.
     * Returns the Payment with status = SUCCESS or throws PaymentFailedException.
     */
    public Payment processPayment(String bookingId, double amount, PaymentMethod method) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                bookingId,
                amount,
                method
        );
        payments.put(payment.getId(), payment);
        bookingToPayment.put(bookingId, payment.getId());

        try {
            PaymentGateway gateway = gatewayFactory.getGateway(method);
            String txnRef = gateway.processPayment(payment);
            payment.markSuccess(txnRef);
        } catch (Exception e) {
            payment.markFailed();
            throw new PaymentFailedException("Payment failed: " + e.getMessage());
        }

        return payment;
    }

    /**
     * Processes a refund for a confirmed booking's payment.
     * Returns the refund reference.
     */
    public String processRefund(String bookingId) {
        String paymentId = bookingToPayment.get(bookingId);
        if (paymentId == null) throw new NotFoundException("No payment found for booking: " + bookingId);

        Payment payment = payments.get(paymentId);
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentFailedException("Cannot refund a payment that is not in SUCCESS state.");
        }

        PaymentGateway gateway = gatewayFactory.getGateway(payment.getMethod());
        String refundRef = gateway.processRefund(payment);
        payment.markRefunded();

        System.out.println("[REFUND] Refund issued: " + refundRef + " for booking: " + bookingId);
        return refundRef;
    }

    public Payment getPaymentForBooking(String bookingId) {
        String paymentId = bookingToPayment.get(bookingId);
        if (paymentId == null) throw new NotFoundException("No payment for booking: " + bookingId);
        return payments.get(paymentId);
    }
}
