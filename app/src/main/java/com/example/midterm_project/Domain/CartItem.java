package com.example.midterm_project.Domain;

import com.example.midterm_project.Domain.FoodDomain;

public class CartItem {
    FoodDomain food;
    long quantity;

    public CartItem(FoodDomain food, long quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public FoodDomain getFood() {
        return food;
    }

    public void setFood(FoodDomain food) {
        this.food = food;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void increaseQuantity(long quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(long quantity) {
        this.quantity -= quantity;
    }
}
