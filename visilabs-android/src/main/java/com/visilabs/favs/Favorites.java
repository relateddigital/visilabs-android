package com.visilabs.favs;


import java.io.Serializable;
import java.util.Arrays;

public class Favorites implements Serializable {
    private String[] ageGroup;
    private String[] attr1;
    private String[] attr2;
    private String[] attr3;
    private String[] attr4;
    private String[] attr5;
    private String[] attr6;
    private String[] attr7;
    private String[] attr8;
    private String[] attr9;
    private String[] attr10;
    private String[] brand;
    private String[] category;
    private String[] color;
    private String[] gender;
    private String[] material;
    private String[] title;

    public String[] getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String[] ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String[] getAttr1() {
        return attr1;
    }

    public void setAttr1(String[] attr1) {
        this.attr1 = attr1;
    }

    public String[] getAttr2() {
        return attr2;
    }

    public void setAttr2(String[] attr2) {
        this.attr2 = attr2;
    }

    public String[] getAttr3() {
        return attr3;
    }

    public void setAttr3(String[] attr3) {
        this.attr3 = attr3;
    }

    public String[] getAttr4() {
        return attr4;
    }

    public void setAttr4(String[] attr4) {
        this.attr4 = attr4;
    }

    public String[] getAttr5() {
        return attr5;
    }

    public void setAttr5(String[] attr5) {
        this.attr5 = attr5;
    }

    public String[] getAttr6() {
        return attr6;
    }

    public void setAttr6(String[] attr6) {
        this.attr6 = attr6;
    }

    public String[] getAttr7() {
        return attr7;
    }

    public void setAttr7(String[] attr7) {
        this.attr7 = attr7;
    }

    public String[] getAttr8() {
        return attr8;
    }

    public void setAttr8(String[] attr8) {
        this.attr8 = attr8;
    }

    public String[] getAttr9() {
        return attr9;
    }

    public void setAttr9(String[] attr9) {
        this.attr9 = attr9;
    }

    public String[] getAttr10() {
        return attr10;
    }

    public void setAttr10(String[] attr10) {
        this.attr10 = attr10;
    }

    public String[] getBrand() {
        return brand;
    }

    public void setBrand(String[] brand) {
        this.brand = brand;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public String[] getGender() {
        return gender;
    }

    public void setGender(String[] gender) {
        this.gender = gender;
    }

    public String[] getMaterial() {
        return material;
    }

    public void setMaterial(String[] material) {
        this.material = material;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Favorites [" +
                "ageGroup = " + Arrays.toString(ageGroup) +
                "attr1 = " + Arrays.toString(attr1) +
                "attr2 = " + Arrays.toString(attr2) +
                "attr3 = " + Arrays.toString(attr3) +
                "attr4 = " + Arrays.toString(attr4) +
                "attr5 = " + Arrays.toString(attr5) +
                "attr6 = " + Arrays.toString(attr6) +
                "attr7 = " + Arrays.toString(attr7) +
                "attr8 = " + Arrays.toString(attr8) +
                "attr9 = " + Arrays.toString(attr9) +
                "attr10 = " + Arrays.toString(attr10) +
                "brand = " + Arrays.toString(brand) +
                "category = " + Arrays.toString(category) +
                "color = " + Arrays.toString(color) +
                "gender = " + Arrays.toString(gender) +
                "material = " + Arrays.toString(material) +
                ", title = " + Arrays.toString(title) + "]";
    }
}

