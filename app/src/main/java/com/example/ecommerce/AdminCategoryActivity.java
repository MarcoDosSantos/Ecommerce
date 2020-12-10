package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView iv_t_shirts, iv_sports_t_shirts, iv_dresses, iv_sweathers,
            iv_glasses, iv_purses_bags_wallets, iv_hats_caps, iv_shoes,
            iv_headphones, iv_laptops, iv_watches, iv_mobile_phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        iv_t_shirts = findViewById(R.id.iv_t_shirts);
        iv_sports_t_shirts = findViewById(R.id.iv_sports_t_shirts);
        iv_dresses = findViewById(R.id.iv_dresses);
        iv_sweathers = findViewById(R.id.iv_sweathers);
        iv_glasses = findViewById(R.id.iv_glasses);
        iv_purses_bags_wallets = findViewById(R.id.iv_purses_bags_wallets);
        iv_hats_caps = findViewById(R.id.iv_hats_caps);
        iv_shoes = findViewById(R.id.iv_shoes);
        iv_headphones = findViewById(R.id.iv_headphones);
        iv_laptops = findViewById(R.id.iv_laptops);
        iv_watches = findViewById(R.id.iv_watches);
        iv_mobile_phones = findViewById(R.id.iv_mobile_phones);

        iv_t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Tshirts");
                startActivity(intent);
            }
        });

        iv_sports_t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "SportsTShirts");
                startActivity(intent);
            }
        });

        iv_dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Dresses");
                startActivity(intent);
            }
        });

        iv_sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Sweathers");
                startActivity(intent);
            }
        });

        iv_glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Glasses");
                startActivity(intent);
            }
        });

        iv_purses_bags_wallets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Purses, bags, wallets");
                startActivity(intent);
            }
        });

        iv_hats_caps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Hats, caps");
                startActivity(intent);
            }
        });

        iv_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Shoes");
                startActivity(intent);
            }
        });

        iv_headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Headphones, handfrees");
                startActivity(intent);
            }
        });

        iv_laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Laptops");
                startActivity(intent);
            }
        });

        iv_watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Watches");
                startActivity(intent);
            }
        });

        iv_mobile_phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("Category", "Mobile phones");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminCategoryActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}