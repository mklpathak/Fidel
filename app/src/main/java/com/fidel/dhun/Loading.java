package com.fidel.dhun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cunoraz.gifview.library.GifView;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        GifView gifView1 = (GifView) findViewById(R.id.gif1);
        gifView1.setVisibility(View.VISIBLE);
        gifView1.play();

        gifView1.setGifResource(R.drawable.loadingcat2);
        gifView1.getGifResource();


    }

}
