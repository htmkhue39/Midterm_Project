package com.example.midterm_project;

import android.content.Intent;
import android.os.Bundle;
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
    private DatabaseReference mDatabase;
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    ImageView bt_cart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment, container, false);

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
        category.add(new CategoryDomain("Pizza","cat_1"));
        category.add(new CategoryDomain("Burger","cat_2"));
        category.add(new CategoryDomain("Hotdog","cat_3"));
        category.add(new CategoryDomain("Drink","cat_4"));
        category.add(new CategoryDomain("All",""));

        adapter=new CategoryAdapter(category);
        recyclerViewCategoryList.setAdapter(adapter);
    }

    private void recyclerViewPopular() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false){
            public boolean canScrollVertically() {
                return true;
            }
        };
        recyclerViewPopularList.setLayoutManager(gridLayoutManager);

        ArrayList<FoodDomain> foodDomainList = new ArrayList<>();

        adapter2 = new FoodAdapter(foodDomainList);
        recyclerViewPopularList.setAdapter(adapter2);

        mDatabase.child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter2.notifyItemRangeRemoved(0, foodDomainList.size());
                foodDomainList.clear();

                for (DataSnapshot category : snapshot.getChildren()) {
                    for (DataSnapshot food : category.getChildren()) {
                        FoodDomain foodDomain = food.getValue(FoodDomain.class);

                        foodDomainList.add(foodDomain);
                        adapter2.notifyItemInserted(foodDomainList.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

