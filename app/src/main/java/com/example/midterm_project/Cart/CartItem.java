package com.example.midterm_project.Cart;

import com.example.midterm_project.Domain.FoodDomain;

public class CartItem {
    FoodDomain food;
    int quantity;

    public CartItem(FoodDomain food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public FoodDomain getFood() {
        return food;
    }

    public void setFood(FoodDomain food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
