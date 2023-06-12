package com.patterns.creational.builder;
public class Director {

    public void buildCar(Builder builder) {
        builder.setBrand("Ferrari");
        builder.setPlates("123XXX");
        builder.setSeats(4);
    }
}
