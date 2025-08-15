package com.visilabs.survey.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class SurveyResult implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("questions")
    private List<QuestionAnswer> questions;

    public String getTitle() {
        return title;
    }

    public List<QuestionAnswer> getQuestions() {
        return questions;
    }
}