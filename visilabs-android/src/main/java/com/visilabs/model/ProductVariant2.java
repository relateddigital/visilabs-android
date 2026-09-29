package com.visilabs.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductVariant2 implements Serializable {
    @SerializedName("color")
    private String color;

    @SerializedName("colors")
    private List<ProductVariant2Color> colors;

    public ProductVariant2() {}

    public ProductVariant2(String color, List<ProductVariant2Color> colors) {
        this.color = color;
        this.colors = colors;
    }

    public static ProductVariant2 fromJsonObject(com.visilabs.json.JSONObject jsonObject) {
        if (jsonObject == null) return null;
        ProductVariant2 variant = new ProductVariant2();
        if (jsonObject.has("color")) {
            variant.setColor(jsonObject.optString("color", null));
        }
        if (jsonObject.has("colors")) {
            com.visilabs.json.JSONArray colorsArray = jsonObject.optJSONArray("colors");
            if (colorsArray != null) {
                List<ProductVariant2Color> list = new ArrayList<>();
                for (int i = 0; i < colorsArray.length(); i++) {
                    com.visilabs.json.JSONObject colorObj = colorsArray.optJSONObject(i);
                    if (colorObj != null) {
                        ProductVariant2Color c = ProductVariant2Color.fromJsonObject(colorObj);
                        if (c != null) {
                            list.add(c);
                        }
                    }
                }
                variant.setColors(list);
            }
        }
        return variant;
    }

    public static ProductVariant2 fromJsonObject(org.json.JSONObject jsonObject) {
        if (jsonObject == null) return null;
        ProductVariant2 variant = new ProductVariant2();
        if (jsonObject.has("color")) {
            variant.setColor(jsonObject.optString("color", null));
        }
        if (jsonObject.has("colors")) {
            org.json.JSONArray colorsArray = jsonObject.optJSONArray("colors");
            if (colorsArray != null) {
                List<ProductVariant2Color> list = new ArrayList<>();
                for (int i = 0; i < colorsArray.length(); i++) {
                    org.json.JSONObject colorObj = colorsArray.optJSONObject(i);
                    if (colorObj != null) {
                        ProductVariant2Color c = ProductVariant2Color.fromJsonObject(colorObj);
                        if (c != null) {
                            list.add(c);
                        }
                    }
                }
                variant.setColors(list);
            }
        }
        return variant;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<ProductVariant2Color> getColors() {
        return colors;
    }

    public void setColors(List<ProductVariant2Color> colors) {
        this.colors = colors;
    }
}
