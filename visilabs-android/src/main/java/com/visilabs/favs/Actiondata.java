package com.visilabs.favs;


import java.io.Serializable;
import java.util.Arrays;

public class Actiondata implements Serializable
{
    private Favorites favorites;

    private String[] attributes;

    public Favorites getFavorites ()
    {
        return favorites;
    }

    public void setFavorites (Favorites favorites)
    {
        this.favorites = favorites;
    }

    public String[] getAttributes ()
    {
        return attributes;
    }

    public void setAttributes (String[] attributes)
    {
        this.attributes = attributes;
    }

    @Override
    public String toString()
    {
        return "Actiondata [favorites = "+favorites+", attributes = "+ Arrays.toString(attributes) +"]";
    }
}

