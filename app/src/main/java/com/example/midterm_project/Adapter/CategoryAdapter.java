package com.example.midterm_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.midterm_project.Domain.CategoryDomain;
import com.example.midterm_project.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(CategoryDomain item);
    }

    ArrayList<CategoryDomain>categoryDomains;
    OnItemClickListener listener;

    public CategoryAdapter(ArrayList<CategoryDomain> categoryDomains, OnItemClickListener listener) {
        this.categoryDomains = categoryDomains;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryDomain category = categoryDomains.get(position);

        holder.categoryName.setText(category.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(category.getPic())
                .into(holder.categoryImage);

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;
        ImageView categoryImage;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryImage=itemView.findViewById(R.id.category_pic);
            mainLayout=itemView.findViewById(R.id.mainLayout);
        }
    }
}
