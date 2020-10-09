package com.visilabs.story.model;

import java.io.Serializable;

public class Report implements Serializable {
    public String impression;
    public String click;

    public String getClick() {
        return click;
    }

    public String getImpression() {
        return impression;
    }
}
