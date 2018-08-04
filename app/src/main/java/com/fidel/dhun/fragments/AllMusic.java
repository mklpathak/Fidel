package com.fidel.dhun.fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fidel.dhun.R;
import com.fidel.dhun.adapter.SongAdapter;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.EventSong;
import com.fidel.dhun.data.GetMusicId;
import com.fidel.dhun.data.Songs;
import com.fidel.dhun.services.MusicService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllMusic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMusic extends Fragment  {
    private ArrayList<Songs> tempList;
    DatabaseSync songDb;
    private ArrayList<Songs> songs;
    RecyclerView recyclerView;
    SongAdapter songsadapter;
    SharedPreferences pref;

MusicService musicSrv = new MusicService();
    public static AllMusic newInstance() {
        AllMusic fragment = new AllMusic();
        return fragment;
    }

    public AllMusic() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment AllMusic.
     */
    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }
        songs = new ArrayList<Songs>();
        DatabaseSync dbsync = new DatabaseSync(getContext());
      //Bundle  bundle = this.getArguments();

       // songs = (ArrayList<Songs>) bundle.getSerializable("songList");

        getSongList();

        Collections.sort(songs, new Comparator<Songs>() {
            @Override
            public int compare(Songs o1, Songs o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });









        // Inflate the layout for this fragment
        View view1= inflater.inflate(R.layout.fragment_all_music, container, false);
        Fresco.initialize(getContext());
       // songDb = new DatabaseSync(getContext());
        recyclerView = (RecyclerView) view1.findViewById(R.id.song_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);



        recyclerView.setAdapter(new SongAdapter(getContext(),songs, new SongAdapter.OnItemClickListener() {
            @Override public void onItemClick(Songs item) {




                EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(songs.indexOf(item))),songs));
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), item.getAlbumarturi());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            if(palette.getDarkMutedSwatch()!= null)

                            Log.e("DarkMuted",String.format("#%06X", (0xFFFFFF &palette.getDarkMutedSwatch().getRgb())));
                            if(palette.getMutedSwatch()!= null)
                            Log.e("Muted",String.format("#%06X", (0xFFFFFF &palette.getMutedSwatch().getRgb())));
                            if(palette.getLightMutedSwatch()!= null)
                            Log.e("LightMuted",String.format("#%06X", (0xFFFFFF &palette.getLightMutedSwatch().getRgb())));
                            if(palette.getVibrantSwatch()!= null)
                            Log.e("VibrantMuted",String.format("#%06X", (0xFFFFFF &palette.getVibrantSwatch().getRgb())));
                            if(palette.getDarkVibrantSwatch()!= null)
                            Log.e("DarkVibrant",String.format("#%06X", (0xFFFFFF &palette.getDarkVibrantSwatch().getRgb())));
                            if(palette.getLightVibrantSwatch()!= null)
                            Log.e("LightVibrant",String.format("#%06X", (0xFFFFFF &palette.getLightVibrantSwatch().getRgb())));



                        }
                    });}

                // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));

//                musicSrv.playSong();
            }
        }));






//      EventBus.getDefault().post(new EventSong(songList));
//
//        songView.setAdapter(songAdt);
//        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                EventBus.getDefault().post(new GetMusicId(position));
////                musicSrv.setSong(position);
////                musicSrv.playSong();
//              Log.e("position",String.valueOf(position));
//
//            }
//        });
        return view1;
    }
    @Subscribe
    public void onEvent(final EventSong event) {




    }
    public void getSongList() {
        ContentResolver musicResolver = getActivity().getApplicationContext().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns

            int songIndex= 0;
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
                String thisdateAdded=musicCursor.getString(dateAdded);

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
                songs.add(new Songs(thisId, thisTitle, thisArtist, thisArtistid, Long.parseLong(thisdateAdded) ,thisAlbum,thisAlbumid,albumArtUri,songIndex));



                songIndex++;
            }
            while (musicCursor.moveToNext());
        }
        //retrieve song info

    }


   

}
