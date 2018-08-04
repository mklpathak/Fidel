
package com.fidel.dhun.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Similar {

    @SerializedName("artist")
    @Expose
    private List<Object> artist = null;

    public List<Object> getArtist() {
        return artist;
    }

    public void setArtist(List<Object> artist) {
        this.artist = artist;
    }

}
