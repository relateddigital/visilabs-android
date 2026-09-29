package com.visilabs.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ProductVariant2Color implements Serializable {
    @SerializedName("size")
    private String size;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("cart_id")
    private String cartId;

    @SerializedName("stock")
    private Integer stock;

    public ProductVariant2Color() {}

    public ProductVariant2Color(String size, Integer productId, String cartId, Integer stock) {
        this.size = size;
        this.productId = productId;
        this.cartId = cartId;
        this.stock = stock;
    }

    public static ProductVariant2Color fromJsonObject(com.visilabs.json.JSONObject jsonObject) {
        if (jsonObject == null) return null;
        ProductVariant2Color color = new ProductVariant2Color();
        if (jsonObject.has("size")) {
            color.setSize(jsonObject.optString("size", null));
        }

        if (jsonObject.has("product_id")) {
            try {
                color.setProductId(jsonObject.getInt("product_id"));
            } catch (Exception e) {
                try {
                    color.setProductId(Integer.parseInt(jsonObject.getString("product_id")));
                } catch (Exception ignored) {}
            }
        }

        if (jsonObject.has("cart_id")) {
            color.setCartId(jsonObject.optString("cart_id", null));
        }

        if (jsonObject.has("stock")) {
            try {
                color.setStock(jsonObject.getInt("stock"));
            } catch (Exception e) {
                try {
                    color.setStock(Integer.parseInt(jsonObject.getString("stock")));
                } catch (Exception ignored) {}
            }
        }
        return color;
    }

    public static ProductVariant2Color fromJsonObject(org.json.JSONObject jsonObject) {
        if (jsonObject == null) return null;
        ProductVariant2Color color = new ProductVariant2Color();
        if (jsonObject.has("size")) {
            color.setSize(jsonObject.optString("size", null));
        }

        if (jsonObject.has("product_id")) {
            try {
                color.setProductId(jsonObject.getInt("product_id"));
            } catch (Exception e) {
                try {
                    color.setProductId(Integer.parseInt(jsonObject.getString("product_id")));
                } catch (Exception ignored) {}
            }
        }

        if (jsonObject.has("cart_id")) {
            color.setCartId(jsonObject.optString("cart_id", null));
        }

        if (jsonObject.has("stock")) {
            try {
                color.setStock(jsonObject.getInt("stock"));
            } catch (Exception e) {
                try {
                    color.setStock(Integer.parseInt(jsonObject.getString("stock")));
                } catch (Exception ignored) {}
            }
        }
        return color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
