package com.visilabs.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VisilabsProduct implements Serializable {
    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("img")
    private String img;

    @SerializedName("dest_url")
    private String destUrl;

    @SerializedName("brand")
    private String brand;

    @SerializedName("price")
    private Double price;

    @SerializedName("dprice")
    private Double dprice;

    @SerializedName("cur")
    private String cur;

    @SerializedName("dcur")
    private String dcur;

    @SerializedName("freeshipping")
    private Boolean freeshipping;

    @SerializedName("samedayshipping")
    private Boolean samedayshipping;

    @SerializedName("rating")
    private Integer rating;

    @SerializedName("comment")
    private Integer comment;

    @SerializedName("discount")
    private Double discount;

    @SerializedName("attr1")
    private String attr1;

    @SerializedName("attr2")
    private String attr2;

    @SerializedName("attr3")
    private String attr3;

    @SerializedName("attr4")
    private String attr4;

    @SerializedName("attr5")
    private String attr5;

    @SerializedName("attr6")
    private String attr6;

    @SerializedName("attr7")
    private String attr7;

    @SerializedName("attr8")
    private String attr8;

    @SerializedName("attr9")
    private String attr9;

    @SerializedName("attr10")
    private String attr10;

    @SerializedName("qs")
    private String qs;

    @SerializedName("variants2")
    private List<ProductVariant2> variants2;

    public VisilabsProduct() {}

    public static VisilabsProduct fromJsonObject(com.visilabs.json.JSONObject jsonObject) {
        if (jsonObject == null) return null;
        VisilabsProduct product = new VisilabsProduct();
        product.setCode(jsonObject.optString("code", null));
        product.setTitle(jsonObject.optString("title", null));
        product.setImg(jsonObject.optString("img", null));
        product.setDestUrl(jsonObject.optString("dest_url", null));
        product.setBrand(jsonObject.optString("brand", null));
        product.setPrice(jsonObject.has("price") ? jsonObject.optDouble("price", 0.0) : null);
        product.setDprice(jsonObject.has("dprice") ? jsonObject.optDouble("dprice", 0.0) : null);
        product.setCur(jsonObject.optString("cur", null));
        product.setDcur(jsonObject.optString("dcur", null));
        product.setFreeshipping(jsonObject.has("freeshipping") ? jsonObject.optBoolean("freeshipping", false) : null);
        product.setSamedayshipping(jsonObject.has("samedayshipping") ? jsonObject.optBoolean("samedayshipping", false) : null);
        product.setRating(jsonObject.has("rating") ? jsonObject.optInt("rating", 0) : null);
        product.setComment(jsonObject.has("comment") ? jsonObject.optInt("comment", 0) : null);
        product.setDiscount(jsonObject.has("discount") ? jsonObject.optDouble("discount", 0.0) : null);
        product.setAttr1(jsonObject.optString("attr1", null));
        product.setAttr2(jsonObject.optString("attr2", null));
        product.setAttr3(jsonObject.optString("attr3", null));
        product.setAttr4(jsonObject.optString("attr4", null));
        product.setAttr5(jsonObject.optString("attr5", null));
        product.setAttr6(jsonObject.optString("attr6", null));
        product.setAttr7(jsonObject.optString("attr7", null));
        product.setAttr8(jsonObject.optString("attr8", null));
        product.setAttr9(jsonObject.optString("attr9", null));
        product.setAttr10(jsonObject.optString("attr10", null));
        product.setQs(jsonObject.optString("qs", null));

        if (jsonObject.has("variants2")) {
            com.visilabs.json.JSONArray variantsArray = jsonObject.optJSONArray("variants2");
            if (variantsArray != null) {
                List<ProductVariant2> variantsList = new ArrayList<>();
                for (int i = 0; i < variantsArray.length(); i++) {
                    com.visilabs.json.JSONObject variantObj = variantsArray.optJSONObject(i);
                    if (variantObj != null) {
                        ProductVariant2 v = ProductVariant2.fromJsonObject(variantObj);
                        if (v != null) {
                            variantsList.add(v);
                        }
                    }
                }
                product.setVariants2(variantsList);
            }
        }
        return product;
    }

    public static VisilabsProduct fromJsonObject(org.json.JSONObject jsonObject) {
        if (jsonObject == null) return null;
        VisilabsProduct product = new VisilabsProduct();
        product.setCode(jsonObject.optString("code", null));
        product.setTitle(jsonObject.optString("title", null));
        product.setImg(jsonObject.optString("img", null));
        product.setDestUrl(jsonObject.optString("dest_url", null));
        product.setBrand(jsonObject.optString("brand", null));
        product.setPrice(jsonObject.has("price") ? jsonObject.optDouble("price", 0.0) : null);
        product.setDprice(jsonObject.has("dprice") ? jsonObject.optDouble("dprice", 0.0) : null);
        product.setCur(jsonObject.optString("cur", null));
        product.setDcur(jsonObject.optString("dcur", null));
        product.setFreeshipping(jsonObject.has("freeshipping") ? jsonObject.optBoolean("freeshipping", false) : null);
        product.setSamedayshipping(jsonObject.has("samedayshipping") ? jsonObject.optBoolean("samedayshipping", false) : null);
        product.setRating(jsonObject.has("rating") ? jsonObject.optInt("rating", 0) : null);
        product.setComment(jsonObject.has("comment") ? jsonObject.optInt("comment", 0) : null);
        product.setDiscount(jsonObject.has("discount") ? jsonObject.optDouble("discount", 0.0) : null);
        product.setAttr1(jsonObject.optString("attr1", null));
        product.setAttr2(jsonObject.optString("attr2", null));
        product.setAttr3(jsonObject.optString("attr3", null));
        product.setAttr4(jsonObject.optString("attr4", null));
        product.setAttr5(jsonObject.optString("attr5", null));
        product.setAttr6(jsonObject.optString("attr6", null));
        product.setAttr7(jsonObject.optString("attr7", null));
        product.setAttr8(jsonObject.optString("attr8", null));
        product.setAttr9(jsonObject.optString("attr9", null));
        product.setAttr10(jsonObject.optString("attr10", null));
        product.setQs(jsonObject.optString("qs", null));

        if (jsonObject.has("variants2")) {
            org.json.JSONArray variantsArray = jsonObject.optJSONArray("variants2");
            if (variantsArray != null) {
                List<ProductVariant2> variantsList = new ArrayList<>();
                for (int i = 0; i < variantsArray.length(); i++) {
                    org.json.JSONObject variantObj = variantsArray.optJSONObject(i);
                    if (variantObj != null) {
                        ProductVariant2 v = ProductVariant2.fromJsonObject(variantObj);
                        if (v != null) {
                            variantsList.add(v);
                        }
                    }
                }
                product.setVariants2(variantsList);
            }
        }
        return product;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public String getDestUrl() { return destUrl; }
    public void setDestUrl(String destUrl) { this.destUrl = destUrl; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getDprice() { return dprice; }
    public void setDprice(Double dprice) { this.dprice = dprice; }

    public String getCur() { return cur; }
    public void setCur(String cur) { this.cur = cur; }

    public String getDcur() { return dcur; }
    public void setDcur(String dcur) { this.dcur = dcur; }

    public Boolean getFreeshipping() { return freeshipping; }
    public void setFreeshipping(Boolean freeshipping) { this.freeshipping = freeshipping; }

    public Boolean getSamedayshipping() { return samedayshipping; }
    public void setSamedayshipping(Boolean samedayshipping) { this.samedayshipping = samedayshipping; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Integer getComment() { return comment; }
    public void setComment(Integer comment) { this.comment = comment; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public String getAttr1() { return attr1; }
    public void setAttr1(String attr1) { this.attr1 = attr1; }

    public String getAttr2() { return attr2; }
    public void setAttr2(String attr2) { this.attr2 = attr2; }

    public String getAttr3() { return attr3; }
    public void setAttr3(String attr3) { this.attr3 = attr3; }

    public String getAttr4() { return attr4; }
    public void setAttr4(String attr4) { this.attr4 = attr4; }

    public String getAttr5() { return attr5; }
    public void setAttr5(String attr5) { this.attr5 = attr5; }

    public String getAttr6() { return attr6; }
    public void setAttr6(String attr6) { this.attr6 = attr6; }

    public String getAttr7() { return attr7; }
    public void setAttr7(String attr7) { this.attr7 = attr7; }

    public String getAttr8() { return attr8; }
    public void setAttr8(String attr8) { this.attr8 = attr8; }

    public String getAttr9() { return attr9; }
    public void setAttr9(String attr9) { this.attr9 = attr9; }

    public String getAttr10() { return attr10; }
    public void setAttr10(String attr10) { this.attr10 = attr10; }

    public String getQs() { return qs; }
    public void setQs(String qs) { this.qs = qs; }

    public List<ProductVariant2> getVariants2() { return variants2; }
    public void setVariants2(List<ProductVariant2> variants2) { this.variants2 = variants2; }
}
