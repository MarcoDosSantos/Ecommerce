package com.example.ecommerce.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private String name, description, category, image, pid, price;

    public Product() {
    }

    public Product(String name, String description, String category, String image, String pid, String price) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.pid = pid;
        this.price = price;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
