package com.visilabs.survey.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class SurveyQuestion implements Serializable {

    @SerializedName("question_text")
    private String questionText;

    @SerializedName("options")
    private List<SurveyOption> options;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<SurveyOption> getOptions() {
        return options;
    }

    public void setOptions(List<SurveyOption> options) {
        this.options = options;
    }
}