package com.example.bakingapp.Models;

public class Step  {


    private int stepId;
    private String stepShortDescription;
    private String stepDescription;
    private String stepVideoUrl;
    private String stepThumbVideo;


    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getStepShortDescription() {
        return stepShortDescription;
    }

    public void setStepShortDescription(String stepShortDescription) {
        this.stepShortDescription = stepShortDescription;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getStepVideoUrl() {
        return stepVideoUrl;
    }

    public void setStepVideoUrl(String stepVideoUrl) {
        this.stepVideoUrl = stepVideoUrl;
    }

    public String getStepThumbVideo() {
        return stepThumbVideo;
    }

    public void setStepThumbVideo(String stepThumbVideo) {
        this.stepThumbVideo = stepThumbVideo;
    }
}