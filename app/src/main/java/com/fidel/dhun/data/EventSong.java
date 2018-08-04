package com.fidel.dhun.data;

import java.util.ArrayList;

/**
 * Created by Mukul on 31-07-2017.
 */

public class EventSong {
    private final ArrayList songList;

    public EventSong(ArrayList songList) {
        this.songList = songList;
    }

    public ArrayList getSongList(){

        return songList;
    }
}
