/*
package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

import org.jetbrains.annotations.NotNull;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_display_product_name, txt_display_product_price, txt_display_product_description;
    public ImageView iv_display_product_image;
    public ItemClickListener listener;
    public ProductViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        txt_display_product_name = itemView.findViewById(R.id.txt_display_product_name);
        txt_display_product_price = itemView.findViewById(R.id.txt_display_product_price);
        txt_display_product_description = itemView.findViewById(R.id.txt_display_product_description);
        iv_display_product_image = itemView.findViewById(R.id.iv_display_product_image);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);

    }
}
*/
