package com.patterns.structural.adapter.review;
public class SquarePeg {

    protected double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    public double getArea() {
        return Math.pow(width, 2);
    }
}
