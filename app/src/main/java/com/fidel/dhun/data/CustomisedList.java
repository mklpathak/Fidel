package com.fidel.dhun.data;

/**
 * Created by fidel on 11/24/17.
 */

public class CustomisedList {
    int type;
    ArtistPojo artist;
    Songs song;
    Songs album;
    String Tag;

    public CustomisedList(int type,ArtistPojo artist, Songs song, Songs album, String tag) {
        this.type = type;
        this.artist = artist;
        this.song = song;
        this.album = album;
        Tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArtistPojo getArtist() {
        return artist;
    }

    public void setArtist(ArtistPojo artist) {
        this.artist = artist;
    }

    public Songs getSong() {
        return song;
    }

    public void setSong(Songs song) {
        this.song = song;
    }

    public Songs getAlbum() {
        return album;
    }

    public void setAlbum(Songs album) {
        this.album = album;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
