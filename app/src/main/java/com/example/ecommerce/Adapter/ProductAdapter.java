
package com.example.ecommerce.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecommerce.Models.Product;
import com.example.ecommerce.ProductDetailActivity;
import com.example.ecommerce.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.zip.Inflater;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements View.OnClickListener {

    ArrayList<Product> products;
    private View.OnClickListener listener;

    public ProductAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_display_product_name, txt_display_product_price, txt_display_product_description;
        ImageView iv_display_product_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_display_product_name = itemView.findViewById(R.id.txt_display_product_name);
            txt_display_product_price = itemView.findViewById(R.id.txt_display_product_price);
            txt_display_product_description = itemView.findViewById(R.id.txt_display_product_description);
            iv_display_product_image = itemView.findViewById(R.id.iv_display_product_image);

        }

    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, null, false);
        view.setOnClickListener(this);
        return new ProductAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.txt_display_product_description.setText(products.get(position).getDescription());
        holder.txt_display_product_name.setText(products.get(position).getName());
        holder.txt_display_product_price.setText("$ " + products.get(position).getPrice());
        Picasso.get().load(products.get(position).getImage()).into(holder.iv_display_product_image);
        String pid = products.get(position).getPid();

    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

