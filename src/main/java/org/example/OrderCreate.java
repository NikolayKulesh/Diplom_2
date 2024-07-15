package org.example;

public class OrderCreate {
    private String[] ingredients;

    public OrderCreate(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public OrderCreate() {
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
