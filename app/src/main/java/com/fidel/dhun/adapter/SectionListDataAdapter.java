package com.fidel.dhun.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fidel.dhun.R;
import com.fidel.dhun.SharedAlbum;
import com.fidel.dhun.data.CustomisedList;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.HorizonalonClicklistener;
import com.fidel.dhun.data.Songs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionListDataAdapter extends RecyclerView.Adapter implements View.OnClickListener {
private ArrayList<CustomisedList> item;
private ArrayList<Songs> songs;
        String backColor = "#ffffff";
        String textColor = "#ffffff";
        String uri = "";
        Integer addElements = 0 ;
        Boolean containeshorizontallayout= false;

        String debugtag= "Debug";


     private   HorizonalonClicklistener horizonalonClicklistener;
        Context mContext;

        DatabaseSync databaseSync;






public SectionListDataAdapter(Context mContext, ArrayList<Songs> songs, ArrayList<CustomisedList> item,  HorizonalonClicklistener listener){
    super();


        this.mContext=mContext;
        this.item =item;
        this.songs = songs;
        this.horizonalonClicklistener = listener;

        if(item.size()>2){
        addElements = 1;
        this.containeshorizontallayout =true;}
        else{
         this.containeshorizontallayout =false;
        addElements = 0;
        databaseSync = new DatabaseSync(mContext);
        }


        //songInf=LayoutInflater.from(c);
        }



@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View view;
        switch (viewType) {
        case 0:
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontalrecyler, parent, false);
        return new FirstItem(view);
        case 1:
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs, parent, false);
        return new SectionListDataAdapter.MyViewHolder(view);

        }
        return null;

        }

@Override
public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

    if (position==0 && containeshorizontallayout){

        ((FirstItem) holder).cardTitle.setText("More By "+item.get(item.size() - 1).getArtist().getName());
        ((FirstItem) holder).datatoshow.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        ((FirstItem) holder).datatoshow.setAdapter(new SongHorizontalAdapter(mContext, true, item, new SongHorizontalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CustomisedList items, ImageView imageView) {
                switch (items.getType()) {
                    case 0:
                        Intent sharedElem = new Intent(mContext,SharedAlbum.class);
                        sharedElem.putExtra("activity","artist");
                        sharedElem.putExtra("shared",items.getArtist().getName());
                        sharedElem.putExtra("imageuri",String.valueOf(items.getArtist().getImageuri()));
                        sharedElem.putExtra("artistname",items.getArtist().getName());
                        //   sharedElem.putExtra("list",tempArrayList);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,imageView,items.getArtist().getName());

                        mContext.startActivity(sharedElem,optionsCompat.toBundle());

                        break;
                    case 1:
                        if(item!=null && item.size()>=2) {

                            Log.e(debugtag,String.valueOf(item.get(item.size()-1).getArtist().getName()));
                            databaseSync = new DatabaseSync(mContext);


                            horizonalonClicklistener.getSong(items.getSong(),databaseSync.getArtistDetails(item.get(item.size() - 1).getArtist().getName()));


                        }
                        break;
                    case 2:
                        Intent sharedElem2 = new Intent(mContext,SharedAlbum.class);
                        sharedElem2.putExtra("activity","album");
                        sharedElem2.putExtra("shared",items.getArtist().getName());
                        sharedElem2.putExtra("imageuri",String.valueOf(items.getArtist().getImageuri()));
                        sharedElem2.putExtra("artistname",items.getArtist().getName());
                        //   sharedElem.putExtra("list",tempArrayList);
                        ActivityOptionsCompat optionsCompat1 = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,imageView,items.getArtist().getName());

                        mContext.startActivity(sharedElem2,optionsCompat1.toBundle());
                        break;

                }


            }
        }));
    }
    else {

        ((MyViewHolder) holder).bind(songs.get(position - addElements),horizonalonClicklistener );
        Songs fetchSongs = songs.get(position-addElements);
        ((MyViewHolder) holder).songTitle.setText(fetchSongs.getTitle());
        ((MyViewHolder) holder).songArtist.setText(fetchSongs.getArtist());
//        holder.albumArt.setImageURI(fetchSongs.getAlbumarturi());
        Glide.with(mContext).load(fetchSongs.getAlbumarturi()).placeholder(R.drawable.music).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

                .thumbnail(0.5f).into(((MyViewHolder) holder).albumArt);


    }









}
//
//    @Override
//    public void onBindViewHolder(SongHorizontalAdapter.MyViewHolder holder, int position) {
//
//
//    }

@Override
public int getItemViewType(int position) {
        if (position==0 && containeshorizontallayout)
        return 0;
        else return 1;
        }

@Override
public int getItemCount() {
        return songs.size()+ addElements;
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
class FirstItem extends  RecyclerView.ViewHolder {

    @BindView(R.id.cardTitle)
    TextView cardTitle;
    @BindView(R.id.datatoshow)
    RecyclerView datatoshow;


    public FirstItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

}
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
    public void bind(final Songs item, final HorizonalonClicklistener listener) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.getSong(item,songs);
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

}
