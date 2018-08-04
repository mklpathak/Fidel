package com.fidel.dhun;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fidel.dhun.adapter.AlbumRecyclerview;
import com.fidel.dhun.adapter.ArtistRecyclerView;
import com.fidel.dhun.adapter.SongAdapter;
import com.fidel.dhun.adapter.SongHorizontalAdapter;
import com.fidel.dhun.adapter.TagRecyclerView;
import com.fidel.dhun.data.Api;
import com.fidel.dhun.data.ApiUtils;
import com.fidel.dhun.data.ArtistInfo;
import com.fidel.dhun.data.ArtistPojo;
import com.fidel.dhun.data.CustomisedList;
import com.fidel.dhun.data.DatabaseSync;
import com.fidel.dhun.data.GetMusicId;
import com.fidel.dhun.data.PopularAlbum;
import com.fidel.dhun.data.PopularArtist;
import com.fidel.dhun.data.Songs;
import com.fidel.dhun.data.Tag.Top;
import com.fidel.dhun.data.Tagforrecylerview;
import com.fidel.dhun.data.api.Example;
import com.fidel.dhun.data.updateui.UpdateNextSongList;
import com.fidel.dhun.data.updateui.UpdateUi;
import com.fidel.dhun.fragments.Albums;
import com.fidel.dhun.fragments.AllMusic;
import com.fidel.dhun.fragments.Artist;
import com.fidel.dhun.fragments.PlayList;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    Handler handler = new Handler();
    Utility utils = new Utility();
    Api artistApi;


    Toolbar toolbarInNowPlying;


    Toolbar toolbar;

    DatabaseSync dbsync;
    ArtistInfo artistSync;
    long currentDuration = 0;
    long totalDuration = 0;
    String TAG = "Debug";

    private int countArtist;
    private SlidingUpPanelLayout mLayout;
    private SlidingUpPanelLayout nowPlayingListLayout;
    private boolean paused = false, playbackPaused = false;
    private Intent playIntent;

    private boolean serviceBound = false;
    private MusicService musicSrv;


    private ArrayList<Songs> songs;

    String defaultbackgroundforConInCC ;
    //fornavbar

    ArrayList<Tagforrecylerview> taglist;
    ArrayList<PopularArtist>  getArtistList;
    ArrayList<String>  demoArtistList1;


    @BindView(R.id.recyclerback)
    ConstraintLayout nowPlayingListContainer ;
    RecyclerView nowPlayingList;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.moreArtistNowPlaying)
    RecyclerView my_recycler_view;

    @BindView(R.id.fragmentsContainer)
    ViewPager fragmentsContainer;

    @BindView(R.id.dragView)
    LinearLayout dragView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.songName)
    TextView songNameInController;
    @BindView(R.id.artistName)
    TextView artistNameInController;
    @BindView(R.id.albumArt)
    SimpleDraweeView albumArtInController;
    @BindView(R.id.playPause)
    ImageView playPauseButton;
    @BindView(R.id.songProgress)
    ProgressBar songProgress;
    @BindDrawable(R.drawable.play)
    Drawable play;
    @BindDrawable(R.drawable.pause)
    Drawable pause;
    @BindView(R.id.songTiitleNowPlaying)
    TextView songTitleNowPlaying;
    @BindView(R.id.artistNameNowPlaying)
    TextView artistNameNowPlaying;
    @BindView(R.id.albumArtNowPlaying)
    SimpleDraweeView albumArtNowPlaying;
    @BindView(R.id.prevNowplaying)
    ImageView prevNowPlaying;
    @BindView(R.id.playNowPlaying)
    ImageView playNowPlaying;
    @BindView(R.id.nextNowPlaying)
    ImageView nextNowPlaying;
    @BindView(R.id.currentDuration)
    TextView currentDurationinNowPlaying;
    @BindView(R.id.maxDuration)
    TextView maxDurationinNowPlaying;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.artistImageinNowPlaying)
    ImageView artistImageInNowPlaying;
    @BindView(R.id.sidepaletteforartistImage)
    View sidepaletteforartistImage;
    @BindView(R.id.bottompaletteforartistImage)
    View bottompaletteforartistImage;
    @BindView(R.id.morebyartist)
    TextView moreByArtist; //Bindviews except navbar elements
    //fornavbar
    @BindView(R.id.recentlyaddedcard)
    CardView RecentlyAddedCard;
    @BindView(R.id.toptagscard)
    CardView TopTagsCard;
    @BindView(R.id.topartistcard)
    CardView TopArtistCard;
    @BindView(R.id.topalbumcard)
    CardView TopAlbumCard;
    @BindView(R.id.recentlyaddedcardImage)
    ImageView RecentlyAddedCardImage;
    @BindView(R.id.toptagscardimage)
    ImageView TopTagsCardImage;
    @BindView(R.id.topartistcardimage)
    ImageView TopArtistCardImage;
    @BindView(R.id.topalbumcardimage)
    ImageView  TopAlbumCardImage;
    @BindView(R.id.recnetlyaddedcardforground)
    View RecentlyAddedCardImageForground;
    @BindView(R.id.toptagscardimageforground)
    View TopTagsCardImageForground;
    @BindView(R.id.topartistcardforground)
    View TopArtistCardforground;
    @BindView(R.id.topalbumcardimageforground)
    View TopAlbumCardForground;
    BackgroundProcess backgroundProcess;

    ImageView artistImageinNavBar;

    @BindView(R.id.songTitleinnavbar)
    TextView songTitleInNavbar;
    @BindView(R.id.artisnamenavbar)
    TextView artistNameinNavBar;
    @BindView(R.id.nowplayingcontrols)
    ConstraintLayout nowPlayingControls;
    @BindView(R.id.moreArtistContainer)
    ConstraintLayout moreArtistContainer;

    SimpleDraweeView albumartinnavbar;

    @BindView(R.id.navbarrecycler)
    RecyclerView navbarrecycler;
    TextView toolbarTitle;



    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (serviceBound) {
                if (musicSrv.isPng()) {
                    totalDuration = musicSrv.getDur();
                    currentDuration = musicSrv.getPosn();
                }
            }
            maxDurationinNowPlaying.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            currentDurationinNowPlaying.setText("" + utils.milliSecondsToTimer(currentDuration));
            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgress.setProgress(progress);
            seekBar.setProgress(progress);
            if (progress > 99) {
                if (serviceBound) {
                    musicSrv.playNext();
                    updateUi();
                }

            }
            // Running this thread after 100 milliseconds
            handler.postDelayed(this, 100);
        }
    }; //thread
    //fetch songs
    //mainactivity navigation and toolbar method
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songs);
            // musicSrv.setArtistlist(artistList);
            updateUi();
            updateProgressBar();
            // serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //mainactivity start code
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitle("");
        toolbarInNowPlying = (Toolbar) findViewById(R.id.toolbarInNowPlaying);
        toolbarInNowPlying.setMinimumHeight(400);
        nowPlayingListLayout = (SlidingUpPanelLayout) findViewById(R.id.slideupNowPlaying);
        nowPlayingList = (RecyclerView) findViewById(R.id.nowPlayingList);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("FIDEL.");


        artistImageinNavBar = (ImageView) findViewById(R.id.artistImageInnavbar);
        albumartinnavbar =(SimpleDraweeView) findViewById(R.id.albumArtinnavbar);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/F.otf");
        toolbarTitle.setTypeface(custom_font);



        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        dbsync = new DatabaseSync(this);
        artistSync = new ArtistInfo(this);

        backgroundProcess=new BackgroundProcess();
        backgroundProcess.execute();
        fragmentsContainer.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        demoArtistList1 = new ArrayList<String>();
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                0.0f
        );
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                15.0f
        );
        moreArtistContainer.setLayoutParams(param);
        moreArtistContainer.setVisibility(View.GONE);
        nowPlayingControls.setLayoutParams(param2);



        getArtistList = new ArrayList<PopularArtist>();
        taglist = new ArrayList<Tagforrecylerview>();
        songs = new ArrayList<Songs>();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(fragmentsContainer);

        Updatesongdatabase updatesongdatabase = new Updatesongdatabase();
        updatesongdatabase.execute();

        artistApi = ApiUtils.getArtist();
        //databasesync.execute();


        if(dbsync.getAllContacts()!=null && dbsync.getAllContacts().size()>=1) {
            songs = dbsync.getAllContacts();
            Collections.sort(songs, new Comparator<Songs>() {
                public int compare(Songs a, Songs b) {
                    return a.getArtist().compareTo(b.getArtist());
                }
            });
        }
        try {
            if (dbsync.getrecentlyadded() != null && getToptags() != null && artistSync.getAllArtist() != null) {
                if(dbsync.getrecentlyadded().size()>=1 && getToptags().size()>=1 &&artistSync.getAllArtist().size()>=1){
                Glide.with(getApplicationContext()).load(dbsync.getrecentlyadded().get(0).getAlbumarturi()).crossFade().centerCrop().into(RecentlyAddedCardImage);
                Glide.with(getApplicationContext()).load(artistSync.getAllArtistbyTag(getToptags().get(0)).get(0).getImageuri()).crossFade().into(TopTagsCardImage);
                Glide.with(getApplicationContext()).load(getTopartist().get(0).getImageuri()).crossFade().into(TopArtistCardImage);
                Glide.with(getApplicationContext()).load(getTopAlbums().get(0).getAlbumarturi()).crossFade().into(TopAlbumCardImage);}
            }

        }
        catch (NullPointerException e){

        }




        //Bundle songsBundle = new Bundle();
        //songsBundle.putSerializable("songList",(Serializable) songs);
        // frag.setArguments(songsBundle);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //end_start_code
        my_recycler_view.setHasFixedSize(true);
        //sliding drawer in main activity
        //main sliding drawer in main activity
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        toolbarInNowPlying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED))
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                else if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });
        mLayout.setScrollableView(my_recycler_view);



        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if(slideOffset > 0.3){
                    toolbarInNowPlying.setVisibility(View.VISIBLE);
                }
                //  Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                albumArtInController.setAlpha(1 - slideOffset);
                songNameInController.setAlpha(1 - slideOffset);
                artistNameInController.setAlpha(1-slideOffset);
                playPauseButton.setAlpha(1-slideOffset);
                songProgress.setAlpha(1-slideOffset);
                toolbarInNowPlying.setAlpha(slideOffset);
                Log.e("SlideOffset",String.valueOf(slideOffset));
                if (slideOffset > 0.99) {
                    albumArtInController.setVisibility(View.GONE);
                    songNameInController.setVisibility(View.GONE);
                    artistNameInController.setVisibility(View.GONE);
                    playPauseButton.setVisibility(View.GONE);
                    songProgress.setVisibility(View.GONE);
                } else {
                    albumArtInController.setVisibility(View.VISIBLE);
                    songNameInController.setVisibility(View.VISIBLE);
                    artistNameInController.setVisibility(View.VISIBLE);
                    playPauseButton.setVisibility(View.VISIBLE);
                    songProgress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    albumArtInController.setVisibility(View.GONE);
                    songNameInController.setVisibility(View.GONE);
                    artistNameInController.setVisibility(View.GONE);
                    playPauseButton.setVisibility(View.GONE);
                    songProgress.setVisibility(View.GONE);
                    toolbarInNowPlying.setVisibility(View.VISIBLE);
                    toolbarInNowPlying.setAlpha(1);

                    mLayout.setTouchEnabled(false);

                } else if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    mLayout.setTouchEnabled(true);
                    toolbarInNowPlying.setVisibility(View.GONE);

                    albumArtInController.setVisibility(View.VISIBLE);
                    songNameInController.setVisibility(View.VISIBLE);
                    artistNameInController.setVisibility(View.VISIBLE);
                    playPauseButton.setVisibility(View.VISIBLE);
                    songProgress.setVisibility(View.VISIBLE);
                }
            }
        });
        //Sliding drawer in now playing

        nowPlayingListLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel1, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel1, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {


                    mLayout.setTouchEnabled(false);
                } else if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    mLayout.setTouchEnabled(true);
                }
            }
        });




        mLayout.setScrollableView(nowPlayingList);


        seekBar.setOnSeekBarChangeListener(this);
        songTitleNowPlaying.setSelected(true);





    }

    //navbarelements
    @OnClick(R.id.recentlyaddedcard)
    public void setRecentlyAddedCard(){

        navbarrecycler.setLayoutManager(new LinearLayoutManager(this));
        navbarrecycler.setAdapter(new SongAdapter(getApplicationContext(), dbsync.getrecentlyadded(), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Songs item) {
                musicSrv.setList(dbsync.getrecentlyadded());
                int songIndex = 0;

                for (int index = 0; index < dbsync.getrecentlyadded().size(); index++) {

                    if (dbsync.getrecentlyadded().get(index).getTitle().equals(item.getTitle())) {
                        songIndex = index;
                        break;
                    }


                }

                musicSrv.setSong(songIndex);
                musicSrv.playSong();
                updateUi();

                updateProgressBar();
                //  EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Songlist.indexOf(item)))));
                // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
                //    Log.e("hello",String.valueOf(item.getID()));
//                musicSrv.playSong();
            }
        }));

    }
    @OnClick(R.id.toptagscard)
    public void setTopTagsCard(){
        ArrayList<String> demolisttags;
        ArrayList<String> demolisttags2 = new ArrayList<>();
        taglist = new ArrayList<>();
        if(artistSync.getallTags()!=null && artistSync.getallTags().size()>=1){
            taglist =artistSync.getallTags();
        }
        else {
            demolisttags = getToptags();
            Collections.sort(demolisttags, new Comparator<String>() {
                public int compare(String a, String b) {
                    return a.compareTo(b);
                }
            });
            String prevTag = "johfhoehriferiopij";
            String currentArtistName = "";
            int tagCount = 0;
            for (String s : demolisttags) {
                if (!s.contains(prevTag)) {

                    prevTag = s;
                    demolisttags2.add(s);

                }
            }

            for (String s : demolisttags2) {
                if (!s.contains("'")) {
                    for (ArtistPojo artistPojo : artistSync.getAllArtistbyTag(s)) {
                        if (dbsync.getArtistDetails(artistPojo.getName()) != null) {
                            if (tagCount < dbsync.getArtistDetails(artistPojo.getName()).size()) {
                                tagCount = dbsync.getArtistDetails(artistPojo.getName()).size();

                                currentArtistName = artistPojo.getName();


                            }
                        }
                    }

                    taglist.add(new Tagforrecylerview(artistSync.getAllArtistbyTag(s).size()
                            , s, artistSync.getArtistDetails(currentArtistName).getImageuri()));
                    artistSync.addTags(new Tagforrecylerview(artistSync.getAllArtistbyTag(s).size()
                            , s, artistSync.getArtistDetails(currentArtistName).getImageuri()));
                    currentArtistName = "";
                    tagCount = 0;
                }
            }

        Collections.sort(taglist, new Comparator<Tagforrecylerview>() {
            public int compare(Tagforrecylerview a, Tagforrecylerview b) {
                return a.getCount().compareTo(b.getCount());
            }
        });
        Collections.reverse(taglist);
        }

        GridLayoutManager lLayout = new GridLayoutManager(this, 2);
        navbarrecycler.setLayoutManager(lLayout);
        navbarrecycler.setHasFixedSize(true);

        navbarrecycler.setAdapter(new TagRecyclerView(this, taglist, 1, new TagRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(Tagforrecylerview item, ImageView imageView) {

                Intent recentlyadded = new Intent(MainActivity.this, SharedAlbum.class);
                recentlyadded.putExtra("activity","tags");

                recentlyadded.putExtra("shared", item.getName());
                recentlyadded.putExtra("imageuri", String.valueOf(item.getUrl()));
                //   sharedElem.putExtra("list",tempArrayList);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageView, item.getName());
                startActivity(recentlyadded, optionsCompat.toBundle());



            }
        }));





    }
    @OnClick(R.id.topartistcard)
    public void setTopArtistCard(){

        GridLayoutManager  lLayout2 = new GridLayoutManager(this, 2);
        navbarrecycler.setHasFixedSize(true);
        navbarrecycler.setLayoutManager(lLayout2);
        navbarrecycler.setAdapter(new ArtistRecyclerView(MainActivity.this,getTopartist(),0, new ArtistRecyclerView.OnItemClickListener() {
            @Override public void onItemClick(ArtistPojo item, ImageView imageView) {


                //EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(artistList.indexOf(item)))));
                Intent sharedElem = new Intent(MainActivity.this,SharedAlbum.class);
                sharedElem.putExtra("activity","artist");
                sharedElem.putExtra("shared",item.getName());
                sharedElem.putExtra("imageuri",String.valueOf(item.getImageuri()));
                sharedElem.putExtra("artistname",item.getName());
                //   sharedElem.putExtra("list",tempArrayList);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView,item.getName());
                startActivity(sharedElem,optionsCompat.toBundle());
                // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));

//                musicSrv.playSong();
            }
        }));






    }
    @OnClick(R.id.topalbumcard)
    public void setTopAlbumCard(){


        GridLayoutManager  lLayout3 = new GridLayoutManager(this, 2);
        navbarrecycler.setHasFixedSize(true);
        navbarrecycler.setLayoutManager(lLayout3);
        navbarrecycler.setAdapter(new AlbumRecyclerview(MainActivity.this, getTopAlbums(), 1, new AlbumRecyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(Songs item, ImageView imageView) {


                // EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(artistList.indexOf(item)))


                Intent sharedElem = new Intent(MainActivity.this, SharedAlbum.class);
                sharedElem.putExtra("activity","album");
                sharedElem.putExtra("shared", item.getArtistid());
                sharedElem.putExtra("imageuri", String.valueOf(item.getAlbumarturi()));
                sharedElem.putExtra("artistname", item.getAlbum());
                sharedElem.putExtra("artistid", item.getAlbumID());
                //   sharedElem.putExtra("list",tempArrayList);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageView, item.getArtistid());
                startActivity(sharedElem, optionsCompat.toBundle());
                // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));

                // musicSrv.playSong();
            }
        }));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //main activity on Click Implimentation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                //shuffle
                if(musicSrv!=null){
                musicSrv.setShuffle();
                if(musicSrv.getSuffleStatus())

                    item.setIcon(R.drawable.suffle);
                }else
                    item.setIcon(R.drawable.suffle);
                Log.e("getSuffle Stauts",String.valueOf(musicSrv.getSuffleStatus()));
                break;
            case R.id.action_search:
                Intent i= new Intent(this,SharedAlbum.class);
                i.putExtra("activity","search");

                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.playPause)
    public void playPause() {
        if (musicSrv.isPng()) {
            musicSrv.pausePlayer();
            updateUi();

            playPauseButton.setImageDrawable(play);
            playNowPlaying.setImageDrawable(play);
            //updateUi();
            updateProgressBar();
        } else if (musicSrv.getSongTitle().equals("")) {
            musicSrv.setSong(0);
            musicSrv.playSong();

            updateUi();
            //updateUi();
//            songNameInController.setText(musicSrv.getSongTitle());
//            artistNameInController.setText(musicSrv.getSongArtist());
            playPauseButton.setImageDrawable(pause);
            playNowPlaying.setImageDrawable(pause);
            updateProgressBar();
        } else {
            musicSrv.go();
            updateUi();
            playPauseButton.setImageDrawable(pause);
            playNowPlaying.setImageDrawable(pause);
        }
    }

    @OnClick(R.id.prevNowplaying)
    public void PrevNowPlaying() {
        musicSrv.playPrev();

        updateUi();
    }

    @OnClick(R.id.nextNowPlaying)
    public void NextNowPlaying() {

        musicSrv.playNext();
        updateUi();
        updatelayoutcolor();
    }

    @OnClick(R.id.playNowPlaying)
    public void PlaytNowPlaying() {
        if (musicSrv.isPng()) {
            musicSrv.pausePlayer();
            playPauseButton.setImageDrawable(play);
            playNowPlaying.setImageDrawable(play);
            updateUi();
        } else if (musicSrv.getSongTitle().equals("")) {
            musicSrv.setSong(0);
            musicSrv.playSong();

//            songNameInController.setText(musicSrv.getSongTitle());
//            artistNameInController.setText(musicSrv.getSongArtist());
            playPauseButton.setImageDrawable(pause);
            playNowPlaying.setImageDrawable(pause);
            updateProgressBar();
            updateUi();
        } else {

            musicSrv.go();
            updateUi();
            playPauseButton.setImageDrawable(pause);
            playNowPlaying.setImageDrawable(pause);
        }
        //updateUi();
    }

    public void updateUi()
    {
        if (serviceBound == true && musicSrv!=null) {
            songNameInController.setText(musicSrv.getSongTitle());
            artistNameInController.setText(musicSrv.getSongArtist());
            albumArtInController.setImageURI(musicSrv.getAlbumart());
            songTitleNowPlaying.setText(musicSrv.getSongTitle());
            artistNameNowPlaying.setText(musicSrv.getSongArtist());
            albumArtNowPlaying.setImageURI(musicSrv.getAlbumart());
            albumartinnavbar.setImageURI(musicSrv.getAlbumart());
            artistNameinNavBar.setText(musicSrv.getSongArtist());
            songTitleInNavbar.setText(musicSrv.getSongTitle());
            if (musicSrv.isPng() == true) {
                playNowPlaying.setImageDrawable(pause);
                playPauseButton.setImageDrawable(pause);

            } else {
                playNowPlaying.setImageDrawable(play);
                playPauseButton.setImageDrawable(play);
            }
            updatelayoutcolor();
            //databasesync.execute();


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
    public void onEvent(GetMusicId event) {
        if (serviceBound == true) {
            songProgress.setProgress(0);
            songProgress.setMax(100);

            //songid = event.getSongid();
            updateProgressBar();
            //musicSrv.setSong(songid);
            musicSrv.setList(event.getList());
            musicSrv.setSong(event.getSongid());

            musicSrv.playSong();
            if (playbackPaused) {
                playbackPaused = false;
            }
            updateUi();
        }
    }


    @Subscribe
    public void onEvent(UpdateUi event) {
        updateUi();


        updateProgressBar();
    }

    @Subscribe
    public void onEvent(UpdateNextSongList event){


    }

    private void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            serviceBound = true;
        }
    }

    //activitymethods start
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(musicConnection);

        //serviceBound = false;
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            paused = false;
        }
        serviceBound = true;
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        updateUi();
    }



    @Override
    protected void onStop() {
        super.onStop();

        //unbindService(musicConnection);
    }

    //activitymethod end
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (nowPlayingListLayout != null &&
                 (nowPlayingListLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || nowPlayingListLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
             nowPlayingListLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
         }
        else if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else {
             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
             alertDialogBuilder.setTitle("Exit Application?");
             alertDialogBuilder
                     .setMessage("Click yes to exit!")
                     .setCancelable(false)
                     .setPositiveButton("Yes",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     moveTaskToBack(true);
                                     android.os.Process.killProcess(android.os.Process.myPid());
                                     System.exit(1);
                                 }
                             })

                     .setNegativeButton("No", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {

                             dialog.cancel();
                         }
                     });

             AlertDialog alertDialog = alertDialogBuilder.create();
             alertDialog.show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            if (serviceBound == true) musicSrv.seek((musicSrv.getDur() * i) / 100);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */


    private void loadArtist( String artistName) {

        artistApi.getArtist(artistName).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, final Response<Example> response) {
//                Log.e("Artist name Mukul","pathak " + response.body().getArtist().getImage().get(3).getText());
                if (response.body().getArtist() != null) {
                    if (!response.body().getArtist().getImage().get(3).getText().equals("")) {
                        artistSync.addArtist(response.body());

                        countArtist++;
                        Log.e("actual artist",String.valueOf(countArtist));


                    }
                    else {

                        countArtist++;
                        Log.e("actual artist",String.valueOf(countArtist));
                    }
                }
                else{
                    countArtist++;
                    Log.e("actual artist",String.valueOf(countArtist));

                }


            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                countArtist++;
                Log.e("actual artist",String.valueOf(countArtist));

            }
        });
        Log.e("actual artist",String.valueOf(countArtist));
    }
    ArrayList<String> getToptags(){
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> demotags = new ArrayList<String>();

        if(artistSync.getAllArtist()!=null){

            for(ArtistPojo s:artistSync.getAllArtist()){

                if(s!=null){

                    if(!s.getTags().equals("")){

                        String[] tag = s.getTags().split(",");

                        for (String t:tag){

                            tags.add(t.toLowerCase());
                        }

                    }

                }


            }}

        Collections.sort(tags, new Comparator<String>() {
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });
        String prevTag = "johfhoehriferiopij";
        String currentArtistName= "";
        int tagCount = 0;
        for (String s:tags){
            if(!s.contains(prevTag)){

                prevTag=s;
                demotags.add(s);

            }
        }



        return demotags;
    }


    private void loadTags(final String Artistname) {
        artistApi.getTags(Artistname).enqueue(new Callback<Top>() {
            @Override
            public void onResponse(Call<Top> call, final Response<Top> response) {
//                Log.e("Artist name Mukul","pathak " + response.body().getArtist().getImage().get(3).getText());
                //artistSync.addArtist(response.body().getToptags(),artisteExampleobject);
                if (response.body().getToptags() != null) {
                    artistSync.updateTags(Artistname, response.body());


                }
            }

            @Override
            public void onFailure(Call<Top> call, Throwable t) {

            }
        });
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment frag = null;

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    frag = AllMusic.newInstance();
                    // frag.setArguments(bundle);
                    return frag;
                case 1:
                    frag = Artist.newInstance();
                    // frag.setArguments(bundle);
                    return frag;
                case 2:
                    //    return "SECTION 2";
                    return Albums.newInstance();
                case 3:
                    //    return "SECTION 2";
                    return PlayList.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All Music";
                case 1:
                    return "Artists";
                case 2:
                    return "Albums";
                case 3:
                    return "Playlist";
            }
            return null;
        }
    }

    void updatelayoutcolor() {




        if (serviceBound == true) {

            if(musicSrv.getNextlist()!=null && musicSrv.getNextlist().getSongs().size()>1){

                int index=0;

                for(Songs s:musicSrv.getNextlist().getSongs()){
                    index++;
                    if(musicSrv.getNextlist().getSong().getTitle().equals(s.getTitle())){

                        break;
                    }

                }
                final ArrayList<Songs> nextList = new ArrayList<>();

                while (index <musicSrv.getNextlist().getSongs().size()){

                    nextList.add(musicSrv.getNextlist().getSongs().get(index));
                    index ++;

                }


                Log.e("LRetu for Main Activity",musicSrv.getNextlist().getSong().getTitle()+" "+musicSrv.getNextlist().getSongs().get(1).getTitle());
                nowPlayingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                nowPlayingList.setAdapter(new SongAdapter(getApplicationContext(), nextList, new SongAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Songs item) {
                        musicSrv.setList(nextList);
                        int songIndex = 0;

                        for (int index= 0 ; index < nextList.size();index++){

                            if(nextList.get(index).getTitle().equals(item.getTitle())){
                                songIndex = index;
                                break;
                            }



                        }
                        musicSrv.setSong(songIndex);
                        musicSrv.playSong();
                        updateUi();

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
            String[] sa = musicSrv.getSongArtist().split(" and | & |/|feat |ft. | , | ,|feat. |, |\\(| -");
            int i = 0;
            for (String sa1 : sa) {
                if (i == 0) {

                    getArtist = sa1;
                }
                i = 1;

            }
            if (!getArtist.equals("")) {

                final ArtistPojo artistPojo = artistSync.getArtistDetails(getArtist);


                if (artistPojo != null) {

                    if (generaterecyclerlist(artistPojo.getName()).size()>=3){
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0,
                                7.0f
                        );
                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0,
                                8.0f
                        );
                        moreArtistContainer.setLayoutParams(param);
                        moreArtistContainer.setVisibility(View.VISIBLE);
                        nowPlayingControls.setLayoutParams(param2);
                        Glide.with(this).load(artistPojo.getImageuri()).asBitmap().centerCrop().into(new BitmapImageViewTarget(artistImageinNavBar) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                artistImageinNavBar.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        my_recycler_view.setAdapter(new SongHorizontalAdapter(getApplicationContext(), false, generaterecyclerlist(artistPojo.getName()), new SongHorizontalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(CustomisedList item,ImageView imageView) {
                                //EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Songlist.indexOf(item)))));
                                // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
                                //  Log.e("hello",String.valueOf(item.getID()));
//                musicSrv.playSong();
//                musicSrv.setList(songs);
//                musicSrv.setSong(songs.indexOf(item));
//                musicSrv.playSong();
                                switch (item.getType()) {
                                    case 0:
                                        Intent sharedElem = new Intent(MainActivity.this,SharedAlbum.class);
                                        sharedElem.putExtra("activity","artist");

                                        sharedElem.putExtra("shared",item.getArtist().getName());
                                        sharedElem.putExtra("imageuri",String.valueOf(item.getArtist().getImageuri()));
                                        sharedElem.putExtra("artistname",item.getArtist().getName());
                                        //   sharedElem.putExtra("list",tempArrayList);
                                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView,item.getArtist().getName());

                                        startActivity(sharedElem,optionsCompat.toBundle());

                                        break;
                                    case 1:
                                        musicSrv.setList(dbsync.getArtistDetails(artistPojo.getName()));
                                        int songIndex = 0;

                                        for (int index= 0 ; index < dbsync.getArtistDetails(artistPojo.getName()).size();index++){

                                            if(dbsync.getArtistDetails(artistPojo.getName()).get(index).getTitle().equals(item.getSong().getTitle())){
                                                songIndex = index;
                                                break;
                                            }



                                        }
                                        musicSrv.setSong(songIndex);
                                        musicSrv.playSong();
                                        break;
                                    case 2:
                                        Intent sharedElem2 = new Intent(MainActivity.this, SharedAlbum.class);
                                        sharedElem2.putExtra("activity","albums");

                                        sharedElem2.putExtra("shared", item.getAlbum().getAlbum());
                                        sharedElem2.putExtra("imageuri", String.valueOf(item.getAlbum().getAlbumarturi()));
                                        sharedElem2.putExtra("artistname", item.getAlbum().getAlbum());
                                        sharedElem2.putExtra("artistid", item.getAlbum().getAlbumID());
                                        //   sharedElem.putExtra("list",tempArrayList);
                                        ActivityOptionsCompat optionsCompat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageView, item.getAlbum().getAlbum());
                                        startActivity(sharedElem2, optionsCompat2.toBundle());

                                        break;

                                }
                                updateUi();
                                updateProgressBar();
                            }
                        }));}
                        else {


                    }

                    moreByArtist.setText("More like : " + artistPojo.getName());

                    Picasso.with(this).load(artistPojo.getImageuri()).into(artistImageInNowPlaying);
                    Picasso.with(this)
                            .load(artistPojo.getImageuri())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom source) {


                                    final float[] from = new float[3],
                                            to = new float[3];
                                    final ValueAnimator anim = ValueAnimator.ofFloat(0, 1);


                                    if (bitmap != null) {
                                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Color.colorToHSV(Color.parseColor("#000000"), from);   // from white


                                                if (palette.getDarkMutedSwatch() != null)
                                                    defaultbackgroundforConInCC = String.format("#%06X", (0xFFFFFF & palette.getDarkMutedSwatch().getRgb()));
                                                if (palette.getMutedSwatch() != null)
                                                 //   Log.e("Muted", String.format("#%06X", (0xFFFFFF & palette.getMutedSwatch().getRgb())));
                                                if (palette.getLightMutedSwatch() != null)
                                                    //   defaultbackgroundforConInCC=  String.format("#%06X", (0xFFFFFF &palette.getLightMutedSwatch().getRgb()));
                                                    if (palette.getVibrantSwatch() != null)
                                                    //    Log.e("VibrantMuted", String.format("#%06X", (0xFFFFFF & palette.getVibrantSwatch().getRgb())));
                                                if (palette.getDarkVibrantSwatch() != null)
                                                    //   defaultbackgroundforConInCC = String.format("#%06X", (0xFFFFFF &palette.getDarkVibrantSwatch().getRgb()));
                                                    if (palette.getLightVibrantSwatch() != null)
                                                      //  Log.e("LightVibrant", String.format("#%06X", (0xFFFFFF & palette.getLightVibrantSwatch().getRgb())));


                                                        if(!defaultbackgroundforConInCC.equals("")){

                                                Color.colorToHSV(Color.parseColor(defaultbackgroundforConInCC), to);     // to red

                                                // animate from 0 to 1
                                                anim.setDuration(300);                              // for 300 ms

                                                final float[] hsv = new float[3];                  // transition color
                                                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                    @Override
                                                    public void onAnimationUpdate(ValueAnimator animation) {
                                                        // Transition along each axis of HSV (hue, saturation, value)
                                                        hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                                                        hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                                                        hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();
                                                        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.HSVToColor(to), Color.HSVToColor(hsv)});
                                                        gradient.setShape(GradientDrawable.RECTANGLE);

                                                        GradientDrawable gradientforbottomviewgradient = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{Color.HSVToColor(to), Color.argb(0, 0, 0, 0)});
                                                        gradientforbottomviewgradient.setShape(GradientDrawable.RECTANGLE);
                                                        sidepaletteforartistImage.setBackgroundColor(Color.HSVToColor(to));
                                                        bottompaletteforartistImage.setBackground(gradientforbottomviewgradient);



                                                        dragView.setBackgroundColor(Color.HSVToColor(to));
                                                        toolbar.setBackgroundColor(Color.HSVToColor(to));
                                                        tabs.setBackgroundColor(Color.HSVToColor(to));
                                                        toolbarInNowPlying.setBackgroundColor(Color.HSVToColor(to));
                                                        navigationView.setBackgroundColor(Color.HSVToColor(to));
                                                        Window window = MainActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
                                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
                                                        window.setStatusBarColor(Color.HSVToColor(to));
                                                        nowPlayingListContainer.setBackgroundColor(Color.HSVToColor(to));

                                                    }
                                                });

                                                anim.start();}


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


    ArrayList<CustomisedList> generaterecyclerlist(String artistName) {
        int numberofobjects = 0;

        ArrayList<CustomisedList> generatedList = new ArrayList<CustomisedList>();
        ArrayList<Songs> songListforalbums = new ArrayList<Songs>();

        for (Songs songs : dbsync.getArtistDetails(artistName)) {

            if (numberofobjects < 5)
                generatedList.add(new CustomisedList(1, null, songs, null, null));
            numberofobjects++;

        }

        numberofobjects = 0;
        songListforalbums = dbsync.getArtistDetails(artistName);
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


        if (artistSync.getArtistDetails(artistName) != null && !artistName.equals("")) {
            generatedList.add(new CustomisedList(0, artistSync.getArtistDetails(artistName), null, null, null));
        }


        return generatedList;
    }



    public ArrayList<ArtistPojo> getTopartist() {

        int noofSongs=0;

        ArrayList<ArtistPojo> popularartist = new ArrayList<ArtistPojo>();







        getArtistList.clear();


        for(ArtistPojo artistPojo:artistSync.getAllArtist() ){

            if(artistPojo!=null){


                if(dbsync.getArtistDetails(artistPojo.getName())!=null){

                    getArtistList.add(new PopularArtist(artistPojo,dbsync.getArtistDetails(artistPojo.getName()).size()));
                   }

            }

        }

        Collections.sort(getArtistList, new Comparator<PopularArtist>() {
            public int compare(PopularArtist a, PopularArtist b) {
                return a.getCount().compareTo(b.getCount());
            }
        });

        Collections.reverse(getArtistList);

        for (PopularArtist p:getArtistList){

            popularartist.add(p.getArtist());


        }




        return popularartist;
    }


    ArrayList<Songs> getTopAlbums(){

        ArrayList<Songs> albumListDemo = new ArrayList<Songs>();
        albumListDemo.clear();
        albumListDemo = dbsync.getAllContacts();
        Collections.sort(albumListDemo, new Comparator<Songs>() {
            public int compare(Songs a, Songs b) {
                return a.getAlbum().compareTo(b.getAlbum());

            }
        });
        albumListDemo.toArray();
        String Albumname = "dgfsfgwrthwr";
//int i =0;
        final ArrayList<PopularAlbum> tempArrayList = new ArrayList<PopularAlbum>();
        for (Songs s : albumListDemo) {

            // if(i ==0)Albumname = s.getAlbum();


            if (!Albumname.equals(s.getAlbum())) {
                // Log.e("Demo Test",String.valueOf(s.getAlbum().contains(Albumname)));

                if (!s.getAlbum().contains(Albumname)) {
                    if(dbsync.getAlbumDetails(s.getAlbum())!=null) {
                        tempArrayList.add(new PopularAlbum(dbsync.getAlbumDetails(s.getAlbum()).size(), s));
                        Albumname = s.getAlbum();
                    }
                }


            }

        }
        Collections.sort(tempArrayList, new Comparator<PopularAlbum>() {
            public int compare(PopularAlbum a, PopularAlbum b) {
                return a.getCount().compareTo(b.getCount());

            }
        });
        Collections.reverse(tempArrayList);



        albumListDemo.clear();
        for(PopularAlbum popularAlbum: tempArrayList){

            albumListDemo.add(popularAlbum.getAlbum());
        }


        return albumListDemo;
    }

    public class BackgroundProcess extends AsyncTask<Boolean,Boolean,Boolean>{

        @Override
        protected Boolean doInBackground(Boolean... booleans) {




            navbarrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            navbarrecycler.setAdapter(new SongAdapter(getApplicationContext(), dbsync.getrecentlyadded(), new SongAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Songs item) {
                    musicSrv.setList(dbsync.getrecentlyadded());
                    int songIndex = 0;

                    for (int index= 0 ; index < dbsync.getrecentlyadded().size();index++){

                        if(dbsync.getrecentlyadded().get(index).getTitle().equals(item.getTitle())){
                            songIndex = index;
                            break;
                        }



                    }
                    musicSrv.setSong(songIndex);
                    musicSrv.playSong();
                    updateUi();

                    updateProgressBar();
                    //  EventBus.getDefault().post(new GetMusicId(Integer.parseInt(String.valueOf(Songlist.indexOf(item)))));
                    // musicSrv.setSong(Integer.parseInt(String.valueOf(item.getID())));
                    //    Log.e("hello",String.valueOf(item.getID()));
//                musicSrv.playSong();
                }
            }));

            return null;
        }
    }

    public class Updatesongdatabase extends AsyncTask<Boolean,Boolean,Boolean>{

        int counter = 1;
        boolean isDeleted = true;
        boolean isAdded = true;


        @Override
        protected Boolean doInBackground(Boolean... booleans) {


            if(dbsync.getAllContacts().size()!=getSongList().size()) {

                for (Songs s : dbsync.getAllContacts()) {

                    isDeleted = true;

                    for (Songs y : getSongList()) {
                        // Log.e("Deleted ",y.getTitle()+"");

                        if (s.getTitle().equals(y.getTitle())) {


                            isDeleted = false;

                        }

                    }

                    if (isDeleted == true) {
                        dbsync.deleteSongs(s);
                        Log.e("Deleted ", s.getTitle() + "");

                    }
                    // Log.e("Checked ",s.getTitle()+"");

                }

                for (Songs s : getSongList()) {

                    isAdded = true;

                    for (Songs y : dbsync.getAllContacts()) {

                        if (s.getTitle().equals(y.getTitle())) {


                            isAdded = false;

                        }

                    }
                    if (isAdded) {
                        dbsync.addSongs(s);
                        Log.e("Added ", s.getTitle() + "");
                    }


                }


            }



            return null;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
        }

        public ArrayList<Songs> getSongList() {
            //retrieve song info

            ArrayList<Songs> getsongs = new ArrayList<>();
            ContentResolver musicResolver = getApplicationContext().getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

            if(musicCursor!=null && musicCursor.moveToFirst()){
                //get columns

                int songIndex= 0;
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int album = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

                int albumid = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int artistid = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
                int dateAdded = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);







                //add songs to list
                do {
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String thisAlbum = musicCursor.getString(album);
                    String thisAlbumid = musicCursor.getString(albumid);
                    String thisArtistid = musicCursor.getString(artistid);
                    String thisdateAdded=musicCursor.getString(dateAdded);

                    final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, Long.parseLong(thisAlbumid));

///                SharedPreferences.Editor editor = pref.edit();
//
//                //songDb.addSongs(new Songs(thisId, thisTitle, thisArtist,thisAlbum,thisAlbumid,albumArtUri,songIndex));
//                // Storing integer
//                if(pref.getInt("noofsongs",0)<songList.size()){
//                    songDb.addSongs(new Songs(thisId, thisTitle, thisArtist,thisAlbum,thisAlbumid,albumArtUri,songIndex));
//                }
//                editor.putInt("noofsongs", songDb.getContactsCount());
//                editor.commit();
                    // songList = songDb.getAllContacts();
                    getsongs.add(new Songs(thisId, thisTitle, thisArtist, thisArtistid, Long.parseLong(thisdateAdded) ,thisAlbum,thisAlbumid,albumArtUri,songIndex));



                    songIndex++;
                }
                while (musicCursor.moveToNext());

            }

            return getsongs;

        }
    }
}