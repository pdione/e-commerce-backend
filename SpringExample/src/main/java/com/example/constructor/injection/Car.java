package com.example.constructor.injection;

public class Car {

    private final Specification specification;

    public Car(Specification specification) {
        this.specification = specification;
    }

    public void diplayDetails() {
        System.out.println("Car Details: " + specification.toString());
    }
}
