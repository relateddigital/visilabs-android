package com.visilabs.story.model.skinbased;

import java.io.Serializable;

public class Countdown implements Serializable {

    private String pagePosition;

    private String messageText;

    private String messageTextSize;

    private String messageTextColor;

    private String displayType;

    private String endDateTime;

    private String endAction; //TODO : endAction gidecek yerine bitince cıkacak gif url si gelecek

    public void setPagePosition(String pagePosition) {this.pagePosition = pagePosition;}

    public String getPagePosition() {return pagePosition;}

    public void setMessageText(String messageText) {this.messageText = messageText;}

    public String getMessageText() {return messageText;}

    public void setMessageTextSize(String messageTextSize) {this.messageTextSize = messageTextSize;}

    public String getMessageTextSize() {return messageTextSize;}

    public void setMessageTextColor(String messageTextColor) {this.messageTextColor = messageTextColor;}

    public String getMessageTextColor() {return messageTextColor;}

    public void setDisplayType(String displayType) {this.displayType = displayType;}

    public String getDisplayType() {return displayType;}

    public void setEndDateTime(String endDateTime) {this.endDateTime = endDateTime;}

    public String getEndDateTime() {return endDateTime;}

    public void setEndAction(String endAction) {this.endAction = endAction;}

    public String getEndAction() {return endAction;}

}
