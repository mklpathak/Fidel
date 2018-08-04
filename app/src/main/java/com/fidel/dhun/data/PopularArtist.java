package com.fidel.dhun.data;

/**
 * Created by fidel on 11/27/17.
 */

public class PopularArtist {
    ArtistPojo artist;
    Integer count;

    public PopularArtist(ArtistPojo artist, Integer count) {
        this.artist = artist;
        this.count = count;
    }

    public ArtistPojo getArtist() {
        return artist;
    }

    public void setArtist(ArtistPojo artist) {
        this.artist = artist;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
