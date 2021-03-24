package com.visilabs.mailSub;

import java.io.Serializable;

public class Report implements Serializable {
    public String impression;
    public String click;

    public void setClick(String click) {
        this.click = click;
    }

    public String getClick() {
        return click;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getImpression() {
        return impression;
    }
}
