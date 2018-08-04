package com.fidel.dhun.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fidel.dhun.R;
import com.fidel.dhun.data.ArtistInfo;
import com.fidel.dhun.data.ArtistPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fidel on 10/1/17.
 */

public class ArtistRecyclerView extends RecyclerView.Adapter<ArtistRecyclerView.MyViewHolder> implements View.OnClickListener {
    private ArrayList<ArtistPojo> artist;
    String backColor = "#ffffff";
    String textColor = "#ffffff";
    private OnItemClickListener Artistlistener;
    Context mContext;
    int pager;
    Drawable placeHolder ;
    ArtistInfo artistInfo;







    public ArtistRecyclerView(Context mContext, ArrayList<ArtistPojo> artist, int pager, OnItemClickListener listener){

        super();
        this.pager = pager;
        this.mContext = mContext;
        this.artist =artist;
        this.Artistlistener = listener;
        artistInfo = new ArtistInfo(mContext);

        //songInf=LayoutInflater.from(c);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noqplayingartist, parent, false);
                int height = itemView.getWidth();
                    itemView.setMinimumHeight(height);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.bind(artist.get(position), Artistlistener,holder.artistImage);
        final ArtistPojo fetchArtist = artist.get(position);
        if(pager ==0)
        {holder.artistTitle.setText(fetchArtist.getName());}


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            holder.artistImage.setTransitionName(fetchArtist.getName());

        }
        //holder.artistImage.setImageURI(fetchSongs.getAlbumarturi());
        Picasso.with(mContext).load(fetchArtist.getImageuri()).placeholder(R.drawable.artistplaceholder).

                into(holder.artistImage);
//        Picasso.with(mContext)
//                .load(fetchArtist.getImageuri())
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
//
//
//                        final float[] color = new float[3],
//                                transparent= new float[3];
//
//
//
//                        if (bitmap != null) {
//                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                                @Override
//                                public void onGenerated(Palette palette) {
//
//
//                                    if(palette.getDarkMutedSwatch()!= null)
//                                        backColor =    String.format("#%06X", (0xFFFFFF &palette.getDarkMutedSwatch().getRgb()));
//                                    if(palette.getMutedSwatch()!= null)
//                                      //  Log.e("Muted",String.format("#%06X", (0xFFFFFF &palette.getMutedSwatch().getRgb())));
//                                    if(palette.getLightMutedSwatch()!= null)
//                                        //   defaultbackgroundforConInCC=  String.format("#%06X", (0xFFFFFF &palette.getLightMutedSwatch().getRgb()));
//                                        if(palette.getVibrantSwatch()!= null)
//                                        //    Log.e("VibrantMuted",String.format("#%06X", (0xFFFFFF &palette.getVibrantSwatch().getRgb())));
//                                    if(palette.getDarkVibrantSwatch()!= null)
//                                      // textColor = String.format("#%06X", (0xFFFFFF &palette.getDarkVibrantSwatch().getRgb()));
//                                        if(palette.getLightVibrantSwatch()!= null)
//                                           textColor = String.format("#%06X", (0xFFFFFF &palette.getLightVibrantSwatch().getRgb()));
//
//                                    Color.colorToHSV(Color.parseColor(backColor),color);
//
//                                    Color.colorToHSV(Color.parseColor("#00FFFFFF"),transparent);
//
//                                    GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]  {Color.HSVToColor(color),Color.argb(0,0,0,0)});
//                                    gradient.setShape(GradientDrawable.RECTANGLE);
//
//
//
//                                    holder.artistTitle.setBackground(gradient);
//                                    holder.artistTitle.setTextColor(Color.parseColor("#ffffff"));
//
//
//
//
//
//
//
//
//                                }
//                            });}
//
//
//
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]  {Color.parseColor("#000000"),Color.argb(0,0,0,0)});
                                    gradient.setShape(GradientDrawable.RECTANGLE);



                                    holder.artistTitle.setBackground(gradient);
                                    holder.artistTitle.setTextColor(Color.parseColor("#ffffff"));



    }
    @Override
    public int getItemCount() {
        return artist.size();
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
        @BindView(R.id.tvTitle2)
        TextView artistTitle;

        @BindDrawable(R.drawable.music)
        Drawable music;


        @BindView(R.id.itemImage2)
        ImageView artistImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



           //  artistImage = (SimpleDraweeView) itemView.findViewById(R.id.itemImage2);

        }
        public void bind(final ArtistPojo item, final OnItemClickListener listener, final ImageView imageView) {

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
        void onItemClick(ArtistPojo item, ImageView image);
    }
}