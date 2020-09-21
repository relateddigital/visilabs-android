package com.visilabs.story.model.skinbased;


public class Stories
{
    private String thumbnail;

    private String title;

    private Items[] items;

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

    public Items[] getItems ()
    {
        return items;
    }

    public void setItems (Items[] items)
    {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [thumbnail = "+thumbnail+", title = "+title+", items = "+items+"]";
    }
}

