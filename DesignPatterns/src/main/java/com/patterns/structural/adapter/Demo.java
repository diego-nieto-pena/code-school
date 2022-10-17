package com.patterns.structural.adapter;
public class Demo {
    public static void main(String[] args) {
        // Round fits round, not surprise
        RoundHole hole = new RoundHole(5);
        RoundPeg rpeg = new RoundPeg(5);
        if(hole.fits(rpeg)) {
            System.out.println("Round peg r5 fits round hole r5.");
        }

        SquarePeg smallSqPeg = new SquarePeg(2);
        SquarePeg largeSqPeg = new SquarePeg(20);
        // hole.fits(smallSqPeg); // Won't compile.

        // Adapter solves the problem
        SquarePegAdapter smallPegAdapter = new SquarePegAdapter(smallSqPeg);
        SquarePegAdapter largePegAdapter = new SquarePegAdapter(largeSqPeg);

        if(hole.fits(smallPegAdapter)) {
            System.out.println("Square peg w2 fits round hole r5.");
        }

        if(!hole.fits(largePegAdapter)) {
            System.out.println("Square peg w20 does not fit into round hole r5.");
        }
    }
}
