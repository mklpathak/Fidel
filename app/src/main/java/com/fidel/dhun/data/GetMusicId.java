package com.fidel.dhun.data;

import java.util.ArrayList;

/**
 * Created by Mukul on 31-07-2017.
 */

public class GetMusicId {
    private final int Songid;
    private  ArrayList<Songs> Songs = new ArrayList<Songs>();

    public GetMusicId(int Songid,ArrayList<Songs> songs) {
        this.Songid = Songid;
        this.Songs = songs;
    }

    public int getSongid(){

        return Songid;

    }
    public ArrayList<Songs> getList(){

        return Songs;
    }
}
