package com.fidel.dhun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fidel.dhun.R;
import com.fidel.dhun.data.Songs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mukul on 30-07-2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements View.OnClickListener {
    private ArrayList<Songs> songs;

    private OnItemClickListener listener;
    Context context;







    public SongAdapter(Context context,ArrayList<Songs> songs, OnItemClickListener listener){
        super();
        this.songs =songs;
        this.listener = listener;
        this.context = context;

        //songInf=LayoutInflater.from(c);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songs, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(songs.get(position), listener);
        Songs fetchSongs = songs.get(position);
        holder.songTitle.setText(fetchSongs.getTitle());
        holder.songArtist.setText(fetchSongs.getArtist());
//        holder.albumArt.setImageURI(fetchSongs.getAlbumarturi());
        Glide.with(context).load(fetchSongs.getAlbumarturi()).placeholder(R.drawable.music).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

                .thumbnail(0.5f).into(holder.albumArt);







    }
    @Override
    public int getItemCount() {
        return songs.size();
    }

//    public void swapCursor(ArrayList<Songs> SongList) {
//        this.songs = SongList;
//        notifyDataSetChanged();
//    }

    @Override
    public void onClick(View view) {

    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        ConstraintLayout songLay = (ConstraintLayout) songInf.inflate
//                (R.layout.songs, parent, false);
//        //get title and artist views
//        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
//        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
//        ImageView albumart = (ImageView)songLay.findViewById(R.id.imageView2);
//        //get song using position
//        Songs currSong = songs.get(position);
//        //get title and artist strings
//        songView.setText(currSong.getTitle());
//        artistView.setText(currSong.getArtist());
//        albumart.setImageURI(currSong.getAlbumarturi());
//        //set position as tag
//        songLay.setTag(position);
//        return songLay;
//    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.song_title)
        TextView songTitle;

        @BindView(R.id.song_artist)
        TextView songArtist;
        @BindView(R.id.imageView2)
        ImageView albumArt;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
        public void bind(final Songs item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        @Override
        public void onClick(View view) {



        }

   //     @Override
//        public boolean onLongClick(View view) {
//            cursor.moveToPosition(getAdapterPosition());
//            int name = cursor.getInt(cursor.getColumnIndex(DatabasePanorbit.ID));
//            onMedicineItemClicked.onMedicineLongClicked(name);
//            return false;
//        }
    }
    public interface OnItemClickListener {
        void onItemClick(Songs item);
    }
}