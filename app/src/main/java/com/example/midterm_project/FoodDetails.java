package com.example.midterm_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.midterm_project.Domain.FoodDomain;

public class FoodDetails extends AppCompatActivity {
    ImageView img, back, bt_plus, bt_minus;
    TextView proName, proPrice, proDesc, numberOrderTxt;

    FoodDomain object;
    int numberOrder = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        initView();
        getBundle();
        back = findViewById(R.id.back2);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FoodDetails.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void getBundle() {
        object= (FoodDomain) getIntent().getSerializableExtra("object");

        int drawableResourceId=this.getResources().getIdentifier(object.getPic(), "drawable", this.getPackageName());
        Glide.with(this)
                .load(drawableResourceId)
                .into(img);

        proName.setText(object.getTitle());
        proPrice.setText("$"+object.getPrice());
        proDesc.setText(object.getDescription());
        numberOrderTxt.setText(String.valueOf(numberOrder));

        bt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrder = numberOrder+1;
                numberOrderTxt.setText(String.valueOf(numberOrder));
            }
        });

        bt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberOrder > 1) {
                    numberOrder = numberOrder - 1;
                }
                numberOrderTxt.setText(String.valueOf(numberOrder));
            }
        });
    }

    private void initView() {
        proName = findViewById(R.id.productName);
        proDesc = findViewById(R.id.prodDesc);
        proPrice = findViewById(R.id.prodPrice);
        img = findViewById(R.id.big_image);
        numberOrderTxt = findViewById(R.id.numberOrderTxt);
        bt_plus = findViewById(R.id.bt_plus);
        bt_minus = findViewById(R.id.bt_minus);
    }

}
