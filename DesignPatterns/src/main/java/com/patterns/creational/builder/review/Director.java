package com.patterns.creational.builder.review;
public class Director {

    public void buildRegularCar(CarBuilder carBuilder) {
        carBuilder.setSeats(4);
        carBuilder.setBrand("Volkswagen");
        carBuilder.setColor("rot");
    }
}
