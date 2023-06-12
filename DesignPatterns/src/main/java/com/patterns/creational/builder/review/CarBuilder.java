package com.patterns.creational.builder.review;
public interface CarBuilder {

    void setSeats(int seats);

    void setBrand(String brand);

    void setColor(String color);

    Car build();
}
