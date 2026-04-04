package com.example.setter.injection;

public class Car {

    private  Specification specification;

    public void diplayDetails() {
        System.out.println("Car Details: " + specification.toString());
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }
}
