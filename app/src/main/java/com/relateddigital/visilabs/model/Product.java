package com.relateddigital.visilabs.model;

public class Product {
    public String name;
    public String url;
    public String imageUrl;
    public String brandName;
    public double price;
    public double discountPrice;
    public String code;
    public String currency;
    public String discountCurrency;

    public Product(String name, String url, String imageUrl, String brandName, double price, double discountPrice, String code, String currency, String discountCurrency) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.brandName = brandName;
        this.price = price;
        this.discountPrice = discountPrice;
        this.code = code;
        this.currency = currency;
        this.discountCurrency = discountCurrency;
    }

}