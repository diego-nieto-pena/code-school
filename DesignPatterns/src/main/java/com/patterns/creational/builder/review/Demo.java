package com.patterns.creational.builder.review;
public class Demo {
    public static void main(String[] args) {
        Director dir = new Director();
        CarBuilder carBuilder = new RegularCarBuilder();
        dir.buildRegularCar(carBuilder);
        System.out.println(carBuilder.build());
    }
}
