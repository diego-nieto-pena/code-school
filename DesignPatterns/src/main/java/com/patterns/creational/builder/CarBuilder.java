package com.patterns.creational.builder;
public class CarBuilder implements Builder {

    private int seats;

    private String brand;

    private String plates;

    @Override
    public void setSeats(int seats) {
        this.seats = seats;
    }
    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }
    @Override
    public void setPlates(String plates) {
        this.plates = plates;
    }

    public Car getResults() {
        return new Car(seats, brand, plates);
    }
}
