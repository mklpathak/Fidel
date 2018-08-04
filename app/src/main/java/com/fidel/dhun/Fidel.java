package com.fidel.dhun;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.fidel.dhun.data.Api;
import com.fidel.dhun.data.ApiUtils;
import com.fidel.dhun.data.ArtistInfo;
import com.fidel.dhun.data.ArtistPojo;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.LoaderCommunication;
import com.fidel.dhun.data.OnConnected;
import com.fidel.dhun.data.Songs;
import com.fidel.dhun.data.Tag.Top;
import com.fidel.dhun.data.Tagforrecylerview;
import com.fidel.dhun.data.api.Example;
import com.fidel.dhun.fragments.Customintro;
import com.fidel.dhun.fragments.Splash;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fidel on 12/4/17.
 */

public class Fidel extends MaterialIntroActivity  implements OnConnected{
    private CheckBox checkBox;
    DatabaseSync dbsync;
    ArtistInfo artistSync;
    ArrayList<Songs> songs;
    ArrayList<Songs> artistList;
    ArrayList<Tagforrecylerview> taglist;
    Integer count = 0 ;
    String Status = "";
    String[] toppings = new String[1];
    Integer countArtist = 0 ;
    Integer totalArtist = 0;
    Api artistApi;
    Boolean firstTime = false;
    Databasesync databasesync;
    TextView textView;
    Boolean canMovefurther = false;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences settings = this.getSharedPreferences("Settings", 0);
        EventBus.getDefault().register(this);
        final Handler handler =new Handler();

        dbsync = new DatabaseSync(this);
        artistSync = new ArtistInfo(this);
        databasesync = new Databasesync();

        songs =new ArrayList<Songs>();
        artistList =new ArrayList<Songs>();
        taglist =new ArrayList<Tagforrecylerview>();
        artistApi = ApiUtils.getArtist();
        boolean previouslyStarted = settings.getBoolean("started", false);
        Toast.makeText(this,"Beta Version. App crash may occurs",Toast.LENGTH_LONG);

        if (!previouslyStarted) {

            try {
                if(isConnected()) {

                    checkPermission();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else
        {
            Intent i = new Intent(Fidel.this,MainActivity.class);
            startActivity(i);
            try {
                Log.e("Check Connection",String.valueOf(isConnected()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        addSlide(new Splash());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.firstslide)
                        .buttonsColor(R.color.colorAccent)
                        .image(R.drawable.navbarintro)
                        .title("Advanced Navigation Bar")
                        .description("Provides easily access to the Popular Artist, Album and Tags")
                        .build()
                );
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.secondslide)
                        .buttonsColor(R.color.colorAccent)
                        .neededPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                        .image(R.drawable.searchintro)
                        .title("Curated search mechanism")
                        .description("Organised according to Artist, Album and Songs")
                        .build()
                );
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.thirdslide)
                        .buttonsColor(R.color.colorAccent)
                        .neededPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                        .image(R.drawable.tagsintro)
                        .title("Heuristic Tagging Algorithm")
                        .description("With the use of LastFm API \nour tagging mechanism helps to filter out \nsongs according to popular tags on Last.fm")

                        .build());
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.forthslide)
                        .buttonsColor(R.color.colorAccent)
                        .neededPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                        .image(R.drawable.sugesstionintro)
                        .title("Optimised suggestions")
                        .description("Ongoing optimised suggestion based on artist in now playing ")

                        .build());

        addSlide(new Customintro());
    }
    @Subscribe
    public void onEvent(LoaderCommunication loaderCommunication){





    }

    @Override
    public void onFinish() {
        super.onFinish();
        Intent i = new Intent(Fidel.this,MainActivity.class);
        startActivity(i);
    }
    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int songIndex = 0;
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int album = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumid = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int artistid = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
            int dateAdded = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(album);
                String thisAlbumid = musicCursor.getString(albumid);
                String thisArtistid = musicCursor.getString(artistid);
                String thisdateAdded = musicCursor.getString(dateAdded);
                final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, Long.parseLong(thisAlbumid));
///                SharedPreferences.Editor editor = pref.edit();
//
//                //songDb.addSongs(new Songs(thisId, thisTitle, thisArtist,thisAlbum,thisAlbumid,albumArtUri,songIndex));
//                // Storing integer
//                if(pref.getInt("noofsongs",0)<songList.size()){
//                    songDb.addSongs(new Songs(thisId, thisTitle, thisArtist,thisAlbum,thisAlbumid,albumArtUri,songIndex));
//                }
//                editor.putInt("noofsongs", songDb.getContactsCount());
//                editor.commit();
                // songList = songDb.getAllContacts();


                songs.add(new Songs(thisId, thisTitle, thisArtist, thisArtistid, Long.parseLong(thisdateAdded), thisAlbum, thisAlbumid, albumArtUri, songIndex));
                songIndex++;
            }
            while (musicCursor.moveToNext());
        }
    }
    public void loadArtist( String artistName) {

        artistApi.getArtist(artistName).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, final Response<Example> response) {
//                Log.e("Artist name Mukul","pathak " + response.body().getArtist().getImage().get(3).getText());
                if (response.body().getArtist() != null) {
                    if (!response.body().getArtist().getImage().get(3).getText().equals("")) {
                        artistSync.addArtist(response.body());

                        countArtist++;
                        Log.e("updated",String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist));
                        EventBus.getDefault().post(new LoaderCommunication(totalArtist,countArtist));

                        if(countArtist ==totalArtist){

                        }




                    }
                    else {

                        countArtist++;
                        Log.e("updated",String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist));
                        Status = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
                        toppings[0] = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
                        EventBus.getDefault().post(new LoaderCommunication(totalArtist,countArtist));

                        if(countArtist ==totalArtist){
                            Intent i = new Intent(Fidel.this, MainActivity.class);
                            startActivity(i);
                        }


                    }
                }
                else{
                    countArtist++;
                    Log.e("updated",String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist));
                    Status = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
                    EventBus.getDefault().post(new LoaderCommunication(totalArtist,countArtist));

                    toppings[0] = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
                    if(countArtist ==totalArtist){
                        Intent i = new Intent(Fidel.this, MainActivity.class);
                        startActivity(i);
                    }




                }


            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                countArtist++;
                Log.e("updated",String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist));
                Status = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
                toppings[0] = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
                EventBus.getDefault().post(new LoaderCommunication(totalArtist,countArtist));

                if(countArtist ==totalArtist){
                    Intent i = new Intent(Fidel.this, MainActivity.class);
                    startActivity(i);
                }



            }
        });
        Log.e("updated",String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist));
        Status = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
        toppings[0] = String.valueOf(countArtist)+" out of " + String.valueOf(totalArtist);
        EventBus.getDefault().post(new LoaderCommunication(totalArtist,countArtist));

        if(countArtist ==totalArtist){
            Intent i = new Intent(Fidel.this, MainActivity.class);
            startActivity(i);
        }


    }
    ArrayList<String> getToptags(){
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> demotags = new ArrayList<String>();

        if(artistSync.getAllArtist()!=null){

            for(ArtistPojo s:artistSync.getAllArtist()){

                if(s!=null){

                    if(!s.getTags().equals("")){

                        String[] tag = s.getTags().split(",");

                        for (String t:tag){

                            tags.add(t.toLowerCase());
                        }

                    }

                }


            }}

        Collections.sort(tags, new Comparator<String>() {
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });
        String prevTag = "johfhoehriferiopij";
        String currentArtistName= "";
        int tagCount = 0;
        for (String s:tags){
            if(!s.contains(prevTag)){

                prevTag=s;
                demotags.add(s);

            }
        }



        return demotags;
    }


    public void loadTags(final String Artistname) {
        artistApi.getTags(Artistname).enqueue(new Callback<Top>() {
            @Override
            public void onResponse(Call<Top> call, final Response<Top> response) {
//                Log.e("Artist name Mukul","pathak " + response.body().getArtist().getImage().get(3).getText());
                //artistSync.addArtist(response.body().getToptags(),artisteExampleobject);
                if (response.body().getToptags() != null) {
                    artistSync.updateTags(Artistname, response.body());


                }
            }

            @Override
            public void onFailure(Call<Top> call, Throwable t) {

            }
        });
    }

    @Override
    public Boolean onConnected(Boolean isConnected) {

        Log.e("Status",String.valueOf(isConnected));

        if(isConnected)
        checkPermission();


        return null;
    }

    private class Databasesync extends AsyncTask<Void, String, Boolean> {
        String demoArtist = "udubsuhdoichjoiej";

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences settings = Fidel.this.getSharedPreferences("Settings", 0);
            boolean previouslyStarted = settings.getBoolean("started", false);

            toppings[0] = "hello";
            onProgressUpdate(toppings);
            if (!previouslyStarted) {


                count = 0;
                songs.clear();
                getSongList();
                for(Songs s:songs){
                    count++;
                    dbsync.addSongs(s);
                    artistList.add(s);
                    if(count==songs.size()){

                        Log.e("Status","Song list updated");

                        Collections.sort(artistList, new Comparator<Songs>() {
                            public int compare(Songs a, Songs b) {
                                return a.getArtist().compareTo(b.getArtist());
                            }
                        });
                        String artist = "hbjbjfbajvlbeavb";
                        ArrayList<String> artistname = new ArrayList<String>();
                        for (Songs artistsong : artistList) {
                            if (!artist.equals(artistsong.getArtist())) {
                                String[] sa = artistsong.getArtist().split(" and | & |/|feat |ft. | , | ,|feat. |, |\\(| -");
                                Arrays.sort(sa);
                                for (String sap : sa) {
                                    artistname.add(sap);
                                }
                                artist = artistsong.getArtist();
                            }
                        }
                        Collections.sort(artistname, new Comparator<String>() {
                            public int compare(String a, String b) {
                                return a.compareTo(b);
                            }
                        });
                        totalArtist = 0 ;
                        countArtist = 0;


                        for (String artistName : artistname
                                ) {
                            if (!demoArtist.equals(artistName)) {
                                totalArtist++;
                                loadArtist(artistName);
                                demoArtist = artistName;
                            }
                        }
                        Log.e("Started",String.valueOf(totalArtist) + "of times");
                        Log.e("Status","Artist list updated");
                        for (String artistName : artistname
                                ) {
                            if (!demoArtist.equals(artistName)) {
                                loadTags(artistName);
                                demoArtist = artistName;
                            }
                        }
                        Log.e("Status","Artist tags updated");


                    }

                }







//                taglist.clear();
//                ArrayList<String> demolisttags = new ArrayList<String>();
//                ArrayList<String> demolisttags2 = new ArrayList<String>();
//
//                demolisttags =getToptags();
//                Collections.sort(demolisttags, new Comparator<String>() {
//                    public int compare(String a, String b) {
//                        return a.compareTo(b);
//                    }
//                });
//                String prevTag = "johfhoehriferiopij";
//                String currentArtistName= "";
//                int tagCount = 0;
//                for (String s:demolisttags){
//                    if(!s.contains(prevTag)){
//
//                        prevTag=s;
//                        demolisttags2.add(s);
//
//                    }
//                }
//
//                for (String s:demolisttags2){
//                    for(ArtistPojo artistPojo: artistSync.getAllArtistbyTag(s)){
//                        if(dbsync.getArtistDetails(artistPojo.getName())!=null){
//                            if(tagCount<dbsync.getArtistDetails(artistPojo.getName()).size()){
//                                tagCount = dbsync.getArtistDetails(artistPojo.getName()).size();
//
//                                currentArtistName = artistPojo.getName();
//
//
//                            }}
//                    }
//                    artistSync.addTags( new Tagforrecylerview(artistSync.getAllArtistbyTag(s).size(),s,artistSync.getArtistDetails(currentArtistName).getImageuri()));
//
//                    taglist.add(new Tagforrecylerview(artistSync.getAllArtistbyTag(s).size(),s,artistSync.getArtistDetails(currentArtistName).getImageuri()));
//                    currentArtistName = "";
//                    tagCount=0;
//                }
                Log.e("Status","Tags Table updated");



            } else {


            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            SharedPreferences settings = Fidel.this.getSharedPreferences("Settings", 0);
            boolean previouslyStarted = settings.getBoolean("started", false);
            if (!previouslyStarted) {
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("started", Boolean.TRUE);
                firstTime = true;
                edit.commit();


            }




            super.onPostExecute(aBoolean);
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted

            databasesync.execute();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && readExternalFile)


                    {
                        databasesync.execute();

                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    public boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }
}

