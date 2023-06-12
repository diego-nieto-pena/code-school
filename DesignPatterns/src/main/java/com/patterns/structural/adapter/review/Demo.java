package com.patterns.structural.adapter.review;
public class Demo {
    public static void main(String[] args) {

        RoundHole hole = new RoundHole(2.3);

        RoundPeg roundPeg = new RoundPeg(3.2);

        System.out.println(hole.fits(roundPeg));

        SquarePeg squarePeg = new SquarePeg(2);

        SquarePegAdapter adapter = new SquarePegAdapter(squarePeg);

        System.out.println(hole.fits(adapter));

    }
}
