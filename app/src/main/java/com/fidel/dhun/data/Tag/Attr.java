
package com.fidel.dhun.data.Tag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attr implements Serializable
{

    @SerializedName("artist")
    @Expose
    private String artist;
    private final static long serialVersionUID = -9193486396903142267L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Attr() {
    }

    /**
     * 
     * @param artist
     */
    public Attr(String artist) {
        super();
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
