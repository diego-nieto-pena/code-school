package com.patterns.structural.adapter.review;

public class SquarePegAdapter extends RoundPeg {

    private SquarePeg squarePeg;

    public SquarePegAdapter(SquarePeg squarePeg) {
        this.squarePeg = squarePeg;
    }

    public double getRadius() {
        return (Math.sqrt(Math.pow((squarePeg.width / 2), 2) * 2));
    }
}
