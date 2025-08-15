package com.visilabs.survey.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class QuestionAnswer implements Serializable {

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}