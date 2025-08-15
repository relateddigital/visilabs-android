package com.visilabs.survey.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SurveyOption implements Serializable {

    @SerializedName("option_text")
    private String optionText;

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}