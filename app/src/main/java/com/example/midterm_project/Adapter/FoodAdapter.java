package com.example.midterm_project.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.midterm_project.Cart.Cart;
import com.example.midterm_project.Domain.FoodDomain;
import com.example.midterm_project.FoodDetails;
import com.example.midterm_project.R;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{
    public static final String TAG = "FoodAdapter";

    Context context;
    ArrayList<FoodDomain> popularFood;

    public FoodAdapter(ArrayList<FoodDomain> popularFood){
        this.popularFood = popularFood;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FoodDomain food = popularFood.get(position);

        holder.title.setText(food.getName());
        holder.price.setText(String.valueOf(food.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(food.getImage())
                .into(holder.pic);

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(holder.itemView.getContext(), FoodDetails.class);
            i.putExtra("object", food);

            holder.itemView.getContext().startActivity(i);
        });

        holder.button_add.setOnClickListener(view -> {
            Log.d(TAG, "Add item " + food.getName());

            Cart.increaseItem(food, 1);
        });
    }

    @Override
    public int getItemCount() {
        return popularFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, price, button_add;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.item_title);
            price=itemView.findViewById(R.id.price);
            pic=itemView.findViewById(R.id.pic);
            button_add=itemView.findViewById(R.id.button_add);
        }
    }
}
