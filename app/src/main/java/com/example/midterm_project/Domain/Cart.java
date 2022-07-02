package com.example.midterm_project.Domain;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart {
    public static final String TAG = "Cart";

    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;

    static ArrayList<CartItem> cart = new ArrayList<>();

    static public void initCart() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("carts").child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> foodNames = new ArrayList<>();
                ArrayList<Long> quantities = new ArrayList<>();

                DataSnapshot dataSnapshot = task.getResult();

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    foodNames.add(item.getKey());
                    quantities.add((Long) item.getValue());
                }

                getRealCart(foodNames, quantities);
            }
        });
    }

    public static void getRealCart(ArrayList<String> foodNames, ArrayList<Long> quantities) {
        mDatabase.child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cart.clear();

                for (DataSnapshot category : snapshot.getChildren()) {
                    for (DataSnapshot item : category.getChildren()) {
                        FoodDomain food = item.getValue(FoodDomain.class);

                        int i = foodNames.indexOf(food.getName());
                        if (i > -1) cart.add(new CartItem(food, quantities.get(i)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    static public boolean increaseItem(FoodDomain food, long quantity) {
        for (CartItem item : cart)
            if (food.getName().equals(item.getFood().getName())) {
                item.increaseQuantity(quantity);
                mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).setValue(item.getQuantity());

                return false;
            }

        cart.add(new CartItem(food, quantity));
        mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).setValue(quantity);

        return true;
    }

    static public boolean decreaseItem(FoodDomain food, int quantity) {
        for (int i = 0; i < cart.size(); ++i)
            if (food.getName().equals(cart.get(i).getFood().getName())) {
                if (cart.get(i).getQuantity() <= quantity) {
                    mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).removeValue();
                    cart.remove(i);

                    return true;
                } else {
                    cart.get(i).decreaseQuantity(quantity);
                    mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).setValue(cart.get(i).getQuantity());

                    return false;
                }
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
}
