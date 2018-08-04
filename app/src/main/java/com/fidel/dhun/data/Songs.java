package com.fidel.dhun.data;

import android.net.Uri;

/**
 * Created by Mukul on 30-07-2017.
 */

public class Songs {
    private long id;
    private String title;
    private String artist;
    private  String AlbumID;
    private String Album;
private int songIndex;
    private Uri Albumarturi;
    private String artistid;
    private  long dateAdded;
   // private Uri songLocation;

    public Songs(long songID, String songTitle, String songArtist,String artistid,long dateadded,String songAlbum, String songAlbumID, Uri Albumart,int songIndex) {
        this.id=songID;
        title=songTitle;
        artist=songArtist;
        Album = songAlbum;
        AlbumID = songAlbumID;
        Albumarturi = Albumart;
        this.songIndex = songIndex;
        this.artistid = artistid;
        this.dateAdded= dateadded;

       // this.songLocation = songLocation;
    }



    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbumID(){return AlbumID;};
    public String getAlbum(){return Album;}

    public Uri getAlbumarturi() {
        return Albumarturi;
    }
    public int getSongIndex(){
        return  songIndex;
    }

//    public Uri getSongLocation() {
//        return songLocation;
//    }


    public String getArtistid() {
        return artistid;
    }

    public long getDateAdded() {
        return dateAdded;
    }
}
