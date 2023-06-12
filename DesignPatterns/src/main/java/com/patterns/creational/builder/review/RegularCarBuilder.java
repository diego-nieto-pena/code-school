package com.patterns.creational.builder.review;
public class RegularCarBuilder implements CarBuilder {

    private int seats;

    private String brand;

    private String color;

    @Override
    public void setSeats(int seats) {
        this.seats = seats;
    }
    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }
    @Override
    public void setColor(String color) {
        this.color = color;
    }

    public Car build() {
        return new Car(seats, brand, color);
    }
}
