package com.visilabs.api;


public class VisilabsTargetFilter {
    private String mAttribute;
    private String mFilterType;
    private String mValue;


    public VisilabsTargetFilter(String attribute, String filterType, String value) {
        mAttribute = attribute;
        mFilterType = filterType;
        mValue = value;
    }

    public String getAttribute() {
        return mAttribute;
    }

    public void setAttribute(String attribute) {
        mAttribute = attribute;
    }

    public String getFilterType() {
        return mFilterType;
    }

    public void setFilterType(String filterType) {
        mFilterType = filterType;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
