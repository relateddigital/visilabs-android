package com.visilabs.story.model.skinbased;


import java.io.Serializable;
import java.util.List;

public class Stories implements Serializable
{
    private String thumbnail;

    private String title;

    private List<Items> items;

    private boolean shown;

    public String getThumbnail ()
    {
        return thumbnail;
    }

    public void setThumbnail (String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public List<Items> getItems ()
    {
        return items;
    }

    public void setItems (List<Items> items)
    {
        this.items = items;
    }

    public Boolean getShown() {
        return shown;
    }

    public void setShown(Boolean shown) {
        this.shown = shown;
    }

    @Override
    public String toString()
    {
        return "Stories [thumbnail = "+thumbnail+", title = "+title+", items = "+items+"]";
    }
}

