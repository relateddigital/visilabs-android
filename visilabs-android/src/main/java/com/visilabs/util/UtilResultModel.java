package com.visilabs.util;

import java.util.ArrayList;

public class UtilResultModel {
    private ArrayList<Integer> numbers = new ArrayList<>();
    private ArrayList<Integer> startIdxs = new ArrayList<>();
    private ArrayList<Integer> endIdxs = new ArrayList<>();
    private String message;
    private boolean isTag;

    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    public ArrayList<Integer> getStartIdxs() {
        return startIdxs;
    }

    public ArrayList<Integer> getEndIdxs() {
        return endIdxs;
    }

    public void addNumber(int number) {
        numbers.add(number);
    }

    public void addStartIdx(int startIdx) {
        startIdxs.add(startIdx);
    }

    public void addEndIdx(int endIdx) {
        endIdxs.add(endIdx);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsTag() {
        return isTag;
    }

    public void setIsTag(boolean isTag) {
        this.isTag = isTag;
    }
}
