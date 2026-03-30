package com.booking.payment;

import com.booking.enums.PaymentMethod;
import com.booking.exception.PaymentFailedException;

import java.util.List;

/**
 * Factory Pattern — resolves the correct PaymentGateway for a given PaymentMethod.
 * DIP: callers depend on PaymentGateway abstraction, not concrete implementations.
 * OCP: register new gateways by adding to the list — no switch/if chains to modify.
 */
public class PaymentGatewayFactory {

    private static volatile PaymentGatewayFactory instance;

    private final List<PaymentGateway> gateways;

    private PaymentGatewayFactory() {
        // Register all known gateways
        this.gateways = List.of(
                new UpiPaymentGateway(),
                new CardPaymentGateway(),
                new NetBankingPaymentGateway()
        );
    }

    public static PaymentGatewayFactory getInstance() {
        if (instance == null) {
            synchronized (PaymentGatewayFactory.class) {
                if (instance == null) instance = new PaymentGatewayFactory();
            }
        }
        return instance;
    }

    public PaymentGateway getGateway(PaymentMethod method) {
        return gateways.stream()
                .filter(g -> g.supports(method))
                .findFirst()
                .orElseThrow(() -> new PaymentFailedException(
                        "No gateway available for payment method: " + method));
    }
}
