package com.fidel.dhun;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fidel.dhun.data.Songs;

/**
 * Created by fidel on 8/11/17.
 */

public class SongsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private  ImageView albumart;
    private  TextView songtitle;
    private  TextView songArtist;


    private Songs songs;
    private Context context;

    public SongsHolder(Context context, View itemView) {

        super(itemView);

        // 1. Set the context
        this.context = context;

        // 2. Set up the UI widgets of the holder
        this.albumart = (ImageView) itemView.findViewById(R.id.imageView2);
        this.songtitle = (TextView) itemView.findViewById(R.id.song_title);
        this.songArtist = (TextView) itemView.findViewById(R.id.song_artist);


        // 3. Set the "onClick" listener of the holder
        itemView.setOnClickListener(this);
    }

    public void bindSongs(Songs song) {

        // 4. Bind the data to the ViewHolder
        this.songs = song;
        this.songtitle.setText(song.getTitle());
        this.songArtist.setText(song.getArtist());
        this.albumart.setImageURI(song.getAlbumarturi());

    }

    @Override
    public void onClick(View v) {

        // 5. Handle the onClick event for the ViewHolder
        if (this.songs != null) {

            Toast.makeText(this.context, "Clicked on " + this.songs.getTitle(), Toast.LENGTH_SHORT ).show();
        }
    }
}
