package com.patterns.creational.builder.review;
public class Car {

    private int seats;

    private String brand;

    private String color;

    public Car(int seats, String brand, String color) {
        this.seats = seats;
        this.brand = brand;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Car{" +
                "seats=" + seats +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
