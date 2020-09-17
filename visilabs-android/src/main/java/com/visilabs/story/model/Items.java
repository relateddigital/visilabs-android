package com.visilabs.story.model;


public class Items
{
    private String targetUrlOriginal;

    private String fileSrc;

    private String buttonText;

    private String displayTime;

    private String buttonColor;

    private String filePreview;

    private String buttonTextColor;

    private String targetUrl;

    private String fileType;

    public String getTargetUrlOriginal ()
    {
        return targetUrlOriginal;
    }

    public void setTargetUrlOriginal (String targetUrlOriginal)
    {
        this.targetUrlOriginal = targetUrlOriginal;
    }

    public String getFileSrc ()
    {
        return fileSrc;
    }

    public void setFileSrc (String fileSrc)
    {
        this.fileSrc = fileSrc;
    }

    public String getButtonText ()
    {
        return buttonText;
    }

    public void setButtonText (String buttonText)
    {
        this.buttonText = buttonText;
    }

    public String getDisplayTime ()
    {
        return displayTime;
    }

    public void setDisplayTime (String displayTime)
    {
        this.displayTime = displayTime;
    }

    public String getButtonColor ()
    {
        return buttonColor;
    }

    public void setButtonColor (String buttonColor)
    {
        this.buttonColor = buttonColor;
    }

    public String getFilePreview ()
    {
        return filePreview;
    }

    public void setFilePreview (String filePreview)
    {
        this.filePreview = filePreview;
    }

    public String getButtonTextColor ()
    {
        return buttonTextColor;
    }

    public void setButtonTextColor (String buttonTextColor)
    {
        this.buttonTextColor = buttonTextColor;
    }

    public String getTargetUrl ()
    {
        return targetUrl;
    }

    public void setTargetUrl (String targetUrl)
    {
        this.targetUrl = targetUrl;
    }

    public String getFileType ()
    {
        return fileType;
    }

    public void setFileType (String fileType)
    {
        this.fileType = fileType;
    }

    @Override
    public String toString()
    {
        return "Items [targetUrlOriginal = "+targetUrlOriginal+", fileSrc = "+fileSrc+", buttonText = "+buttonText+", displayTime = "+displayTime+", buttonColor = "+buttonColor+", filePreview = "+filePreview+", buttonTextColor = "+buttonTextColor+", targetUrl = "+targetUrl+", fileType = "+fileType+"]";
    }
}

