package com.fidel.dhun.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fidel.dhun.R;
import com.fidel.dhun.data.Api;
import com.fidel.dhun.data.ApiUtils;
import com.fidel.dhun.data.ArtistInfo;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.LoaderCommunication;
import com.fidel.dhun.data.Songs;
import com.fidel.dhun.data.Tagforrecylerview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import agency.tango.materialintroscreen.SlideFragment;


public class Customintro extends SlideFragment {
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
    ProgressBar materialProgressBar;
    float Progress=0;
    Boolean canMoveFurther = false;

    TextView textView;

Boolean firstTime = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_customintro, container, false);
        SharedPreferences settings = getContext().getSharedPreferences("Settings", 0);
        EventBus.getDefault().register(this);
        materialProgressBar = (ProgressBar) view.findViewById(R.id.progressBarforintro);



        dbsync = new DatabaseSync(getContext());
        artistSync = new ArtistInfo(getContext());


        songs =new ArrayList<Songs>();
        artistList =new ArrayList<Songs>();
        taglist =new ArrayList<Tagforrecylerview>();
         textView = (TextView) view.findViewById(R.id.Stauts);
        artistApi = ApiUtils.getArtist();
        boolean previouslyStarted = settings.getBoolean("started", false);





        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.colorPrimary;
    }


    @Override
    public int buttonsColor() {
        return R.color.colorAccent;
    }
    @Override
    public boolean canMoveFurther() {
        return canMoveFurther;
    }




    @Override
    public String cantMoveFurtherErrorMessage() {
        return ("hello");
    }
    @Subscribe
    public void onEvent(LoaderCommunication loaderCommunication){

         Progress = ((float) loaderCommunication.getCurrentcalls()/loaderCommunication.getTotalcalls())*100;

        materialProgressBar.setProgress((int) Progress);
        int prog = (int) Progress;
        textView.setText(String.valueOf((int) Progress)+" % Completed");

        if(Progress==100){

            canMoveFurther=true;
        }




        Log.e("Progress:",String.valueOf(Progress));



    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
