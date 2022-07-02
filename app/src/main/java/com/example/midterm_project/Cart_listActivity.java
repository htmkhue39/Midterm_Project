package com.example.midterm_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Cart_listActivity extends AppCompatActivity {
    ImageView bt_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        bt_back=findViewById(R.id.back2);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Cart_listActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}