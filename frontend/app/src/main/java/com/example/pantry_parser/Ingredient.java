package com.example.pantry_parser;

public class Ingredient {

    private String name;
    private Double quantity;

    public Ingredient(String name, Double quantity) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
