package com.example.midterm_project.Domain;

import android.telecom.Call;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    static int totalQuantity;
    static CartWatcher cartWatcher;
    static ArrayList<CartItem> cart = new ArrayList<>();

    public interface CartWatcher {
        void OnCartChanged(int quantity);
    }
    
    public interface Callback {
        void OnFinishFunction();
    }

    static public void initCart(CartWatcher watcher) {
        totalQuantity = 0;
        cartWatcher = watcher;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("carts").child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> foodNames = new ArrayList<>();
                ArrayList<Long> quantities = new ArrayList<>();

                DataSnapshot dataSnapshot = task.getResult();

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    long quantity = (long) item.getValue();

                    foodNames.add(item.getKey());
                    quantities.add(quantity);

                    totalQuantity += quantity;
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

                cartWatcher.OnCartChanged(totalQuantity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    static public boolean increaseItem(FoodDomain food, long quantity, Callback callback) {
        for (CartItem item : cart)
            if (food.getName().equals(item.getFood().getName())) {
                mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).setValue(item.getQuantity()).addOnSuccessListener(unused -> {
                    item.increaseQuantity(quantity);

                    totalQuantity += quantity;
                    cartWatcher.OnCartChanged(totalQuantity);

                    callback.OnFinishFunction();
                });

                return false;
            }

        mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).setValue(quantity).addOnSuccessListener(unused -> {
            cart.add(new CartItem(food, quantity));

            totalQuantity += quantity;
            cartWatcher.OnCartChanged(totalQuantity);

            callback.OnFinishFunction();
        });

        return true;
    }

    static public boolean decreaseItem(FoodDomain food, int quantity, Callback callback) {
        for (int i = 0; i < cart.size(); ++i)
            if (food.getName().equals(cart.get(i).getFood().getName())) {
                int finalI = i;

                if (cart.get(i).getQuantity() <= quantity) {
                    mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            totalQuantity -= cart.get(finalI).getQuantity();
                            cartWatcher.OnCartChanged(totalQuantity);

                            cart.remove(finalI);

                            callback.OnFinishFunction();
                        }
                    });

                    return true;
                } else {
                    mDatabase.child("carts").child(mAuth.getUid()).child(food.getName()).setValue(cart.get(i).getQuantity()).addOnSuccessListener(unused -> {
                        totalQuantity -= quantity;
                        cartWatcher.OnCartChanged(totalQuantity);

                        cart.get(finalI).decreaseQuantity(quantity);

                        callback.OnFinishFunction();
                    });

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
