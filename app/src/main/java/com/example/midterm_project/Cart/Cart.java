package com.example.midterm_project.Cart;

import android.util.Log;

import com.example.midterm_project.Domain.FoodDomain;

import java.util.ArrayList;

public class Cart {
    public static final String TAG = "Cart";

    static ArrayList<CartItem> cart = new ArrayList<>();

    static public void getCartFromFirebase() {

    }

    static public boolean increaseItem(FoodDomain food, int quantity) {
        for (CartItem item : cart)
            if (food.getName().equals(item.getFood().getName())) {
                item.increaseQuantity(quantity);
                return false;
            }

        cart.add(new CartItem(food, quantity));
        return true;
    }

    static public boolean decreaseItem(FoodDomain food, int quantity) {
        for (int i = 0; i < cart.size(); ++i)
            if (food.getName().equals(cart.get(i).getFood().getName())) {
                cart.get(i).decreaseQuantity(quantity);

                if (cart.get(i).quantity <= 0) {
                    cart.remove(i);
                    return true;
                }

                return false;
            }

        return false;
    }

    static public int getCartSize() {
        return cart.size();
    }

    static public CartItem getItemAtPosition(int position) {
        return cart.get(position);
    }

    static public double getTotal() {
        double total = 0;
        for (CartItem item : cart)
            total += item.getFood().getPrice() * item.getQuantity();
        return total;
    }

    static public void printCart() {
        for (CartItem item : cart) {
            Log.d(TAG, item.getFood().getName() + ' ' + item.getQuantity());
        }
    }
}
