package com.fidel.dhun.data.updateui;

import com.fidel.dhun.data.Songs;

import java.util.ArrayList;

/**
 * Created by fidel on 12/3/17.
 */

public class Updatenextplayingforartist {
    private ArrayList<com.fidel.dhun.data.Songs> Songs;
    private Songs song;

    public Updatenextplayingforartist(ArrayList<com.fidel.dhun.data.Songs> songs, com.fidel.dhun.data.Songs song) {
        Songs = songs;
        this.song = song;
    }

    public ArrayList<com.fidel.dhun.data.Songs> getSongs() {
        return Songs;
    }

    public void setSongs(ArrayList<com.fidel.dhun.data.Songs> songs) {
        Songs = songs;
    }

    public com.fidel.dhun.data.Songs getSong() {
        return song;
    }

    public void setSong(com.fidel.dhun.data.Songs song) {
        this.song = song;
    }
}
