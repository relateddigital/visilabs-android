package com.visilabs.story.model.storylookingbanners;

import com.visilabs.story.model.Report;

import java.io.Serializable;
import java.util.List;

public class Actiondata implements Serializable {
    private List<Stories> stories;

    private String taTemplate;

    private String ExtendedProps;
    
    private Report report;

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public List<Stories> getStories() {
        return this.stories;
    }

    public void setTaTemplate(String taTemplate) {
        this.taTemplate = taTemplate;
    }

    public String getTaTemplate() {
        return this.taTemplate;
    }

    public void setExtendedProps(String ExtendedProps) {
        this.ExtendedProps = ExtendedProps;
    }

    public String getExtendedProps() {
        return this.ExtendedProps;
    }

    public Report getReport() {
        return this.report;
    }
}
