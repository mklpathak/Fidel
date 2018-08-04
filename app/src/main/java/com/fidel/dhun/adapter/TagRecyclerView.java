package com.fidel.dhun.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fidel.dhun.R;
import com.fidel.dhun.data.Tagforrecylerview;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fidel on 11/26/17.
 */

public class TagRecyclerView extends RecyclerView.Adapter<TagRecyclerView.MyViewHolder> implements View.OnClickListener {
    private ArrayList<Tagforrecylerview> tag;
    String backColor = "#ffffff";
    String textColor = "#ffffff";
    private TagRecyclerView.OnItemClickListener Artistlistener;
    Context mContext;
    int pager;
    Drawable placeHolder;


    public TagRecyclerView(Context mContext, ArrayList<Tagforrecylerview> tag, int pager, TagRecyclerView.OnItemClickListener listener) {

        super();
        this.pager = pager;
        this.mContext = mContext;
        this.tag = tag;
        this.Artistlistener = listener;

        //songInf=LayoutInflater.from(c);
    }


    @Override
    public TagRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tagbackground, parent, false);
        return new TagRecyclerView.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TagRecyclerView.MyViewHolder holder, int position) {


        holder.bind(tag.get(position), Artistlistener, holder.tagImage);
        Tagforrecylerview fetchTag = tag.get(position);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            holder.tagImage.setTransitionName(fetchTag.getName());

        }
        //holder.artistImage.setImageURI(fetchSongs.getAlbumarturi());


        holder.tagTitle.setText(fetchTag.getName().substring(0, 1).toUpperCase() + fetchTag.getName().substring(1));
        Picasso.with(mContext).load(fetchTag.getUrl()).

                into(holder.tagImage);
        Picasso.with(mContext)
                .load(fetchTag.getUrl())
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




                                    holder.tagImageforground.setBackgroundColor(Color.parseColor(backColor));



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

    @Override
    public int getItemCount() {
        return tag.size();
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tagText)
        TextView tagTitle;




        @BindView(R.id.tagbackgroundImage)
        ImageView tagImage;
        @BindView(R.id.tagbackgroundImageforground)
        View tagImageforground;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            //  artistImage = (SimpleDraweeView) itemView.findViewById(R.id.itemImage2);

        }

        public void bind(final Tagforrecylerview item, final TagRecyclerView.OnItemClickListener listener, final ImageView imageView) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, imageView);
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
        void onItemClick(Tagforrecylerview item, ImageView image);
    }

}

