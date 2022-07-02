package com.example.midterm_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.midterm_project.Adapter.CartAdapter;
import com.example.midterm_project.Cart.Cart;

public class CartListActivity extends AppCompatActivity {
    RecyclerView cartRecyclerView;
    TextView itemTotal;
    ImageView bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        cartRecyclerView = findViewById(R.id.cart_list_view);
        itemTotal = findViewById(R.id.itemTotal);
        bt_back=findViewById(R.id.back2);

        CartAdapter cartAdapter = new CartAdapter();
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter.notifyItemRangeInserted(0, Cart.getCartSize());

        cartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                itemTotal.setText("$" + Cart.getTotal());
            }
        });

        bt_back.setOnClickListener(view -> {
            finish();
        });
    }
}