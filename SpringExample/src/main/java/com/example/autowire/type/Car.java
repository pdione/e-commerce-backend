package com.example.autowire.type;

public class Car {

    private Specification specification;

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public void diplayDetails() {
        System.out.println("Car Details: " + specification.toString());
    }
}
