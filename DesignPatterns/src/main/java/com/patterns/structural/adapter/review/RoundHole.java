package com.patterns.structural.adapter.review;

public class RoundHole {

    private double radius;

    public RoundHole(double radius) {
        this.radius = radius;
    }

    public boolean fits(RoundPeg roundPeg) {
        return this.radius >= roundPeg.radius;
    }
}
