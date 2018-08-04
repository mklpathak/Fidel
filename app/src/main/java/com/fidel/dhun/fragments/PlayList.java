package com.fidel.dhun.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fidel.dhun.R;
import com.fidel.dhun.SharedAlbum;

import com.fidel.dhun.adapter.TagRecyclerView;
import com.fidel.dhun.data.ArtistInfo;
import com.fidel.dhun.data.ArtistPojo;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.PopularArtist;
import com.fidel.dhun.data.Songs;
import com.fidel.dhun.data.Tagforrecylerview;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PlayList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayList extends Fragment {


    ArrayList<Songs> getSongList;
    ArrayList<ArtistPojo> artistDetails;
    ArrayList<Tagforrecylerview> tagList;
    ArrayList<Tagforrecylerview> tagListdemo;
String TopArtistName = "";

    ArtistInfo artistInfo;
    DatabaseSync dbSync;
    String topArtistName;
    String setBackgroundcolor = "";

    ImageView recentlyaddedbackground;
    View recentlyaddedbackgroundsideview;
    View recentlyaddedbackgroundforground;
    CardView recentlyadded;
    RecyclerView tags;
    GridLayoutManager lLayout;





    public static PlayList newInstance() {
        PlayList fragment = new PlayList();
        return fragment;
    }


    public PlayList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlayList.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=  inflater.inflate(R.layout.fragment_play_list, container, false);
        ButterKnife.bind(getActivity());
        recentlyaddedbackground = (ImageView) view.findViewById(R.id.recentlyaddedbackground);
        recentlyaddedbackgroundforground = (View) view.findViewById(R.id.recentlyaddedbackgroundforground) ;
        recentlyaddedbackgroundsideview = (View) view.findViewById(R.id.recentlyaddedbackgroundsideview) ;
        tags = (RecyclerView) view.findViewById(R.id.recenttags);
        recentlyadded= (CardView) view.findViewById(R.id.recentlyaddedcardview);


        artistInfo = new ArtistInfo(getActivity());
        dbSync = new DatabaseSync(getActivity());




//        for(Tagforrecylerview t:tagList){
//
//            Log.e("Tag is "," "+t.getName()+" and count is "+ t.getCount());
//        }



        Backgroundprocessing updatelayout = new Backgroundprocessing();
        updatelayout.execute();
        UpdateRecyclerViewonBackground updateRecyclerview = new UpdateRecyclerViewonBackground();


        lLayout = new GridLayoutManager(getContext(), 2);

        tagListdemo = new ArrayList<Tagforrecylerview>();
        if(artistInfo.getallTags()!=null && artistInfo.getallTags().size()>=1) {

            tagListdemo = artistInfo.getallTags();

            tags.setAdapter(new TagRecyclerView(getContext(), artistInfo.getallTags(), 1, new TagRecyclerView.OnItemClickListener() {
                @Override
                public void onItemClick(Tagforrecylerview item, ImageView imageView) {

                    Intent recentlyadded = new Intent(getActivity(), SharedAlbum.class);
                    recentlyadded.putExtra("activity","tags");

                    recentlyadded.putExtra("shared", item.getName());
                    recentlyadded.putExtra("imageuri", String.valueOf(item.getUrl()));
                    //   sharedElem.putExtra("list",tempArrayList);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, item.getName());
                    startActivity(recentlyadded, optionsCompat.toBundle());


                }
            }));
        }
        else {
            updateRecyclerview.execute();

        }


        tags.setLayoutManager(lLayout);
        tags.setHasFixedSize(true);



        // Inflate the layout for this fragment
        return view;
    }

    public String getTopartist() {

        int noofSongs=0;




      ArrayList  getArtistList = new ArrayList<PopularArtist>();
        artistDetails = new ArrayList<ArtistPojo>();
        artistDetails = artistInfo.getAllArtist();


        for(ArtistPojo artistPojo:artistDetails ){

            if(artistPojo!=null){


                    if(dbSync.getArtistDetails(artistPojo.getName())!=null){
                    if (noofSongs < (dbSync.getArtistDetails(artistPojo.getName()).size())) {

                        topArtistName = artistPojo.getName();
                        noofSongs = (dbSync.getArtistDetails(artistPojo.getName()).size());
                    }}

            }

        }


       return topArtistName;
    }

    class Backgroundprocessing extends AsyncTask<String,
            String,String>{

        String topArtist = "";

        @Override
        protected String doInBackground(String... strings) {
            topArtist = getTopartist();


            return topArtist;
        }

        @Override
        protected void onPostExecute(String topArtist) {

            Glide.with(getActivity()).load(artistInfo.getArtistDetails(topArtist).getImageuri()).crossFade().centerCrop().into(recentlyaddedbackground);

            Picasso.with(getActivity())
                    .load(artistInfo.getArtistDetails(topArtist).getImageuri())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){


                            final float[] color = new float[3],
                                    transparent= new float[3];



                            if (bitmap != null) {
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {


                                        if(palette.getDarkMutedSwatch()!= null)
                                            setBackgroundcolor =    String.format("#%06X", (0xFFFFFF &palette.getDarkMutedSwatch().getRgb()));
//                                    if(palette.getMutedSwatch()!= null)
//                                        //  Log.e("Muted",String.format("#%06X", (0xFFFFFF &palette.getMutedSwatch().getRgb())));
//                                        if(palette.getLightMutedSwatch()!= null)
//                                            //   defaultbackgroundforConInCC=  String.format("#%06X", (0xFFFFFF &palette.getLightMutedSwatch().getRgb()));
//                                            if(palette.getVibrantSwatch()!= null)
//                                                //    Log.e("VibrantMuted",String.format("#%06X", (0xFFFFFF &palette.getVibrantSwatch().getRgb())));
//                                                if(palette.getDarkVibrantSwatch()!= null)
//                                                    // textColor = String.format("#%06X", (0xFFFFFF &palette.getDarkVibrantSwatch().getRgb()));
//                                                    if(palette.getLightVibrantSwatch()!= null)
                                        if(!setBackgroundcolor.equals("")) {
                                            Color.colorToHSV(Color.parseColor(setBackgroundcolor), color);

                                            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{Color.HSVToColor(color), Color.argb(0, 0, 0, 0)});
                                            gradient.setShape(GradientDrawable.RECTANGLE);
                                            recentlyaddedbackgroundforground.setBackground(gradient);
                                            recentlyaddedbackgroundsideview.setBackgroundColor(Color.parseColor(setBackgroundcolor));

                                        }





                                    }
                                });}



                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
            TopArtistName = topArtist;

            recentlyaddedbackground.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fadein));
            recentlyaddedbackgroundforground.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fadein));
            recentlyaddedbackgroundsideview.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fadein));
            recentlyaddedbackground.setTransitionName("recentlyadded");
            recentlyadded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent recentlyadded = new Intent(getActivity(), SharedAlbum.class);
                    recentlyadded.putExtra("activity","tags");

                    recentlyadded.putExtra("shared", "recentlyadded");
                    recentlyadded.putExtra("imageuri", String.valueOf(artistInfo.getArtistDetails(TopArtistName).getImageuri()));
                    //   sharedElem.putExtra("list",tempArrayList);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), recentlyaddedbackground, "recentlyadded");
                    startActivity(recentlyadded, optionsCompat.toBundle());
                }
            });

            super.onPostExecute(TopArtistName);
        }
    }

    class UpdateRecyclerViewonBackground extends AsyncTask<String,Integer,ArrayList<Tagforrecylerview>>{


        @Override
        protected ArrayList<Tagforrecylerview> doInBackground(String... strings) {

            ArrayList<String> demolisttags = new ArrayList<String>();
            ArrayList<String> demolisttags2 = new ArrayList<String>();
            tagList = new ArrayList<Tagforrecylerview>();
            demolisttags =getToptags();
            Collections.sort(demolisttags, new Comparator<String>() {
                public int compare(String a, String b) {
                    return a.compareTo(b);
                }
            });
            String prevTag = "johfhoehriferiopij";
            String currentArtistName= "";
            int tagCount = 0;
            for (String s:demolisttags){
                if(!s.contains(prevTag)){

                    prevTag=s;
                    demolisttags2.add(s);

                }
            }

            for (String s:demolisttags2){
                if(!s.contains("'")) {
                    for (ArtistPojo artistPojo : artistInfo.getAllArtistbyTag(s)) {
                        if (dbSync.getArtistDetails(artistPojo.getName()) != null) {
                            if (tagCount < dbSync.getArtistDetails(artistPojo.getName()).size()) {
                                tagCount = dbSync.getArtistDetails(artistPojo.getName()).size();

                                currentArtistName = artistPojo.getName();


                            }
                        }
                    }

                    tagList.add(new Tagforrecylerview(artistInfo.getAllArtistbyTag(s).size()
                            , s, artistInfo.getArtistDetails(currentArtistName).getImageuri()));
                    artistInfo.addTags(new Tagforrecylerview(artistInfo.getAllArtistbyTag(s).size()
                            , s, artistInfo.getArtistDetails(currentArtistName).getImageuri()));
                    currentArtistName = "";
                    tagCount = 0;
                }
            }
            Collections.sort(tagList, new Comparator<Tagforrecylerview>() {
                public int compare(Tagforrecylerview a, Tagforrecylerview b) {
                    return a.getCount().compareTo(b.getCount());
                }
            });
            Collections.reverse(tagList);


            return tagList;
        }

        @Override
        protected void onPostExecute(ArrayList<Tagforrecylerview> tagforrecylerview) {

            tags.setAdapter(new TagRecyclerView(getContext(), tagforrecylerview, 1, new TagRecyclerView.OnItemClickListener() {
                @Override
                public void onItemClick(Tagforrecylerview item, ImageView imageView) {

                    Intent recentlyadded = new Intent(getActivity(), SharedAlbum.class);
                    recentlyadded.putExtra("activity","tags");

                    recentlyadded.putExtra("shared", item.getName());
                    recentlyadded.putExtra("imageuri", String.valueOf(item.getUrl()));
                    //   sharedElem.putExtra("list",tempArrayList);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, item.getName());
                    startActivity(recentlyadded, optionsCompat.toBundle());



                }
            }));


            super.onPostExecute(tagforrecylerview);
        }
    }



    ArrayList<String> getToptags(){
        ArrayList<String> tags = new ArrayList<String>();

        for(ArtistPojo s:artistInfo.getAllArtist()){

            if(s!=null){

                if(!s.getTags().equals("")){

                    String[] tag = s.getTags().split(",");

                    for (String t:tag){

                        tags.add(t.toLowerCase());
                    }

                }

            }


        }
        return tags;
    }


}

