package com.fidel.dhun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fidel.dhun.data.Tag.Top;
import com.fidel.dhun.data.api.Example;

import java.util.ArrayList;

/**
 * Created by fidel on 10/29/17.
 */

public class ArtistInfo extends SQLiteOpenHelper {
    ArtistPojo artistName;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dhunartist";

    // Contacts table name
    private static final String TABLE_Artist = "artistInfoCollection";
    private static final String TABLE_TAGS = "Tags";

    // Contacts Table Columns names
    private static final String KEY_sno = "artistNo";
    private static final String KEY_artistName = "artistName";
    private static final String KEY_artistSummary = "artistSummary";
    private static final String KEY_artistImageUri = "artistImageURi";
    private static final String KEY_artistSimilar = "artistSimilar";
    //private static final String KEY_Uri = "song_uri";
    private static final String KEY_imageUpdated = "artistImageUpdated";
    private static final String KEY_noOfTimePlayed = "noOftimeplayed";
    private static final String KEY_ARTIST_BACK_COLOR = "artistbackcolor";
    private static final String KEY_ARTIST_FOR_COLOR = "artistforcolor";
    private static final String KEY_noOFSongs= "noOfSongs";
    private static final String KEY_tags= "tags";
    private static final String KEY_tags_updated= "tagsupdated";
    private static final String KEY_TAG_NAME = "tagname";
    private static final String KEY_TAG_COUNTS= "counts";
    private static final String KEY_TAG_URI= "taguri";
    private static final String KEY_TAG_URI_IMAGEUPDATED= "taguriimageupdated";




    public ArtistInfo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Artist);
        String CREATE_Songs_TABLE = "CREATE TABLE " + TABLE_Artist + "("
            + KEY_artistName + " TEXT,"
                + KEY_artistSummary + " TEXT," + KEY_artistImageUri + " TEXT," + KEY_artistSimilar +
                " TEXT," + KEY_imageUpdated + " TEXT," + KEY_noOfTimePlayed + " TEXT," + KEY_noOFSongs + " TEXT," + KEY_tags + " TEXT," +
                 KEY_tags_updated + " TEXT, "+KEY_ARTIST_BACK_COLOR+" TEXT, "+KEY_ARTIST_FOR_COLOR+" TEXT )";
        db.execSQL(CREATE_Songs_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);

        String CREATE_Songs_Tags = "CREATE TABLE " + TABLE_TAGS + "("
                + KEY_TAG_NAME + " TEXT,"
                + KEY_TAG_COUNTS + " TEXT," + KEY_TAG_URI + " TEXT," + KEY_TAG_URI_IMAGEUPDATED+
                " TEXT)";
        db.execSQL(CREATE_Songs_Tags);


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
    public void addArtist( Example artist) {
        String tag = "";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_artistName, artist.getArtist().getName()); // Contact Name
        values.put(KEY_artistSummary, artist.getArtist().getBio().getSummary());
        values.put(KEY_artistImageUri, artist.getArtist().getImage().get(3).getText());
        values.put(KEY_artistSimilar, tag);


        values.put(KEY_tags, tag);
        values.put(KEY_imageUpdated, "false");
        values.put(KEY_noOFSongs, 0);
        values.put(KEY_noOfTimePlayed, 0);
        values.put(KEY_tags_updated, "false");
        values.put(KEY_ARTIST_FOR_COLOR,"");
        values.put(KEY_ARTIST_BACK_COLOR,"");

Log.e("hello ","fuck");

        // Inserting Row
        db.insert(TABLE_Artist, null, values);

    }

    public void addTags( Tagforrecylerview tags) {
        String tag = "";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tags.getName()); // Contact Name
        values.put(KEY_TAG_COUNTS, tags.getCount());
        values.put(KEY_TAG_URI, tags.getUrl());
        values.put(KEY_TAG_URI_IMAGEUPDATED, "false");


        // Inserting Row
        db.insert(TABLE_TAGS, null, values);

    }

    public void updateTagfortagtable( Tagforrecylerview tags) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAG_NAME,tags.getName());
        cv.put(KEY_TAG_COUNTS,tags.getCount());
        cv.put(KEY_TAG_URI,tags.getUrl());
        //These Fields should be your String values of actual column names
        db.update(TABLE_TAGS,cv,KEY_TAG_NAME + "=?",  new String[]{tags.getName()});
       // Closing database connection
    }

    public void updateTags(String artistName, Top tags){

        String tag = "";
//        for (Tag a:tags.getToptags().getTag()
//             ) {
//
//            tag = "" + tag +" or "+ a.getName();
//
//        }
        int size = 0 ;

if(tags.getToptags().getTag().size()>10){
    size =10;


}
else{
    size =tags.getToptags().getTag().size();
}
        for(int i = 0 ; i<size;i++){

            if(i==0){

                tag = "" + tags.getToptags().getTag().get(i).getName();

            }
            else {

                tag = tag + "," +tags.getToptags().getTag().get(i).getName();
            }

        }

        //

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_tags,tag);
        cv.put(KEY_tags_updated,"true");//These Fields should be your String values of actual column names
        db.update(TABLE_Artist,cv,KEY_artistName + "=?",  new String[]{artistName});
       // db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

                // Log.e("Error",cursor.getString(4));
                // Adding contact to list




    }

    public void updatecolor(String backcolor,String forcolor,String artistName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ARTIST_BACK_COLOR,backcolor);
        cv.put(KEY_ARTIST_FOR_COLOR,forcolor);//These Fields should be your String values of actual column names
        db.update(TABLE_Artist,cv,KEY_artistName + "=?",  new String[]{artistName});


    }

    // Getting single artist detaills
   public ArtistPojo getArtistDetails(String searchArtist) {
       if( !searchArtist.contains("'")) {

       Log.e("Artist : " ,"here" + searchArtist);
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_Artist, new String[] { KEY_artistName,
//
//                        KEY_artistSummary, KEY_artistImageUri,KEY_artistSimilar,KEY_imageUpdated,KEY_noOfTimePlayed,KEY_noOFSongs}, KEY_artistName + "=?" ,
//                new String[]{searchArtist}, null, null, null, null);

//       String CREATE_Songs_TABLE = "CREATE TABLE " + TABLE_Artist + "("
//               + KEY_artistName + " TEXT,"
//               + KEY_artistSummary + " TEXT," + KEY_artistImageUri + " TEXT," + KEY_artistSimilar +
//               " TEXT," + KEY_imageUpdated + " TEXT," + KEY_noOfTimePlayed + " TEXT,"
//               + KEY_noOFSongs + " TEXT," + KEY_tags + " TEXT )";
       String selectQuery = "SELECT  * FROM " + TABLE_Artist + " WHERE "+ KEY_artistName +"  LIKE '%"+ searchArtist  +"%'";
    //   String name, String imageuri, String similar, String summary, String imageUpdated, String noOFtimesPlayed, String noofSongs
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       if (cursor != null &&  cursor.moveToFirst()){

         artistName = new ArtistPojo(
        cursor.getString(0),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(1),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),cursor.getString(7),cursor.getString(8)
                 ,cursor.getString(9));
        return artistName;}
        else {
           return null;
       }}
       else {return null;}
    }

    public Tagforrecylerview getTags(String searchTag) {


            Log.e("Tag to be search : " ,"here" + searchTag);

            String selectQuery = "SELECT  * FROM " + TABLE_TAGS + " WHERE "+ KEY_TAG_NAME +"  LIKE '%"+ searchTag  +"%'";
            //   String name, String imageuri, String similar, String summary, String imageUpdated, String noOFtimesPlayed, String noofSongs
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null &&  cursor.moveToFirst()){

                Tagforrecylerview tagforrecylerview = new Tagforrecylerview(Integer.parseInt(cursor.getString(1))
                        ,cursor.getString(0),cursor.getString(2));
                cursor.close();

                return tagforrecylerview;}
            else {
                cursor.close();
                return null;
            }


    }




    // Getting All Contacts
    public ArrayList<ArtistPojo> getAllArtist() {
        ArrayList<ArtistPojo> artistList = new ArrayList<ArtistPojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Artist;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                + KEY_artistName + " TEXT,"
//                        + KEY_artistSummary + " TEXT," + KEY_artistImageUri + " TEXT," + KEY_artistSimilar +
//                        " TEXT," + KEY_imageUpdated + " TEXT," + KEY_noOfTimePlayed + " TEXT," + KEY_noOFSongs + " TEXT " +

//            String name, String imageuri, String similar,
// String summary, String imageUpdated,
// String noOFtimesPlayed, String noofSongs
                ArtistPojo artistName = new ArtistPojo(
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(1),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),cursor.getString(7),cursor.getString(8)
                        ,cursor.getString(9));


                // Log.e("Error",cursor.getString(4));
                // Adding contact to list
                if(artistName!=null)
                artistList.add(artistName);
            } while (cursor.moveToNext());
        }

        // return contact list
        return artistList;
    }
    public ArrayList<ArtistPojo> getAllArtistbyTag(String tag) {
        ArrayList<ArtistPojo> artistList = new ArrayList<ArtistPojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Artist + " WHERE "+ KEY_tags +"  LIKE '%"+ tag  +"%'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                + KEY_artistName + " TEXT,"
//                        + KEY_artistSummary + " TEXT," + KEY_artistImageUri + " TEXT," + KEY_artistSimilar +
//                        " TEXT," + KEY_imageUpdated + " TEXT," + KEY_noOfTimePlayed + " TEXT," + KEY_noOFSongs + " TEXT " +

//            String name, String imageuri, String similar,
// String summary, String imageUpdated,
// String noOFtimesPlayed, String noofSongs
                ArtistPojo artistName = new ArtistPojo(
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(1),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),cursor.getString(7),cursor.getString(8)
                        ,cursor.getString(9));

                // Log.e("Error",cursor.getString(4));
                // Adding contact to list
                if(artistName!=null)
                    artistList.add(artistName);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list
        return artistList;
    }

    public ArrayList<ArtistPojo> getArtistbySearch(String search) {
        ArrayList<ArtistPojo> artistList = new ArrayList<ArtistPojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Artist + " WHERE "+ KEY_artistName +"  LIKE '%"+ search  +"%'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                + KEY_artistName + " TEXT,"
//                        + KEY_artistSummary + " TEXT," + KEY_artistImageUri + " TEXT," + KEY_artistSimilar +
//                        " TEXT," + KEY_imageUpdated + " TEXT," + KEY_noOfTimePlayed + " TEXT," + KEY_noOFSongs + " TEXT " +

//            String name, String imageuri, String similar,
// String summary, String imageUpdated,
// String noOFtimesPlayed, String noofSongs
                ArtistPojo artistName = new ArtistPojo(
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(1),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),cursor.getString(7),cursor.getString(8)
                        ,cursor.getString(9));

                // Log.e("Error",cursor.getString(4));
                // Adding contact to list
                if(artistName!=null)
                    artistList.add(artistName);
            } while (cursor.moveToNext());
        }

        // return contact list
        return artistList;
    }

//    // Updating single contact
//    public int updateContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }

    // Deleting single contact
//    public void deleteContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }
    public ArrayList<Tagforrecylerview> getallTags() {

       ArrayList<Tagforrecylerview> getTagList = new ArrayList<Tagforrecylerview>();


        String selectQuery = "SELECT  * FROM " + TABLE_TAGS + " order by " +KEY_TAG_COUNTS+" desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {

                getTagList.add(new Tagforrecylerview(Integer.parseInt(cursor.getString(1)),cursor.getString(0),cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return getTagList;

    }

    ArrayList<String> topTags(){




        return null;
    }
    // Getting contacts Count
    public int getSongsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Artist;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void addSongs() {
    }
}