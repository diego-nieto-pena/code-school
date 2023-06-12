package com.patterns.creational.builder;
public class Demo {
    public static void main(String[] args) {
        Director director = new Director();
        CarBuilder builder = new CarBuilder();
        director.buildCar(builder);

        System.out.println(builder.getResults());
    }
}
