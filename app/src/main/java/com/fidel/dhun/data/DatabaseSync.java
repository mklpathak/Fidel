package com.fidel.dhun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by fidel on 8/20/17.
 */

public class DatabaseSync extends SQLiteOpenHelper {

    ArrayList<Songs> getSongList;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dhun";

    // Contacts table name
    private static final String TABLE_Songs = "songCollection";

    // Contacts Table Columns names
    private static final String KEY_ID = "songNo";
    private static final String KEY_Title = "title";
    private static final String KEY_Artist = "artist";
    private static final String KEY_ArtistId = "artistID";
    private static final String KEY_Album = "album";
    //private static final String KEY_Uri = "song_uri";
    private static final String KEY_albumarturi = "albumArtUri";
    private static final String KEY_albumid = "albumId";
    private static final String KEY_dateAdded = "dateAdded";
    private static final String KEY_SongID = "songid";



    public DatabaseSync(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Songs);
        String CREATE_Songs_TABLE = "CREATE TABLE " + TABLE_Songs + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_Title + " TEXT,"
                + KEY_Artist + " TEXT," + KEY_ArtistId + " TEXT," + KEY_Album +
                " TEXT," + KEY_albumarturi + " TEXT," + KEY_albumid + " TEXT," + KEY_dateAdded + " TEXT," + KEY_SongID + " TEXT" + ")";
        db.execSQL(CREATE_Songs_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed


        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addSongs(Songs songs) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Title, songs.getTitle()); // Contact Name
        values.put(KEY_Artist, songs.getArtist());
        values.put(KEY_Album, songs.getAlbum());
        values.put(KEY_albumarturi, String.valueOf(songs.getAlbumarturi()));
        values.put(KEY_albumid, songs.getAlbumID());
        values.put(KEY_SongID, songs.getID());
        values.put(KEY_ArtistId, songs.getArtistid());
        values.put(KEY_dateAdded, songs.getDateAdded());


        // Inserting Row
        db.insert(TABLE_Songs, null, values);

    }

    // Getting single contact
    Songs getSong(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_Songs, new String[]{KEY_ID,
                        KEY_Title, KEY_Artist, KEY_Album, KEY_albumarturi, KEY_albumid, KEY_SongID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
//
//        Songs song = new Songs(
//                Long.parseLong(cursor.getString(6)),
//                cursor.getString(1),
//                cursor.getString(2),
//                cursor.getString(3),
//                cursor.getString(4),
//                Uri.parse(cursor.getString(5)),
//                Integer.parseInt(cursor.getString(0))
//
//        );
        // return contact
        return null;
    }

    // Getting All Contacts
    public ArrayList<Songs> getAllContacts() {
        ArrayList<Songs> songlist = new ArrayList<Songs>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Songs + " order by " + KEY_Title;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                +  0 KEY_ID + " INTEGER PRIMARY KEY," + 1  KEY_Title + " TEXT,"
//                        + 2  KEY_Artist + " TEXT," + 3 KEY_ArtistId + " TEXT," +  4 KEY_Album +
//                        " TEXT," + 5 KEY_albumarturi + " TEXT," +  6 KEY_albumid + " TEXT," +7  KEY_dateAdded +
// " TEXT," + 8 KEY_SongID +

//                long songID, String songTitle, String songArtist,
//                        String artistid,long dateadded,String songAlbum,
//                        String songAlbumID, Uri Albumart,int songIndex
                Songs song = new Songs(
                        Long.parseLong(cursor.getString(8)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Long.parseLong(cursor.getString(7)),
                        cursor.getString(4),
                        cursor.getString(6),
                        Uri.parse(cursor.getString(5)), 0

                );
                // Log.e("Error",cursor.getString(4));
                // Adding contact to list
                songlist.add(song);
            } while (cursor.moveToNext());
        }


        // return contact list
        return songlist;
    }

    public ArrayList<Songs> getArtistDetails(String searchArtist) {

        getSongList = new ArrayList<Songs>();

        if (!searchArtist.contains("'")) {
            String selectQuery = "SELECT  * FROM " + TABLE_Songs + " WHERE " + KEY_Artist + "  LIKE '%" + searchArtist + "%' order by " + KEY_Title;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
//                +  0 KEY_ID + " INTEGER PRIMARY KEY," + 1  KEY_Title + " TEXT,"
//                        + 2  KEY_Artist + " TEXT," + 3 KEY_ArtistId + " TEXT," +  4 KEY_Album +
//                        " TEXT," + 5 KEY_albumarturi + " TEXT," +  6 KEY_albumid + " TEXT," +7  KEY_dateAdded +
// " TEXT," + 8 KEY_SongID +

//                long songID, String songTitle, String songArtist,
//                        String artistid,long dateadded,String songAlbum,
//                        String songAlbumID, Uri Albumart,int songIndex
                    Songs song = new Songs(
                            Long.parseLong(cursor.getString(8)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            Long.parseLong(cursor.getString(7)),
                            cursor.getString(4),
                            cursor.getString(6),
                            Uri.parse(cursor.getString(5)), 0

                    );
                    // Log.e("Error",cursor.getString(4));
                    // Adding contact to list
                    getSongList.add(song);
                } while (cursor.moveToNext());
            }
            cursor.close();

            return getSongList;
        } else {
            return null;
        }
    }

    public ArrayList<Songs> getAlbumDetails(String searchAlbum) {

        getSongList = new ArrayList<Songs>();

        if (!searchAlbum.contains("'")) {
            String selectQuery = "SELECT  * FROM " + TABLE_Songs + " WHERE " + KEY_Album + "  LIKE '%" + searchAlbum + "%' order by " + KEY_Title;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {

                    Songs song = new Songs(
                            Long.parseLong(cursor.getString(8)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            Long.parseLong(cursor.getString(7)),
                            cursor.getString(4),
                            cursor.getString(6),
                            Uri.parse(cursor.getString(5)), 0

                    );

                    getSongList.add(song);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return getSongList;
        } else {
            return null;
        }
    }
    public ArrayList<Songs> getSongListbySearch(String searchTitle) {
        ArrayList<Songs> songListBySearch = new ArrayList<Songs>();
        songListBySearch = new ArrayList<Songs>();

        if (!searchTitle.contains("'")) {
            String selectQuery = "SELECT  * FROM " + TABLE_Songs + " WHERE " + KEY_Title + "  LIKE '%" + searchTitle + "%' order by " + KEY_Title;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {

                    Songs song = new Songs(
                            Long.parseLong(cursor.getString(8)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            Long.parseLong(cursor.getString(7)),
                            cursor.getString(4),
                            cursor.getString(6),
                            Uri.parse(cursor.getString(5)), 0

                    );

                    songListBySearch.add(song);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return songListBySearch;
        } else {
            return null;
        }
    }
    public ArrayList<Songs> getrecentlyadded() {

        getSongList = new ArrayList<Songs>();


            String selectQuery = "SELECT  * FROM " + TABLE_Songs + " order by " +KEY_dateAdded+" desc";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {

                    Songs song = new Songs(
                            Long.parseLong(cursor.getString(8)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            Long.parseLong(cursor.getString(7)),
                            cursor.getString(4),
                            cursor.getString(6),
                            Uri.parse(cursor.getString(5)), 0

                    );

                    getSongList.add(song);
                } while (cursor.moveToNext());
            }
            return getSongList;

    }




    // Getting contacts Count
    public int getSongsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Songs;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteSongs( Songs s) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_Songs,KEY_SongID+" = "+s.getID(),null);

        Log.e("Deleted",s.getTitle());




    }
}