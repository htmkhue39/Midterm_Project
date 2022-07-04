package com.example.midterm_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_project.Adapter.CategoryAdapter;
import com.example.midterm_project.Adapter.FoodAdapter;
import com.example.midterm_project.Domain.Cart;
import com.example.midterm_project.Domain.CategoryDomain;
import com.example.midterm_project.Domain.FoodDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";

    private DatabaseReference mDatabase;
    private RecyclerView.Adapter adapter, foodsAdapter;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    ImageView bt_cart;

    ArrayList<FoodDomain> foods = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        bt_cart=root.findViewById(R.id.cart);
        recyclerViewCategoryList = root.findViewById(R.id.recyclerView);
        recyclerViewPopularList = root.findViewById(R.id.recyclerView2);

        Cart.initCart();

        bt_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CartListActivity.class));
            }
        });

        recyclerViewCategory();
        recyclerViewPopular();

        return root;
    }

    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain("Pizza",R.drawable.cat_1));
        category.add(new CategoryDomain("Burger",R.drawable.cat_2));
        category.add(new CategoryDomain("Hotdog",R.drawable.cat_3));
        category.add(new CategoryDomain("Drink",R.drawable.cat_4));
        category.add(new CategoryDomain("All",R.drawable.all));

        adapter=new CategoryAdapter(category, item -> showFoodsInCategory(item.getTitle().toLowerCase()));
        recyclerViewCategoryList.setAdapter(adapter);
    }

    private void recyclerViewPopular() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false){
            public boolean canScrollVertically() {
                return true;
            }
        };
        recyclerViewPopularList.setLayoutManager(gridLayoutManager);

        foodsAdapter = new FoodAdapter(foods);
        recyclerViewPopularList.setAdapter(foodsAdapter);

        showFoodsInCategory("all");
    }

    private void showFoodsInCategory(String category) {
        if (!category.equals("all")) {
            mDatabase.child("foods").child(category).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    foodsAdapter.notifyItemRangeRemoved(0, foods.size());
                    foods.clear();

                    for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                            FoodDomain foodDomain = foodSnapshot.getValue(FoodDomain.class);

                            foods.add(foodDomain);
                            foodsAdapter.notifyItemInserted(foods.size() - 1);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Get foods failed", error.toException());
                }
            });
        } else {
            mDatabase.child("foods").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    foodsAdapter.notifyItemRangeRemoved(0, foods.size());
                    foods.clear();

                    for (DataSnapshot category : snapshot.getChildren()) {
                        for (DataSnapshot food : category.getChildren()) {
                            FoodDomain foodDomain = food.getValue(FoodDomain.class);

                            foods.add(foodDomain);
                            foodsAdapter.notifyItemInserted(foods.size() - 1);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Get foods failed", error.toException());
                }
            });
        }
    }
}

