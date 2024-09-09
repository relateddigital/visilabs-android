package com.visilabs.inApp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ProductStatNotifierActionData implements Serializable {

    @SerializedName("content")
    private String content;
    @SerializedName("width")
    private Integer width;
    @SerializedName("height")
    private Integer height;
    @SerializedName("timeout")
    private String timeout;
    @SerializedName("pos")
    private String pos;
    @SerializedName("bgcolor")
    private String bgcolor;
    @SerializedName("threshold")
    private Integer threshold;
    @SerializedName("showclosebtn")
    private Boolean showclosebtn;
    @SerializedName("waiting_time")
    private Integer waitingTime;
    @SerializedName("eventtype")
    private String eventtype;
    @SerializedName("ExtendedProps")
    private String extendedProps;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Boolean getShowclosebtn() {
        return showclosebtn;
    }

    public void setShowclosebtn(Boolean showclosebtn) {
        this.showclosebtn = showclosebtn;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    public String getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }

}
