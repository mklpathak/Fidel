package com.fidel.dhun.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fidel.dhun.R;
import com.fidel.dhun.data.CustomisedList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fidel on 8/20/17.
 */

public class SongHorizontalAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private ArrayList<CustomisedList> item;
    String backColor = "#ffffff";
    String textColor = "#ffffff";
    String uri = "";
    Integer removeelements = 0 ;
    Boolean leftoffset = false;


    private SongHorizontalAdapter.OnItemClickListener listener;
    Context mContext;







    public SongHorizontalAdapter(Context mContext, Boolean leftOfset, ArrayList<CustomisedList> songs, OnItemClickListener listener){
        super();
        this.mContext=mContext;
        this.item =songs;
        this.listener = listener;
        this.leftoffset = leftOfset;
        if(leftOfset == true)
            removeelements = 0;
        else
            removeelements = 2;

        //songInf=LayoutInflater.from(c);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontalcard, parent, false);

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transparentview, parent, false);
                return new TranparentHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontalcard, parent, false);
                return new SongHorizontalAdapter.MyViewHolder(view);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {




            if (leftoffset == false && (position == 0 || position == 1)) {


            } else {
                (

                        (SongHorizontalAdapter.MyViewHolder) holder).bind(item.get(position - removeelements), listener, ((MyViewHolder) holder).albumArt);
                CustomisedList fetchSongs = item.get(position - removeelements);

                switch (fetchSongs.getType()) {
                    case 0:
                        ((MyViewHolder) holder).songName.setText(fetchSongs.getArtist().getName());
                        ((MyViewHolder) holder).artistName.setVisibility(View.INVISIBLE);
                        Glide.with(mContext).load(fetchSongs.getArtist().getImageuri()).into(((MyViewHolder) holder).albumArt);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            ((MyViewHolder) holder).albumArt.setTransitionName(fetchSongs.getArtist().getName());

                        }
                        uri = fetchSongs.getArtist().getImageuri();
                        break;
                    case 1:
                        ((MyViewHolder) holder).songName.setText(fetchSongs.getSong().getTitle());
                        ((MyViewHolder) holder).artistName.setText(fetchSongs.getSong().getArtist());
                        Glide.with(mContext).load(fetchSongs.getSong().getAlbumarturi()).into(((MyViewHolder) holder).albumArt);
                        uri = String.valueOf(fetchSongs.getSong().getAlbumarturi());
                        break;
                    case 2:
                        ((MyViewHolder) holder).songName.setText(fetchSongs.getAlbum().getAlbum());
                        ((MyViewHolder) holder).artistName.setVisibility(View.INVISIBLE);
                        Glide.with(mContext).load(fetchSongs.getAlbum().getAlbumarturi()).into(((MyViewHolder) holder).albumArt);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            ((MyViewHolder) holder).albumArt.setTransitionName(fetchSongs.getAlbum().getAlbum());

                        }
                        uri = String.valueOf(fetchSongs.getAlbum().getAlbumarturi());
                        break;

                }

                Picasso.with(mContext)
                        .load(uri)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                                final float[] color = new float[3],
                                        transparent = new float[3];


                                if (bitmap != null) {
                                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {


                                            if (palette.getDarkMutedSwatch() != null)
                                                backColor = String.format("#%06X", (0xFFFFFF & palette.getDarkMutedSwatch().getRgb()));
                                            if (palette.getMutedSwatch() != null)
                                                //  Log.e("Muted",String.format("#%06X", (0xFFFFFF &palette.getMutedSwatch().getRgb())));
                                                if (palette.getLightMutedSwatch() != null)
                                                    //   defaultbackgroundforConInCC=  String.format("#%06X", (0xFFFFFF &palette.getLightMutedSwatch().getRgb()));
                                                    if (palette.getVibrantSwatch() != null)
                                                        //    Log.e("VibrantMuted",String.format("#%06X", (0xFFFFFF &palette.getVibrantSwatch().getRgb())));
                                                        if (palette.getDarkVibrantSwatch() != null)
                                                            // textColor = String.format("#%06X", (0xFFFFFF &palette.getDarkVibrantSwatch().getRgb()));
                                                            if (palette.getLightVibrantSwatch() != null)
                                                                textColor = String.format("#%06X", (0xFFFFFF & palette.getLightVibrantSwatch().getRgb()));

                                            Color.colorToHSV(Color.parseColor(backColor), color);

                                            Color.colorToHSV(Color.parseColor("#00FFFFFF"), transparent);

                                            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{Color.HSVToColor(color), Color.argb(0, 0, 0, 0)});
                                            gradient.setShape(GradientDrawable.RECTANGLE);


                                            ((MyViewHolder) holder).background.setBackground(gradient);


                                        }
                                    });
                                }


                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });


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
        if(leftoffset == false && (position == 0 || position ==1))
            return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {
        return item.size()+removeelements;
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
        class TranparentHolder extends  RecyclerView.ViewHolder {

    @BindView(R.id.transparent)
    View transparent;


    public TranparentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.artistNameinHorizontalCard)
        TextView artistName;
        @BindView(R.id.songTitleInHorizontal)
        TextView songName;



        @BindView(R.id.artistImageInCard)
        ImageView albumArt;
        @BindView(R.id.horizontalImageBackground)
        View background;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
        public void bind(final CustomisedList item, final SongHorizontalAdapter.OnItemClickListener listener, final ImageView imageView) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item,imageView);
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
        void onItemClick(CustomisedList item,ImageView imageView);
    }
}
