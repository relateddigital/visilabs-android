package com.relateddigital.visilabs.model;

import com.visilabs.model.ProductVariant2;
import java.util.List;

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
    public List<ProductVariant2> variants2;

    public Product(String name, String url, String imageUrl, String brandName, double price, double discountPrice, String code, String currency, String discountCurrency) {
        this(name, url, imageUrl, brandName, price, discountPrice, code, currency, discountCurrency, null);
    }

    public Product(String name, String url, String imageUrl, String brandName, double price, double discountPrice, String code, String currency, String discountCurrency, List<ProductVariant2> variants2) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.brandName = brandName;
        this.price = price;
        this.discountPrice = discountPrice;
        this.code = code;
        this.currency = currency;
        this.discountCurrency = discountCurrency;
        this.variants2 = variants2;
    }
}
