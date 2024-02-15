package com.relateddigital.visilabs.model;

import java.util.List;

public class Category {
    public String title;
    public List<Product> products;

    public Category(String title, List<Product> products) {
        this.title = title;
        this.products = products;
    }
}
