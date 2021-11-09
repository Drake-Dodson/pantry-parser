package com.example.pantry_parser;

public class Ingredient {

    private String name;
    private Double quantity;
    private Unit unit;

    /**
     *
     * @param name Name of ingredient
     * @param quantity Quantity of ingredient
     * @param unit  Unit of ingredient
     */
    public Ingredient(String name, Double quantity, Unit unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
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
