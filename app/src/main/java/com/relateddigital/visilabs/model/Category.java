package com.relateddigital.visilabs.model;

import java.util.List;

public class Category {
    public String name;
    public List<Product> products;

    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }
}
