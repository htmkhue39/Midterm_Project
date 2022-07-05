package com.example.midterm_project.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.midterm_project.Domain.Cart;
import com.example.midterm_project.Domain.CartItem;
import com.example.midterm_project.Domain.FoodDomain;
import com.example.midterm_project.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public static final String TAG = "CartAdapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        return new ViewHolder(inflater.inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = Cart.getItemAtPosition(position);
        FoodDomain food = item.getFood();

        Glide.with(holder.itemView.getContext())
                .load(food.getImage())
                .into(holder.image);

        holder.title.setText(food.getName());
        holder.price.setText(String.valueOf(food.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        holder.plus.setOnClickListener(view -> {
            Cart.increaseItem(food, 1, () -> notifyDataSetChanged());
        });

        holder.minus.setOnClickListener(view -> {
            if (Cart.decreaseItem(food, 1, () -> notifyDataSetChanged())) {
                Log.d(TAG, "Remove item " + position);
            } else {
                Log.d(TAG, "Decrease item " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Cart.getCartSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, price, quantity, plus, minus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView3);
            title = itemView.findViewById(R.id.textView);
            price = itemView.findViewById(R.id.textView2);
            quantity = itemView.findViewById(R.id.textView5);
            minus = itemView.findViewById(R.id.textView3);
            plus = itemView.findViewById(R.id.textView6);
        }
    }
}
