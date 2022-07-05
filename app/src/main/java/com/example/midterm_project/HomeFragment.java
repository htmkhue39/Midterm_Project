package com.example.midterm_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";

    private DatabaseReference mDatabase;

    CategoryAdapter categoriesAdapter;
    FoodAdapter foodsAdapter;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;

    TextView sl;
    ImageView bt_cart;
    EditText searchEditText;

    ArrayList<FoodDomain> foods = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        bt_cart=root.findViewById(R.id.cart);
        searchEditText = root.findViewById(R.id.searchEditText);
        recyclerViewCategoryList = root.findViewById(R.id.recyclerView);
        recyclerViewPopularList = root.findViewById(R.id.recyclerView2);
        sl = root.findViewById(R.id.notifcation_textview);

        Cart.initCart(quantity -> {
            if (quantity == 0) {
                sl.setVisibility(View.INVISIBLE);
            } else {
                sl.setVisibility(View.VISIBLE);
                sl.setText(String.valueOf(quantity));
            }
        });

        bt_cart.setOnClickListener(view -> startActivity(new Intent(getContext(), CartListActivity.class)));

        recyclerViewCategory();
        recyclerViewPopular();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                foodsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

        categoriesAdapter =new CategoryAdapter(category, item -> showFoodsInCategory(item.getTitle().toLowerCase()));
        recyclerViewCategoryList.setAdapter(categoriesAdapter);
    }

    private void recyclerViewPopular() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false){
            public boolean canScrollVertically() {
                return true;
            }
        };
        recyclerViewPopularList.setLayoutManager(gridLayoutManager);

        foodsAdapter = new FoodAdapter(getContext(), foods);
        recyclerViewPopularList.setAdapter(foodsAdapter);

        showFoodsInCategory("all");
    }

    private void showFoodsInCategory(String category) {
        DatabaseReference ref = mDatabase.child("foods");

        if (!category.equals("all"))
            ref = ref.child(category);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();

                if (!category.equals("all")) {
                    for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                        FoodDomain foodDomain = foodSnapshot.getValue(FoodDomain.class);
                        foods.add(foodDomain);
                    }
                } else {
                    for (DataSnapshot category : snapshot.getChildren()) {
                        for (DataSnapshot food : category.getChildren()) {
                            FoodDomain foodDomain = food.getValue(FoodDomain.class);
                            foods.add(foodDomain);
                        }
                    }
                }

                foodsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

