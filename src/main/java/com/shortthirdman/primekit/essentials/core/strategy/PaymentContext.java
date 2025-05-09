package com.shortthirdman.primekit.essentials.core.strategy;

import org.springframework.stereotype.Component;

@Component
public class PaymentContext {

    private final PaymentStrategy strategy;

    public PaymentContext(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void checkout(double amount) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set.");
        }

        strategy.pay(amount);
    }

    public void checkout(Double amount) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set.");
        }

        strategy.pay(amount);
    }
}
