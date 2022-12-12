package com.patterns.behavioral.strategy;

public interface PayStrategy {
    boolean pay(int paymentAmount);

    void collectPaymentDetails();
}
