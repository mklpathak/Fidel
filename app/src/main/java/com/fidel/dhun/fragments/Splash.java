package com.fidel.dhun.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fidel.dhun.R;
import com.fidel.dhun.data.OnConnected;

import java.io.IOException;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Splash extends SlideFragment {

    OnConnected onConnected;


    Boolean canMoveFurther = false;

    TextView textView;
    Handler handler;

    Boolean firstTime = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_splash, container, false);
        if (getActivity() instanceof OnConnected)
            onConnected = (OnConnected) getActivity();
        TextView title = (TextView) view.findViewById(R.id.maintitle);
        TextView artist = (TextView) view.findViewById(R.id.artisttitle);
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        TextView bottomtext = (TextView) view.findViewById(R.id.bottomtext);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Selima.ttf");
        Typeface titletypeface = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/F.otf");
        Typeface Bottomtext = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/blowbrush.otf");


        title.setText("Maa tujhe salaam\n" +
                "Amma tujhe salaam\n" +
                "Vande maataram,");
        title.setTypeface(custom_font);
        Logo.setTypeface(titletypeface);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        Logo.startAnimation(animationFadeIn);




        artist.setTypeface(custom_font);
        bottomtext.setTypeface(Bottomtext);
        bottomtext.startAnimation(animationFadeIn);
        final Button tryAgain =(Button) view.findViewById(R.id.isConnected);

        try {
            if(!isConnected()){


                tryAgain.setVisibility(View.VISIBLE);
                tryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if(isConnected()){

                            onConnected.onConnected(isConnected());
                            canMoveFurther=true;
                            tryAgain.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Network Connected" , Toast.LENGTH_SHORT).show();


                            }
                            else {
                                Toast.makeText(getContext(), "Unable to detect a network!\nPlease turn on the internet and click try again." , Toast.LENGTH_LONG).show();


                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
            else {
                canMoveFurther =true;
            }










        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities

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


    @Override
    public void onDestroy() {

        super.onDestroy();
    }
    public boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }
}
