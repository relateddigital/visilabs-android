package com.visilabs.android.util;

import java.util.List;

public class VisilabsParameter {
    private String mKey;//OM.OSS
    private String mStoreKey;//OM.voss
    private int mCount;
    private List<String> mRelatedKeys;

    public VisilabsParameter(String key, String storeKey, int count, List<String> relatedKeys) {
        mKey = key;
        mStoreKey = storeKey;
        mCount = count;
        mRelatedKeys =relatedKeys;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getStoreKey() {
        return mStoreKey;
    }

    public void setStoreKey(String storeKey) {
        mStoreKey = storeKey;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public List<String> getRelatedKeys() {
        return mRelatedKeys;
    }

    public void setRelatedKeys(List<String> relatedKeys) {
        mRelatedKeys = relatedKeys;
    }

}
