package com.fidel.dhun.data;

/**
 * Created by fidel on 11/27/17.
 */

public class PopularAlbum {
    Integer Count;
    Songs album;

    public PopularAlbum(Integer count, Songs album) {
      this.Count = count;
        this.album = album;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        this.Count = count;
    }

    public Songs getAlbum() {
        return album;
    }

    public void setAlbum(Songs album) {
        this.album = album;
    }
}
