package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProductDetailActivity extends AppCompatActivity {
    private FloatingActionButton fab_detail_add_to_cart;
    private ElegantNumberButton btn_detail_number_product;
    private ImageView iv_detail_product_image;
    private TextView tv_detail_product_name, tv_detail_product_description, tv_detail_product_price;

    private String pid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        pid = getIntent().getStringExtra("pid");

        fab_detail_add_to_cart = findViewById(R.id.fab_detail_add_to_cart);
        btn_detail_number_product = findViewById(R.id.btn_detail_number_product);

        iv_detail_product_image = findViewById(R.id.iv_detail_product_image);
        tv_detail_product_name = findViewById(R.id.tv_detail_product_name);
        tv_detail_product_description = findViewById(R.id.tv_detail_product_description);
        tv_detail_product_price = findViewById(R.id.tv_detail_product_price);

        getProductDetails(pid);


    }

    private void getProductDetails(String pid) {
        DatabaseReference pidRef = FirebaseDatabase.getInstance().getReference().child("Products");
        pidRef.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Product product = snapshot.getValue(Product.class);
                    tv_detail_product_name.setText(product.getName());
                    tv_detail_product_description.setText(product.getDescription());
                    tv_detail_product_price.setText("$ " + product.getPrice());
                    Picasso.get().load(product.getImage()).into(iv_detail_product_image);

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}