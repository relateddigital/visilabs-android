package com.visilabs.android.api;

import com.visilabs.android.util.StringUtils;

/**
 * Created by visilabs on 15.12.2016.
 */
public class VisilabsTargetFilter {
    private String mAttribute;
    private String mFilterType;
    private String mValue;


    public VisilabsTargetFilter(){

    }

    public VisilabsTargetFilter(String mAttribute, String mFilterType, String mValue) {
        this.mAttribute = mAttribute;
        this.mFilterType = mFilterType;
        this.mValue = mValue;
    }

    public String getAttribute() {
        return mAttribute;
    }

    public void setAttribute(String mAttribute) {
        this.mAttribute = mAttribute;
    }

    public String getFilterType() {
        return mFilterType;
    }

    public void setFilterType(String mFilterType) {
        this.mFilterType = mFilterType;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }
}
