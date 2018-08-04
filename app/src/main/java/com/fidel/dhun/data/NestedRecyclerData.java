package com.fidel.dhun.data;

import java.util.ArrayList;

/**
 * Created by mukul on 12/8/17.
 */

public class NestedRecyclerData {
    int type = 0;
    ArrayList<Songs> songs;
    String cardtitle;

    public String getCardtitle() {
        return cardtitle;
    }

    public void setCardtitle(String cardtitle) {
        this.cardtitle = cardtitle;
    }

    public NestedRecyclerData(int type, ArrayList<Songs> songs, String cardtitle, ArrayList<CustomisedList> customisedLists) {
        this.type = type;
        this.songs = songs;
        this.cardtitle = cardtitle;
        this.customisedLists = customisedLists;

    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Songs> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Songs> songs) {
        this.songs = songs;
    }

    public ArrayList<CustomisedList> getCustomisedLists() {
        return customisedLists;
    }

    public void setCustomisedLists(ArrayList<CustomisedList> customisedLists) {
        this.customisedLists = customisedLists;
    }

    ArrayList<CustomisedList> customisedLists;
}
