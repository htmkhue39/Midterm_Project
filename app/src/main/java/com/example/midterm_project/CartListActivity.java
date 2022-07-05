package com.example.midterm_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.midterm_project.Adapter.CartAdapter;
import com.example.midterm_project.Domain.Cart;

public class CartListActivity extends AppCompatActivity {
    RecyclerView cartRecyclerView;
    TextView itemTotal, fee, total;
    ImageView bt_back;
    Button checkOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        cartRecyclerView = findViewById(R.id.cart_list_view);
        bt_back=findViewById(R.id.back2);
        fee=findViewById(R.id.fee);
        total=findViewById(R.id.total);
        checkOut=findViewById(R.id.checkout);

        CartAdapter cartAdapter = new CartAdapter();
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter.notifyItemRangeInserted(0, Cart.getCartSize());

        itemTotal = findViewById(R.id.itemTotal);
        itemTotal.setText("$" + Cart.getTotal());
        fee.setText("$" + String.valueOf(5.5));

        Double temp = Cart.getTotal() + 5.5;
        total.setText("$"+ temp);

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

        checkOut.setOnClickListener(view -> {
            finish();
        });
    }
}