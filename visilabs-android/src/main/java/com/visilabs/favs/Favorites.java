package com.visilabs.favs;


import java.util.Arrays;

public class Favorites {
    private String[] category;

    private String[] title;

    private String[] brand;

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String[] getBrand() {
        return brand;
    }

    public void setBrand(String[] brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "ClassPojo [category = " + Arrays.toString(category) + ", title = " + Arrays.toString(title) + ", brand = " + Arrays.toString(brand) + "]";
    }
}

