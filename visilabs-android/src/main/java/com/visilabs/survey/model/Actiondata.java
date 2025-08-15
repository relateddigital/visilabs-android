package com.visilabs.survey.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Actiondata implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("survey_questions")
    private List<SurveyQuestion> survey_questions;

    @SerializedName("ExtendedProps")
    private String extendedProps;

    @SerializedName("report")
    private Report report; // Kendi Report sınıfımızı kullanıyoruz.

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<SurveyQuestion> getSurveyQuestions() {
        return survey_questions;
    }
    public void setSurveyQuestions(List<SurveyQuestion> survey_questions) {
        this.survey_questions = survey_questions;
    }
    public String getExtendedProps() {
        return extendedProps;
    }
    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
    }
}