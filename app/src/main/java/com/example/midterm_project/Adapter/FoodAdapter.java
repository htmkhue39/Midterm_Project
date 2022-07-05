package com.example.midterm_project.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.midterm_project.Domain.Cart;
import com.example.midterm_project.Domain.FoodDomain;
import com.example.midterm_project.FoodDetailsActivity;
import com.example.midterm_project.R;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements Filterable {
    public static final String TAG = "FoodAdapter";

    Context context;
    ArrayList<FoodDomain> foods, foodsFiltered;

    public FoodAdapter(Context context, ArrayList<FoodDomain> foods){
        this.context = context;
        this.foods = foods;
        this.foodsFiltered = foods;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FoodDomain food = foodsFiltered.get(position);

        holder.title.setText(food.getName());
        holder.price.setText(String.valueOf(food.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(food.getImage())
                .into(holder.pic);

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(holder.itemView.getContext(), FoodDetailsActivity.class);
            i.putExtra("object", food);

            holder.itemView.getContext().startActivity(i);
        });

        holder.button_add.setOnClickListener(view -> {
            Log.d(TAG, "Add item " + food.getName());

            Cart.increaseItem(food, 1, () -> {
                Toast.makeText(context, "Added 1 " + food.getName() + " to cart!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return foodsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    foodsFiltered = foods;
                } else {
                    ArrayList<FoodDomain> filteredList = new ArrayList<>();

                    for (FoodDomain row : foods)
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    foodsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = foodsFiltered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                foodsFiltered = (ArrayList<FoodDomain>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
