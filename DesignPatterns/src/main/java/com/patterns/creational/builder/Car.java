package com.patterns.creational.builder;

import lombok.ToString;

@ToString
public class Car {
    private int seats;

    private String brand;

    private String plates;
    public Car(int seats, String brand, String plates) {
        this.seats = seats;
        this.brand = brand;
        this.plates = plates;
    }

}
