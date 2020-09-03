package com.example.whattomake3;

public class Ingredient {
    private String ingName;
    private String quantity;

    public Ingredient() {
        ingName = "";
        quantity = "";
    }

    public Ingredient(String ingName, String quantity) {
        this.ingName = ingName;
        this.quantity = quantity;
    }

    public String getIngName() {
        return ingName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return (ingName + "\t" + quantity);
    }
}
