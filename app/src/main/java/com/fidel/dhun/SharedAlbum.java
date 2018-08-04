package com.fidel.dhun;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fidel.dhun.adapter.SectionListDataAdapter;
import com.fidel.dhun.adapter.SongAdapter;
import com.fidel.dhun.adapter.SongHorizontalAdapter;
import com.fidel.dhun.data.ArtistInfo;
import com.fidel.dhun.data.ArtistPojo;
import com.fidel.dhun.data.CustomisedList;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.HorizonalonClicklistener;
import com.fidel.dhun.data.NestedRecyclerData;
import com.fidel.dhun.data.Songs;
import com.fidel.dhun.data.api.Image;
import com.fidel.dhun.data.updateui.UpdateUi;
import com.fidel.dhun.data.updateui.UpdateuiforsharedArtist;
import com.fidel.dhun.services.MusicService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.fidel.dhun.R.drawable.pause;

/**
 * Created by fidel on 11/18/17.
 */

public class SharedAlbum extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    String backColor ="#000000";
    String textColor = "#ffffff";
    ImageView sharedImage;

    ArtistInfo artistInfo ;


    SimpleDraweeView AlbumartinShared;
    SimpleDraweeView albumArtNowPlayingInShared;
    private SlidingUpPanelLayout sharedpanel;
    private SlidingUpPanelLayout sharedpanelinNowplaying;
    SeekBar seekBarInShared;
    @BindView(R.id.playPauseInShared)
    ImageView playpauseInShared;
    @BindView(R.id.imagebackground)
    View imageBackground;
    @BindView(R.id.imageforgroud)
    View imageForeground;
    @BindDrawable(R.drawable.play)
    Drawable playShared;
    @BindDrawable(pause)
    Drawable pauseShared;
    @BindView(R.id.songNameInShared)
    TextView songNameInShared;
    TextView moreLike;
    @BindView(R.id.artistNameInShared)
    TextView artistNameInShared;

    @BindView(R.id.moreArtistNowPlaying)
    RecyclerView moreArtistNowPlayingInShared;
    @BindView(R.id.songTiitleNowPlaying)
    TextView songTiitleNowPlayingInShared;
    @BindView(R.id.artistNameNowPlaying)
    TextView artistNameNowPlayingInShared;
    @BindView(R.id.prevNowplaying)
    ImageView prevNowplayingInShared;
    @BindView(R.id.playNowPlaying)
    ImageView playNowPlayingInShared;
    @BindView(R.id.nextNowPlaying)
    ImageView nextNowPlayingInShared;
    @BindView(R.id.currentDuration)
    TextView currentDurationInShared;
    @BindView(R.id.artistNameShared)
    TextView artistNameShared;
    @BindView(R.id.maxDuration)
    TextView maxDurationInShared;
    @BindView(R.id.nowPlayingList)
    RecyclerView nowPlayingListInShared;

    @BindView(R.id.Verticlelistinshared)
    RecyclerView verticleListInShared;
    @BindView(R.id.consInCCInShared)
    ConstraintLayout sharedview;
    MaterialProgressBar materialProgressBarShared;
    Toolbar toolbar; //Variables
    ArrayList<Songs> songList;
    ArrayList<Songs> songListArtist;
    MusicService musicService;
    Intent playIntent;
    Handler handler = new Handler();
    Utility utils = new Utility();
    long totalDuration = 0;
    long currentDuration = 0;
    DatabaseSync dbSync;
    @BindView(R.id.artistImageinNowPlaying)
    ImageView artistImageInNowPlaying;
    @BindView(R.id.sidepaletteforartistImage)
    View sidepaletteforartistImage;
    @BindView(R.id.bottompaletteforartistImage)
    View bottompaletteforartistImage;
    @BindView(R.id.morebyartist)
    TextView moreByArtist;
    ArrayList<Songs> demoList;
    Toolbar toolbarinslideupshared;
    BackgroundProcessing backgroundProcessing;

    RecyclerView searchSongResult;
    RecyclerView searchArtist;
    RecyclerView searchAlbum;
    @BindView(R.id.songsResult)
    CardView songResult;
    @BindView(R.id.artistResult)
    CardView ArtistResult;
    @BindView(R.id.albumResult)
    CardView albumResult;
    @BindView(R.id.searchError)
    ConstraintLayout searchError;
    @BindView(R.id.errorText)
    TextView errorText;
    NestedScrollView linearLayoutsearch;
    @BindView(R.id.searchContainer)
    FrameLayout SearchContainer;


    @BindView(R.id.nowPlayingListConatiner)
    LinearLayout nowPlayingListContainer;

    String defaultbackgroundforConInCC = "#000000";
    AppBarLayout appBarLayout;


    @BindView(R.id.imagebackgroundtags)
    View imageBackgroundtags;
    ImageView sharedImage_tags;
    @BindView(R.id.artistNameShared_tags)
    TextView artistNameSharedtags;
    @BindView(R.id.imageforgroudtags)
    View imageForegroundtags;

    private boolean serviceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_element);
        ButterKnife.bind(this);
        toolbarinslideupshared = (Toolbar) findViewById(R.id.toolbarInNowPlayingInShared);


        sharedImage = (ImageView) findViewById(R.id.place_image);

        materialProgressBarShared = (MaterialProgressBar) findViewById(R.id.songProgressinshared);
        EventBus.getDefault().register(this);

        sharedpanel = (SlidingUpPanelLayout) findViewById(R.id.slidingLayoutSharedView);
        backgroundProcessing= new BackgroundProcessing();
        backgroundProcessing.execute();

        dbSync = new DatabaseSync(this);
        artistInfo = new ArtistInfo(this);







        songList = new ArrayList<Songs>();


        demoList = new ArrayList<Songs>();
        switch (getIntent().getStringExtra("activity")){

            case "artist":
//                appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
//                appBarLayout.setVisibility(View.VISIBLE);
                sharedImage = (ImageView) findViewById(R.id.place_image);
                if(sharedImage!=null) {
                    Glide.with(SharedAlbum.this).load(Uri.parse(getIntent().getStringExtra("imageuri"))).into(sharedImage);
                }
                artistNameShared.setText(getIntent().getStringExtra("artistname"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sharedImage.setTransitionName(getIntent().getStringExtra("shared"));
                }

                Picasso.with(this)
                        .load(Uri.parse(getIntent().getStringExtra("imageuri")))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                                if (bitmap != null) {
                                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onGenerated(Palette palette) {


                                            if(palette.getDarkMutedSwatch()!= null)
                                                backColor =    String.format("#%06X", (0xFFFFFF &palette.getDarkMutedSwatch().getRgb()));


                                            imageBackground.setBackgroundColor(Color.parseColor(backColor));


                                            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]  {Color.parseColor(backColor),Color.argb(0,0,0,0)});
                                            gradient.setShape(GradientDrawable.RECTANGLE);
                                            imageForeground.setBackground(gradient);
                                            Window window = SharedAlbum.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
                                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
                                            window.setStatusBarColor(Color.parseColor(backColor));


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

                verticleListInShared.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                verticleListInShared.setAdapter(new SectionListDataAdapter(getApplicationContext(), dbSync.getArtistDetails(getIntent().getStringExtra("artistname")), generaterecyclerlist(getIntent().getStringExtra("artistname"))
                        , new HorizonalonClicklistener() {
                    @Override
                    public Void getSong(Songs song, ArrayList<Songs> Songs) {
                        Log.e("hello","hello");
                        if(musicService!=null){

                            musicService.setList(Songs);
                            int songIndex = 0;

                            for (int index= 0 ; index < Songs.size();index++){

                                if(Songs.get(index).getTitle().equals(song.getTitle())){
                                    songIndex = index;
                                    break;
                                }



                            }
                            musicService.setSong(songIndex);
                            musicService.playSong();
                            updateui();
                            updateProgressBar();
                        }

                        return null;
                    }
                }));



//                horizontalListInShared.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//
//                demoList = dbSync.getArtistDetails(getIntent().getStringExtra("artistname"));
//                horizontalListInShared.setAdapter(new SongHorizontalAdapter(getApplicationContext(), false, generaterecyclerlist(getIntent().getStringExtra("artistname")), new SongHorizontalAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(CustomisedList item,ImageView imageView) {
//                        //EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Songlist.indexOf(item)))));
//                        // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
//                        //  Log.e("hello",String.valueOf(item.getID()));
////                musicSrv.playSong();
////                musicSrv.setList(songs);
////                musicSrv.setSong(songs.indexOf(item));
////                musicSrv.playSong();
//                        switch (item.getType()) {
//                            case 0:
//                                Intent sharedElem = new Intent(SharedAlbum.this,SharedAlbum.class);
//                                sharedElem.putExtra("activity","artist");
//                                sharedElem.putExtra("shared",item.getArtist().getName());
//                                sharedElem.putExtra("imageuri",String.valueOf(item.getArtist().getImageuri()));
//                                sharedElem.putExtra("artistname",item.getArtist().getName());
//                                //   sharedElem.putExtra("list",tempArrayList);
//                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this,imageView,item.getArtist().getName());
//
//                                startActivity(sharedElem,optionsCompat.toBundle());
//                                break;
//                            case 1:
//                                musicService.setList(demoList);
//                                int songIndex = 0;
//
//                                for (int index= 0 ; index < demoList.size();index++){
//
//                                    if(demoList.get(index).getTitle().equals(item.getSong().getTitle())){
//                                        songIndex = index;
//                                        break;
//                                    }
//
//
//
//                                }
//                                musicService.setSong(songIndex);
//                                musicService.playSong();
//                                break;
//                            case 2:
//                                Intent sharedElem2 = new Intent(SharedAlbum.this, SharedAlbum
//                                        .class);
//                                sharedElem2.putExtra("activity","album");
//                                sharedElem2.putExtra("shared", item.getAlbum().getAlbum());
//                                sharedElem2.putExtra("imageuri", String.valueOf(item.getAlbum().getAlbumarturi()));
//                                sharedElem2.putExtra("artistname", item.getAlbum().getAlbum());
//                                sharedElem2.putExtra("artistid", item.getAlbum().getAlbumID());
//                                //   sharedElem.putExtra("list",tempArrayList);
//                                ActivityOptionsCompat optionsCompat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this, imageView, item.getAlbum().getAlbum());
//                                startActivity(sharedElem2, optionsCompat2.toBundle());
//
//                                break;
//
//                        }
//                        updateui();
//                        updateProgressBar();
//                    }
//                }));

                //Artist activity code





                break;
            case "album":

                AppBarLayout appBarAlbum = (AppBarLayout) findViewById(R.id.app_bar);
                appBarAlbum.setVisibility(View.VISIBLE);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_appbar) ;
                setSupportActionBar(toolbar);
                Glide.with(this).load(Uri.parse(getIntent().getStringExtra("imageuri"))).into(sharedImage);
                artistNameShared.setText(getIntent().getStringExtra("artistname"));


                Picasso.with(this)
                        .load(Uri.parse(getIntent().getStringExtra("imageuri")))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){

                                if (bitmap != null) {
                                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onGenerated(Palette palette) {


                                            if(palette.getDarkMutedSwatch()!= null)
                                                backColor =    String.format("#%06X", (0xFFFFFF &palette.getDarkMutedSwatch().getRgb()));


                                            imageBackground.setBackgroundColor(Color.parseColor(backColor));


                                            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]  {Color.parseColor(backColor),Color.argb(0,0,0,0)});
                                            gradient.setShape(GradientDrawable.RECTANGLE);
                                            imageForeground.setBackground(gradient);
                                            Window window = SharedAlbum.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
                                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
                                            window.setStatusBarColor(Color.parseColor(backColor));


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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sharedImage.setTransitionName(getIntent().getStringExtra("shared"));
                }
                verticleListInShared.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


                verticleListInShared.setAdapter(new SectionListDataAdapter(getApplicationContext(),dbSync.getAlbumDetails(getIntent().getStringExtra("artistname")),generaterecyclerlist(getIntent().getStringExtra("artistname"))
                        , new HorizonalonClicklistener() {
                    @Override
                    public Void getSong(Songs song, ArrayList<Songs> Songs) {
                        if(musicService!=null){

                            musicService.setList(Songs);
                            int songIndex = 0;

                            for (int index= 0 ; index < Songs.size();index++){

                                if(Songs.get(index).getTitle().equals(song.getTitle())){
                                    songIndex = index;
                                    break;
                                }



                            }
                            musicService.setSong(songIndex);
                            musicService.playSong();
                            updateui();
                            updateProgressBar();
                        }
                        return null;

                    }
                }));
                verticleListInShared.setNestedScrollingEnabled(true);

//                horizontalListInShared.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//
//
//
//
//                horizontalListInShared.setAdapter(new SongHorizontalAdapter(getApplicationContext(), false, generaterecyclerlist(getIntent().getStringExtra("artistname")), new SongHorizontalAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(CustomisedList item,ImageView imageView) {
//                        //EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Songlist.indexOf(item)))));
//                        // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
//                        //  Log.e("hello",String.valueOf(item.getID()));
////                musicSrv.playSong();
////                musicSrv.setList(songs);
////                musicSrv.setSong(songs.indexOf(item));
////                musicSrv.playSong();
//                        switch (item.getType()) {
//                            case 0:
//                                Intent sharedElem = new Intent(SharedAlbum.this,SharedAlbum.class);
//                                sharedElem.putExtra("activity","artist");
//                                sharedElem.putExtra("shared",item.getArtist().getName());
//                                sharedElem.putExtra("imageuri",String.valueOf(item.getArtist().getImageuri()));
//                                sharedElem.putExtra("artistname",item.getArtist().getName());
//                                //   sharedElem.putExtra("list",tempArrayList);
//                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this,imageView,item.getArtist().getName());
//
//                                startActivity(sharedElem,optionsCompat.toBundle());
//
//                                break;
//                            case 1:
//                                musicService.setList(dbSync.getArtistDetails(getIntent().getStringExtra("artistname")));
//                                int songIndex = 0;
//
//                                for (int index= 0 ; index < dbSync.getArtistDetails(getIntent().getStringExtra("artistname")).size();index++){
//
//                                    if(dbSync.getArtistDetails(getIntent().getStringExtra("artistname")).get(index).getTitle().equals(item.getSong().getTitle())){
//                                        songIndex = index;
//                                        break;
//                                    }
//
//
//
//                                }
//                                musicService.setSong(songIndex);
//                                musicService.playSong();
//                                break;
//                            case 2:
//                                Intent sharedElem2 = new Intent(SharedAlbum.this, SharedAlbum.class);
//                                sharedElem2.putExtra("activity","album");
//                                sharedElem2.putExtra("shared", item.getAlbum().getAlbum());
//                                sharedElem2.putExtra("imageuri", String.valueOf(item.getAlbum().getAlbumarturi()));
//                                sharedElem2.putExtra("artistname", item.getAlbum().getAlbum());
//                                sharedElem2.putExtra("artistid", item.getAlbum().getAlbumID());
//                                //   sharedElem.putExtra("list",tempArrayList);
//                                ActivityOptionsCompat optionsCompat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this, imageView, item.getAlbum().getAlbum());
//                                startActivity(sharedElem2, optionsCompat2.toBundle());
//                                break;
//
//                        }
//                        updateui();
//                        updateProgressBar();
//                    }
//                }));


                //Album Activity Code


                break;
            case "search":

                AppBarLayout tabLayout = (AppBarLayout) findViewById(R.id.app_barforsearch);
                tabLayout.setVisibility(View.VISIBLE);
                Toolbar  toolbarsearch = (Toolbar) findViewById(R.id.toolbarsearch);
                setSupportActionBar(toolbarsearch);
                SearchContainer.setVisibility(View.VISIBLE);


                linearLayoutsearch = (NestedScrollView) findViewById(R.id.searchresultContainer);


                EditText editTextsearch = (EditText) findViewById(R.id.searchSongs);

                searchSongResult  = (RecyclerView) findViewById(R.id.searchSongsResult);
                searchArtist=(RecyclerView) findViewById(R.id.searchartistResult);
                searchAlbum = (RecyclerView) findViewById(R.id.searchAlbumResult);

                editTextsearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        performSearch(String.valueOf(s));

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                //Search Activity Code


                break;
            case "tags":
                songListArtist  = new ArrayList<>();
                songListArtist.clear();
               AppBarLayout appBarLayout2 = (AppBarLayout) findViewById(R.id.app_bar_tags);
                appBarLayout2.setVisibility(View.VISIBLE);
                toolbar = (Toolbar) findViewById(R.id.toolbar_tags);
                sharedImage_tags = (ImageView) findViewById(R.id.place_image_tags);


                Glide.with(this).load(Uri.parse(getIntent().getStringExtra("imageuri"))).into(sharedImage);
                if (getIntent().getStringExtra("shared").equals("recentlyadded")) {
                    artistNameSharedtags.setText("Recently Added");
                } else {
                    artistNameSharedtags.setText(getIntent().getStringExtra("shared").substring(0, 1).toUpperCase() + getIntent().getStringExtra("shared").substring(1));

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sharedImage_tags.setTransitionName(getIntent().getStringExtra("shared"));
                }

                Picasso.with(this)
                        .load(Uri.parse(getIntent().getStringExtra("imageuri")))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                                final float[] color = new float[3],
                                        transparent = new float[3];


                                final ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                                if (bitmap != null) {
                                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {


                                            if (palette.getDarkMutedSwatch() != null)
                                                backColor = String.format("#%06X", (0xFFFFFF & palette.getDarkMutedSwatch().getRgb()));
                                            if (palette.getMutedSwatch() != null)
                                                Log.e("Muted", String.format("#%06X", (0xFFFFFF & palette.getMutedSwatch().getRgb())));
                                            if (palette.getLightMutedSwatch() != null)
                                                //   defaultbackgroundforConInCC=  String.format("#%06X", (0xFFFFFF &palette.getLightMutedSwatch().getRgb()));
                                                if (palette.getVibrantSwatch() != null)
                                                    Log.e("VibrantMuted", String.format("#%06X", (0xFFFFFF & palette.getVibrantSwatch().getRgb())));
                                            if (palette.getDarkVibrantSwatch() != null)
                                                // textColor = String.format("#%06X", (0xFFFFFF &palette.getDarkVibrantSwatch().getRgb()));
                                                if (palette.getLightVibrantSwatch() != null)
                                                    textColor = String.format("#%06X", (0xFFFFFF & palette.getLightVibrantSwatch().getRgb()));

                                            Color.colorToHSV(Color.parseColor(backColor), color);

                                            Color.colorToHSV(Color.parseColor("#00FFFFFF"), transparent);

                                            imageBackgroundtags.setBackgroundColor(Color.parseColor(backColor));


                                            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{Color.HSVToColor(color), Color.argb(0, 0, 0, 0)});
                                            gradient.setShape(GradientDrawable.RECTANGLE);
                                            imageForegroundtags.setBackground(gradient);


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

                if (getIntent().getStringExtra("shared").equals("recentlyadded")) {
                    songListArtist = dbSync.getrecentlyadded();

                } else {

                    if (getIntent().getStringExtra("shared") != null) {

                        for (ArtistPojo artistPojo : artistInfo.getAllArtistbyTag(getIntent().getStringExtra("shared"))) {
                            if (dbSync.getArtistDetails(artistPojo.getName()) != null) {

                                for (Songs songs : dbSync.getArtistDetails(artistPojo.getName())) {
                                    songListArtist.add(songs);
                                }
                            }
                        }
                    }
                }

                Collections.sort(songListArtist, new Comparator<Songs>() {
                    public int compare(Songs a ,Songs b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });

                verticleListInShared.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


                verticleListInShared.setAdapter(new SongAdapter(getApplicationContext(),
                        songListArtist, new SongAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Songs item) {

                        // EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Son.indexOf(item)))));
                        musicService.setList(songListArtist);
                        musicService.setSong(songListArtist.indexOf(item));

                        // Log.e("debug : ", String.valueOf(songListArtist.indexOf(item)));
                        // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
                        //    Log.e("hello",String.valueOf(item.getID()));
                        musicService.playSong();
                        updateui();
                        updateProgressBar();
                        if (musicService.isPng() == false) {
                            playNowPlayingInShared.setImageDrawable(pauseShared);
                            playpauseInShared.setImageDrawable(pauseShared);

                        } else {
                            playNowPlayingInShared.setImageDrawable(playShared);
                            playpauseInShared.setImageDrawable(playShared);
                        }
                    }
                }));






                break;
        }














//        Picasso.with(SharedElementActivity.this).load(Uri.parse(getIntent().getStringExtra("imageuri")))
//
//                .into(sharedImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        supportStartPostponedEnterTransition();
//                        sharedImage.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError() {
//                        supportStartPostponedEnterTransition();
//                    }
//                });



        //  Glide.with(SharedElementActivity.this).load(Uri.parse(getIntent().getStringExtra("imageuri"))).into(sharedImage);


        sharedpanelinNowplaying = (SlidingUpPanelLayout) findViewById(R.id.slideupNowPlaying);
        sharedpanelinNowplaying.setScrollableView(nowPlayingListInShared);

        sharedpanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e("panel slide", String.valueOf(slideOffset));
                sharedview.setAlpha(1 - slideOffset);

                if (slideOffset > 0.99) {
                    sharedview.setVisibility(View.GONE);
                } else {
                    sharedview.setVisibility(View.VISIBLE);
                }
                //

                sharedpanel.setOverlayed(true);

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {

                    sharedpanel.setTouchEnabled(false);
                    sharedview.setVisibility(View.GONE);




                } else if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    sharedpanel.setTouchEnabled(true);
                    sharedview.setVisibility(View.VISIBLE);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedpanel.setNestedScrollingEnabled(true);
        }
        toolbarinslideupshared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedpanel.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED))
                    sharedpanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                else if (sharedpanel.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    sharedpanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });
        AlbumartinShared = (SimpleDraweeView) findViewById(R.id.albumArtInShared);
        sharedpanel.setScrollableView(sharedpanelinNowplaying);
        AlbumartinShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        sharedpanel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        // AlbumartinShared = (SimpleDraweeView) findViewById(R.id.albumArtInShared);
        albumArtNowPlayingInShared = (SimpleDraweeView) findViewById(R.id.albumArtNowPlaying);
        seekBarInShared = (SeekBar) findViewById(R.id.seekBar);

        sharedpanelinNowplaying.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {

                }
            }
        });
        sharedpanelinNowplaying.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        // first slide panel declaration , controls container






        //recycler views

        seekBarInShared.setOnSeekBarChangeListener(this);
        // if(serviceBound == true)


    }


    @OnClick(R.id.playPauseInShared)
    public void setPlaypauseInShared() {
        if (musicService.isPng()) {
            musicService.pausePlayer();
            playpauseInShared.setImageDrawable(playShared);
            playNowPlayingInShared.setImageDrawable(playShared);
            updateProgressBar();


        } else if (musicService.getSongTitle().equals("")) {

            musicService.setSong(0);
            musicService.playSong();
            updateui();
            updateProgressBar();

            playpauseInShared.setImageDrawable(pauseShared);
            playNowPlayingInShared.setImageDrawable(pauseShared);


        } else {
            musicService.go();
            playpauseInShared.setImageDrawable(pauseShared);
            playNowPlayingInShared.setImageDrawable(pauseShared);
        }

    }

    @OnClick(R.id.prevNowplaying)
    public void setPrevNowplayingInShared() {
        musicService.playPrev();
        updateui();
        updateProgressBar();
        if (musicService.isPng() == false) {
            playNowPlayingInShared.setImageDrawable(pauseShared);
            playpauseInShared.setImageDrawable(pauseShared);

        } else {
            playNowPlayingInShared.setImageDrawable(playShared);
            playpauseInShared.setImageDrawable(playShared);
        }

    }

    @OnClick(R.id.nextNowPlaying)
    public void setNextNowPlayingInShared() {
        musicService.playNext();
        updateui();
        updateProgressBar();
        updatelayoutcolor();
        if (musicService.isPng() == false) {
            playNowPlayingInShared.setImageDrawable(pauseShared);
            playpauseInShared.setImageDrawable(pauseShared);

        } else {
            playNowPlayingInShared.setImageDrawable(playShared);
            playpauseInShared.setImageDrawable(playShared);
        }
    }

    @OnClick(R.id.playNowPlaying)
    public void setPlayNowPlayingInShared() {
        if (musicService.isPng()) {
            musicService.pausePlayer();
            playpauseInShared.setImageDrawable(playShared);
            playNowPlayingInShared.setImageDrawable(playShared);

            updateProgressBar();


        } else if (musicService.getSongTitle().equals("")) {

            musicService.setSong(0);
            musicService.playSong();
            updateui();
            updateProgressBar();

            playpauseInShared.setImageDrawable(pauseShared);
            playNowPlayingInShared.setImageDrawable(pauseShared);


        } else {
            musicService.go();
            updateProgressBar();
            playpauseInShared.setImageDrawable(pauseShared);
            playNowPlayingInShared.setImageDrawable(pauseShared);
        }

    }


    @Override
    protected void onStart() {

        super.onStart();

        playIntent = new Intent(this, MusicService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);


    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicService = binder.getService();
            serviceBound = true;
            //pass list

            updateui();
            updateProgressBar();
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void updateui() {

        AlbumartinShared.setImageURI(musicService.getAlbumart());
        artistNameInShared.setText(musicService.getSongArtist());
        songNameInShared.setText(musicService.getSongTitle());
        moreByArtist.setText("More by " + musicService.getSongArtist());
        songTiitleNowPlayingInShared.setText(musicService.getSongTitle());
        artistNameNowPlayingInShared.setText(musicService.getSongArtist());
        albumArtNowPlayingInShared.setImageURI(musicService.getAlbumart());
        EventBus.getDefault().post(new UpdateUi(true));
        Log.e("tag", String.valueOf(musicService.isPng()));
        updatelayoutcolor();
        if (musicService.isPng() == true) {
            playNowPlayingInShared.setImageDrawable(pauseShared);
            playpauseInShared.setImageDrawable(pauseShared);

        } else {
            playNowPlayingInShared.setImageDrawable(playShared);
            playpauseInShared.setImageDrawable(playShared);
        }


    }

    private void updateProgressBar() {

        handler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (serviceBound == true) {
                if (musicService.isPng()) {
                    totalDuration = musicService.getDur();

                    currentDuration = musicService.getPosn();
                }

            }
            maxDurationInShared.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            currentDurationInShared.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            //  songProgress.setProgress(progress);
            seekBarInShared.setProgress(progress);
            materialProgressBarShared.setProgress(progress);

            if (progress > 99) {
                if (serviceBound == true) {
                    progress = 0;
                    musicService.playNext();
                    updateui();

                }

            }

            // Running this thread after 100 milliseconds
            handler.postDelayed(this, 100);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        serviceBound = true;
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(musicConnection);
        serviceBound = false;
    }




    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (sharedpanelinNowplaying != null &&
                (sharedpanelinNowplaying.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sharedpanelinNowplaying.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED))

        {
            sharedpanelinNowplaying.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (sharedpanel != null &&
                (sharedpanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sharedpanel.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            sharedpanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
          //  toolbar.setVisibility(View.GONE

            //;

        }
        //fab.setVisibility(View.GONE);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            if (serviceBound == true) musicService.seek((musicService.getDur() * i) / 100);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    void updatelayoutcolor(){


        if (serviceBound == true) {


            if(musicService.getNextlist()!=null && musicService.getNextlist().getSongs().size()>1){

                int index=0;

                for(Songs s:musicService.getNextlist().getSongs()){
                    index++;
                    if(musicService.getNextlist().getSong().getTitle().equals(s.getTitle())){

                        break;
                    }

                }
                final ArrayList<Songs> nextList = new ArrayList<>();

                while (index <musicService.getNextlist().getSongs().size()){

                    nextList.add(musicService.getNextlist().getSongs().get(index));
                    index ++;

                }

                nowPlayingListInShared.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                nowPlayingListInShared.setAdapter(new SongAdapter(getApplicationContext(), nextList, new SongAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Songs item) {
                        musicService.setList(nextList);
                        int songIndex = 0;

                        for (int index= 0 ; index < nextList.size();index++){

                            if(nextList.get(index).getTitle().equals(item.getTitle())){
                                songIndex = index;
                                break;
                            }



                        }
                        musicService.setSong(songIndex);
                        musicService.playSong();
                        updateui();

                        updateProgressBar();
                        //  EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Songlist.indexOf(item)))));
                        // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
                        //    Log.e("hello",String.valueOf(item.getID()));
//                musicSrv.playSong();
                    }
                }));


            }




            Bitmap bitmap = null;

            //getsingleartist
            String getArtist = "";
            String[] sa = musicService.getSongArtist().split(" and | & |/|feat |ft. | , | ,|feat. |, |\\(| -");
            int i = 0;
            for(String sa1 :sa){
                if(i == 0 ){

                    getArtist = sa1;}
                i =1;

            }
            if(!getArtist.equals("")){
                Log.e("artist fidel: " ,getArtist);
                ArtistPojo artistPojo = artistInfo.getArtistDetails(getArtist);

                if(artistPojo!=null){
                    moreArtistNowPlayingInShared.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                    moreArtistNowPlayingInShared.setAdapter(new SongHorizontalAdapter(getApplicationContext(), false, generaterecyclerlist(artistPojo.getName()), new SongHorizontalAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(CustomisedList item , ImageView imageView) {
                            //EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Songlist.indexOf(item)))));
                            // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
                            //  Log.e("hello",String.valueOf(item.getID()));
//                musicSrv.playSong();
//                musicSrv.setList(songs);
//                musicSrv.setSong(songs.indexOf(item));
//                musicSrv.playSong();
                            switch (item.getType()) {
                                case 0:
                                    Intent sharedElem = new Intent(SharedAlbum.this,SharedAlbum.class);
                                    sharedElem.putExtra("activity","artist");
                                    sharedElem.putExtra("shared",item.getArtist().getName());
                                    sharedElem.putExtra("imageuri",String.valueOf(item.getArtist().getImageuri()));
                                    sharedElem.putExtra("artistname",item.getArtist().getName());
                                    //   sharedElem.putExtra("list",tempArrayList);
                                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this,imageView,item.getArtist().getName());

                                    startActivity(sharedElem,optionsCompat.toBundle());

                                    break;
                                case 1:
                                    musicService.setList(demoList);
                                    int songIndex = 0;

                                    for (int index= 0 ; index < demoList.size();index++){

                                        if(demoList.get(index).getTitle().equals(item.getSong().getTitle())){
                                            songIndex = index;
                                            break;
                                        }



                                    }
                                    musicService.setSong(songIndex);
                                    musicService.playSong();
                                    break;
                                case 2:
                                    Intent sharedElem2 = new Intent(SharedAlbum.this, SharedAlbum.class);
                                    sharedElem2.putExtra("activity","album");
                                    sharedElem2.putExtra("shared", item.getAlbum().getAlbum());
                                    sharedElem2.putExtra("imageuri", String.valueOf(item.getAlbum().getAlbumarturi()));
                                    sharedElem2.putExtra("artistname", item.getAlbum().getAlbum());
                                    sharedElem2.putExtra("artistid", item.getAlbum().getAlbumID());
                                    //   sharedElem.putExtra("list",tempArrayList);
                                    ActivityOptionsCompat optionsCompat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this, imageView, item.getAlbum().getAlbum());
                                    startActivity(sharedElem2, optionsCompat2.toBundle());
                                    break;

                            }
                            updateui();
                            updateProgressBar();
                        }
                    }));

                    moreByArtist.setText("More like : "+artistPojo.getName());

                    Picasso.with(this).load(artistPojo.getImageuri()).into(artistImageInNowPlaying);
                    Picasso.with(this)
                            .load(artistPojo.getImageuri())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom source){



                                    final float[] from = new float[3],
                                            to =   new float[3];
                                    final   ValueAnimator anim = ValueAnimator.ofFloat(0, 1);



                                    if (bitmap != null) {
                                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Color.colorToHSV(Color.parseColor(defaultbackgroundforConInCC), from);   // from white



                                                if(palette.getDarkMutedSwatch()!= null)
                                                    defaultbackgroundforConInCC =    String.format("#%06X", (0xFFFFFF &palette.getDarkMutedSwatch().getRgb()));
                                                if(palette.getMutedSwatch()!= null)
                                                    Log.e("Muted",String.format("#%06X", (0xFFFFFF &palette.getMutedSwatch().getRgb())));
                                                if(palette.getLightMutedSwatch()!= null)
                                                    //   defaultbackgroundforConInCC=  String.format("#%06X", (0xFFFFFF &palette.getLightMutedSwatch().getRgb()));
                                                    if(palette.getVibrantSwatch()!= null)
                                                        Log.e("VibrantMuted",String.format("#%06X", (0xFFFFFF &palette.getVibrantSwatch().getRgb())));
                                                if(palette.getDarkVibrantSwatch()!= null)
                                                    //   defaultbackgroundforConInCC = String.format("#%06X", (0xFFFFFF &palette.getDarkVibrantSwatch().getRgb()));
                                                    if(palette.getLightVibrantSwatch()!= null)
                                                        Log.e("LightVibrant",String.format("#%06X", (0xFFFFFF &palette.getLightVibrantSwatch().getRgb())));


                                                Color.colorToHSV(Color.parseColor(defaultbackgroundforConInCC), to);     // to red

                                                // animate from 0 to 1
                                                anim.setDuration(300);                              // for 300 ms

                                                final float[] hsv  = new float[3];                  // transition color
                                                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                                                    @Override public void onAnimationUpdate(ValueAnimator animation) {
                                                        // Transition along each axis of HSV (hue, saturation, value)
                                                        hsv[0] = from[0] + (to[0] - from[0])*animation.getAnimatedFraction();
                                                        hsv[1] = from[1] + (to[1] - from[1])*animation.getAnimatedFraction();
                                                        hsv[2] = from[2] + (to[2] - from[2])*animation.getAnimatedFraction();
                                                        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]  {Color.HSVToColor(to), Color.HSVToColor(hsv)});
                                                        gradient.setShape(GradientDrawable.RECTANGLE);

                                                        GradientDrawable gradientforbottomviewgradient = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]  {Color.HSVToColor(to),Color.argb(0,0,0,0)});
                                                        gradientforbottomviewgradient.setShape(GradientDrawable.RECTANGLE);
                                                        sidepaletteforartistImage.setBackgroundColor(Color.HSVToColor(to));
                                                        nowPlayingListContainer.setBackgroundColor(Color.HSVToColor(to));
                                                        sharedview.setBackgroundColor(Color.HSVToColor(to));
                                                        toolbarinslideupshared.setBackgroundColor(Color.HSVToColor(to));

                                                        bottompaletteforartistImage.setBackground(gradientforbottomviewgradient);
                                                        Window window = SharedAlbum.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
                                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
                                                        window.setStatusBarColor(Color.HSVToColor(to));

                                                        gradient.setCornerRadius(10.f);

                                                    }
                                                });

                                                anim.start();







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


                }

            }



//            if(musicSrv.isPng()){
//
//                playPauseButton.setImageDrawable(play);
//                playNowPlaying.setImageDrawable(play);
//            }else{
//                playPauseButton.setImageDrawable(pause);
//                playNowPlaying.setImageDrawable(pause);
//            }
        }





    }
    @Subscribe
    public void onEvent(UpdateuiforsharedArtist event) {
        updateui();


        updateProgressBar();
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    ArrayList<CustomisedList> generaterecyclerlist(String artistName) {
        int numberofobjects = 0;

        ArrayList<CustomisedList> generatedList = new ArrayList<CustomisedList>();
        ArrayList<Songs> songListforalbums = new ArrayList<Songs>();

        for (Songs songs : dbSync.getArtistDetails(artistName)) {

            if (numberofobjects < 5)
                generatedList.add(new CustomisedList(1, null, songs, null, null));
            numberofobjects++;

        }

        numberofobjects = 0;
        songListforalbums = dbSync.getArtistDetails(artistName);
        Collections.sort(songListforalbums, new Comparator<Songs>() {
            public int compare(Songs a, Songs b) {
                return a.getAlbum().compareTo(b.getAlbum());

            }
        });

        String Albumname = "dgfsfgwrthwr";
//int i =0;
        //final ArrayList<Songs> tempArrayList = new ArrayList<Songs>();
        for (Songs s : songListforalbums) {

            // if(i ==0)Albumname = s.getAlbum();


            if (!Albumname.equals(s.getAlbum())) {
                // Log.e("Demo Test",String.valueOf(s.getAlbum().contains(Albumname)));

                if (!s.getAlbum().contains(Albumname)) {

                    if (numberofobjects < 5)
                        generatedList.add(new CustomisedList(2, null, null, s, null));
                    numberofobjects++;

                    Albumname = s.getAlbum();
                }


            }

        }


        if (artistInfo.getArtistDetails(artistName) != null && !artistName.equals("")) {
            generatedList.add(new CustomisedList(0, artistInfo.getArtistDetails(artistName), null, null, null));
        }


        return generatedList;
    }

    public class BackgroundProcessing extends AsyncTask<Boolean,Boolean,Boolean>{

        @Override
        protected Boolean doInBackground(Boolean... booleans) {





            return null;
        }
    }


    private void performSearch(String s) {
        if(!s.equals("")){

            if((generaterecyclerlistforsearch(s,0)!=null && generaterecyclerlistforsearch(s,0).size()>=1)
                    ||(generaterecyclerlistforsearch(s,1)!=null && generaterecyclerlistforsearch(s,1).size()>=1)
                    ||(generaterecyclerlistforsearch(s,2)!=null && generaterecyclerlistforsearch(s,2).size()>=1)) {
                searchError.setVisibility(View.GONE);

                linearLayoutsearch.setVisibility(View.VISIBLE);

                if((generaterecyclerlistforsearch(s,1) !=null &&generaterecyclerlistforsearch(s,1).size()>=1)){
                    songResult.setVisibility(View.VISIBLE);
                    searchSongResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    searchSongResult.setHasFixedSize(true);
                    searchSongResult.setAdapter(new SongHorizontalAdapter(this, true, generaterecyclerlistforsearch(s, 1), new SongHorizontalAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(CustomisedList item, ImageView imageView) {
                            if(musicService.getList()!=null) {


                                int songIndex = 0;

                                for (int index = 0; index < demoList.size(); index++) {

                                    if (musicService.getList().get(index).getTitle().equals(item.getSong().getTitle())) {
                                        songIndex = index;
                                        break;
                                    }


                                }
                                musicService.setSong(songIndex);
                                musicService.playSong();

                            }

                        }
                    }));}
                else {
                    songResult.setVisibility(View.GONE);
                }
                if((generaterecyclerlistforsearch(s,0) !=null &&generaterecyclerlistforsearch(s,0).size()>=1)){
                    ArtistResult.setVisibility(View.VISIBLE);
                    searchArtist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

                    searchArtist.setHasFixedSize(true);
                    searchArtist.setAdapter(new SongHorizontalAdapter(this, true, generaterecyclerlistforsearch(s, 0), new SongHorizontalAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(CustomisedList item, ImageView imageView) {
                            Intent sharedElem = new Intent(SharedAlbum.this,SharedAlbum.class);
                            sharedElem.putExtra("activity","artist");
                            sharedElem.putExtra("shared",item.getArtist().getName());
                            sharedElem.putExtra("imageuri",String.valueOf(item.getArtist().getImageuri()));
                            sharedElem.putExtra("artistname",item.getArtist().getName());
                            //   sharedElem.putExtra("list",tempArrayList);
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this,imageView,item.getArtist().getName());

                            startActivity(sharedElem,optionsCompat.toBundle());

                        }
                    }));}
                else {
                    ArtistResult.setVisibility(View.GONE);
                }
                if((generaterecyclerlistforsearch(s,0) !=null &&generaterecyclerlistforsearch(s,0).size()>=1)){
                    albumResult.setVisibility(View.VISIBLE);
                    searchAlbum.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

                    searchAlbum.setHasFixedSize(true);
                    searchAlbum.setAdapter(new SongHorizontalAdapter(this, true, generaterecyclerlistforsearch(s, 2), new SongHorizontalAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(CustomisedList item, ImageView imageView) {
                            Intent sharedElem2 = new Intent(SharedAlbum.this, SharedAlbum.class);
                            sharedElem2.putExtra("activity","album");
                            sharedElem2.putExtra("shared", item.getAlbum().getAlbum());
                            sharedElem2.putExtra("imageuri", String.valueOf(item.getAlbum().getAlbumarturi()));
                            sharedElem2.putExtra("artistname", item.getAlbum().getAlbum());
                            sharedElem2.putExtra("artistid", item.getAlbum().getAlbumID());
                            //   sharedElem.putExtra("list",tempArrayList);
                            ActivityOptionsCompat optionsCompat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(SharedAlbum.this, imageView, item.getAlbum().getAlbum());
                            startActivity(sharedElem2, optionsCompat2.toBundle());

                        }
                    }));}
                else {
                    albumResult.setVisibility(View.GONE);
                }
            }
            else {
                linearLayoutsearch.setVisibility(View.GONE);
                searchError.setVisibility(View.VISIBLE);
                errorText.setTextSize(16);

                errorText.setText("Unable to find "+s+" . \n Please provide more relevent input.");

            }

        }else {
            linearLayoutsearch.setVisibility(View.GONE);
            searchError.setVisibility(View.VISIBLE);

        }
    }

    ArrayList<CustomisedList> generaterecyclerlistforsearch(String search,int type) {
        int numberofobjects = 0;
        ArrayList<CustomisedList> generatedList = new ArrayList<CustomisedList>();
        ArrayList<Songs> songListforalbums = new ArrayList<Songs>();
        switch (type){
            case 0: //for artist

                generatedList.clear();
                numberofobjects = 0;


                if(artistInfo.getArtistbySearch(search)!=null) {

                    for (ArtistPojo artistPojo : artistInfo.getArtistbySearch(search)) {

                        if(artistPojo!=null && numberofobjects<10){


                            generatedList.add(new CustomisedList(0, artistPojo, null, null, null));
                            numberofobjects ++;


                        }


                    }
                }
                break;
            case 1: //forsongs
                generatedList.clear();

                for (Songs songs : dbSync.getSongListbySearch(search)) {



                    if (numberofobjects < 10)
                        generatedList.add(new CustomisedList(1, null, songs, null, null));
                    numberofobjects++;

                }
                break;
            case 2: //albums
                generatedList.clear();
                numberofobjects = 0;
                songListforalbums = dbSync.getSongListbySearch(search);
                Collections.sort(songListforalbums, new Comparator<Songs>() {
                    public int compare(Songs a, Songs b) {
                        return a.getAlbum().compareTo(b.getAlbum());

                    }
                });

                String Albumname = "dgfsfgwrthwr";
//int i =0;
                //final ArrayList<Songs> tempArrayList = new ArrayList<Songs>();
                for (Songs s : songListforalbums) {

                    // if(i ==0)Albumname = s.getAlbum();


                    if (!Albumname.equals(s.getAlbum())) {
                        // Log.e("Demo Test",String.valueOf(s.getAlbum().contains(Albumname)));

                        if (!s.getAlbum().contains(Albumname)) {

                            if (numberofobjects < 10)
                                generatedList.add(new CustomisedList(2, null, null, s, null));
                            numberofobjects++;

                            Albumname = s.getAlbum();
                        }


                    }

                }

                break;
        }










        return generatedList;
    }





}
