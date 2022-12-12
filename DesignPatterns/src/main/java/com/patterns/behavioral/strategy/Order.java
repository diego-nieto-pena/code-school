package com.patterns.behavioral.strategy;

/**
 * Order class doesn't know the concrete payment method (strategy) user has picked.
 * It uses common strategy interface to delegate collecting payment data to strategy object.
 */
public class Order {

    private int totalCost = 0;
    private boolean isClosed = false;

    public void processOrder(PayStrategy strategy) {
        strategy.collectPaymentDetails();
    }

    public void setTotalCost(int cost) {
        this.totalCost += cost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed() {
        isClosed = true;
    }
}
