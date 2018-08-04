package com.fidel.dhun.data;

/**
 * Created by fidel on 11/17/17.
 */

public class ArtistPojo {
    private String name;
    private String Imageuri ;
    private String similar;
    private String summary ;
    private String imageUpdated;
    private String tags;
    private String getbackcolor;
    private String getforcolor;



    public String getGetbackcolor() {
        return getbackcolor;
    }

    public void setGetbackcolor(String getbackcolor) {
        this.getbackcolor = getbackcolor;
    }

    public String getGetforcolor() {
        return getforcolor;
    }

    public void setGetforcolor(String getforcolor) {
        this.getforcolor = getforcolor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageuri() {
        return Imageuri;
    }

    public ArtistPojo(String name, String imageuri, String similar, String summary, String imageUpdated, String noOFtimesPlayed, String noofSongs,String tags, String getbackcolor, String getforcolor) {
        this.name = name;
        Imageuri = imageuri;
        this.similar = similar;
        this.summary = summary;
        this.imageUpdated = imageUpdated;
        this.noOFtimesPlayed = noOFtimesPlayed;
        this.noofSongs = noofSongs;
        this.tags=tags;
        this.getbackcolor = getbackcolor;
        this.getforcolor = getforcolor;
    }

    public void setImageuri(String imageuri) {
        Imageuri = imageuri;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUpdated() {
        return imageUpdated;
    }

    public void setImageUpdated(String imageUpdated) {
        this.imageUpdated = imageUpdated;
    }

    public String getNoOFtimesPlayed() {
        return noOFtimesPlayed;
    }

    public void setNoOFtimesPlayed(String noOFtimesPlayed) {
        this.noOFtimesPlayed = noOFtimesPlayed;
    }

    public String getNoofSongs() {
        return noofSongs;
    }

    public void setNoofSongs(String noofSongs) {
        this.noofSongs = noofSongs;
    }

    private  String noOFtimesPlayed;
    private String noofSongs;
}
