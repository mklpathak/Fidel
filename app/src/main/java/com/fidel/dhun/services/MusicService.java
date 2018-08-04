package com.fidel.dhun.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.fidel.dhun.R;
import com.fidel.dhun.data.Api;
import com.fidel.dhun.data.ApiUtils;
import com.fidel.dhun.data.Songs;
import com.fidel.dhun.data.updateui.UpdateNextSongList;
import com.fidel.dhun.data.updateui.UpdateUi;
import com.fidel.dhun.data.updateui.UpdateUiforsearch;
import com.fidel.dhun.data.updateui.UpdateUiforsharedalbum;
import com.fidel.dhun.data.updateui.UpdateuiforTags;
import com.fidel.dhun.data.updateui.UpdateuiforsharedArtist;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Mukul on 29-07-2017.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    public static final String ACTION_PLAY = "com.valdioveliu.valdio.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.valdioveliu.valdio.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.valdioveliu.valdio.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.valdioveliu.valdio.audioplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.valdioveliu.valdio.audioplayer.ACTION_STOP";
    Bitmap notificationalbumart = null;
    private boolean headsetConnected = false;
    RemoteControlClient remoteControlClient = null;

    String playingStatus = "";

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;
    private String songTitle = "";
    private String songArtist = "";
    private String songAlbum = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private boolean isSongPrepared = false;
    private Random rand;
    private Uri albumart = null;
    private String temptitle= "";
    private ArrayList<String> artistlist;
    Api artistApi ;
    int i = 0;
    int j  = 0 ;

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer player;
    //song list
    private ArrayList<Songs> songs;
    private ArrayList<Songs> UnShuffledList;
    //current position
    private int songPosn;


    private static final String TAG = "TTSService";
    public static MusicService sInstance;




    @Override
    public void onCreate() {
        super.onCreate();
        songPosn = 0;
        rand = new Random();
        //create player
        artistlist = new ArrayList<String>();
        this.registerReceiver(new HeadsetConnectionReceiver(),
                new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        player = new MediaPlayer();
        initMusicPlayer();
        artistApi = ApiUtils.getArtist();

    }

    public void initMusicPlayer() {
        //set player properties

        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }



    public void setList(ArrayList<Songs> theSongs) {

        songs = theSongs;
        UnShuffledList = new ArrayList<>(songs);
        if(shuffle)
        Collections.shuffle(songs);


    } public Boolean getSuffleStatus(){
        return shuffle;
    }
    public ArrayList<Songs> getList() {
        return songs;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return musicBind;
    }

    //suffle
    public Boolean setShuffle() {
        if (shuffle) shuffle = false;

        else shuffle = true;



        return shuffle;
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //player.stop();
        //player.release();
        return false;
    }

    public void playSong() {
        //play a song
        player.reset();
        //get song
        Songs playSong = songs.get(songPosn);

//get id
        long currSong = playSong.getID();
        songTitle = playSong.getTitle();
        songArtist = playSong.getArtist();

        albumart = playSong.getAlbumarturi();
//set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
       if(requestAudioFocusForMyApp(getApplicationContext())){
        player.prepareAsync();
        }
        buildNotification(true);

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (player.getCurrentPosition() < 0) {
            mediaPlayer.reset();
            playNext();
        }

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
//        Intent notIntent = new Intent(this, MainActivity.class);
//        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
//                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification.Builder builder = new Notification.Builder(this);
//
//        builder.setContentIntent(pendInt)
//                .setSmallIcon(R.drawable.play)
//                .setTicker(songTitle)
//                .setOngoing(true)
//                .setContentTitle("Playing")
//                .setContentText(songTitle);
//        Notification not = builder.build();
//
//        startForeground(NOTIFY_ID, not);
        try {
            initMediaSession();
        }
        catch (RemoteException e){


        }

        isSongPrepared = true;

    }

    public void setSong(int songIndex) {
        songPosn = songIndex;

        if(shuffle){
            if(temptitle!=null){
           temptitle= UnShuffledList.get(songIndex).getTitle();
                songPosn=0;
           for(Songs s:songs){
               if(temptitle.equals(s.getTitle()))
                   break;

               songPosn++;

           }

            }
        }
    }

    public UpdateNextSongList getNextlist() {
        if(songs!=null &&songs.size()>=1 )
        return new UpdateNextSongList(songs,songs.get(songPosn));
        else
            return null;
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public String getSongTitle() {
        return songTitle;

    }

    public Uri getAlbumart() {

        return albumart;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public boolean isPng() {
        return player.isPlaying();
    }


    public void pausePlayer() {
        player.pause();
        buildNotification(false);
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
    if(requestAudioFocusForMyApp(getApplicationContext())){
    player.start();}
        buildNotification(true);
    }

    public void playPrev() {
        songPosn--;
        if (songPosn < 0) songPosn = songs.size() - 1;

        if(player.isPlaying()){
        playSong();
            buildNotification(true);}
        else {
            buildNotification(false);
        }

//        EventBus.getDefault().post(new UpdateNextSongList(songs,songs.get(songPosn)));
//        EventBus.getDefault().post(new Updatenextplayingforalbums(songs,songs.get(songPosn)));
//        EventBus.getDefault().post(new Updatenextplayingforartist(songs,songs.get(songPosn)));
//        EventBus.getDefault().post(new UpdateNextPlayingforsearch(songs,songs.get(songPosn)));
//        EventBus.getDefault().post(new UpdateNextPlayingforTags(songs,songs.get(songPosn)));
    }

    public void playNext() {
        songPosn++;

            if (songPosn >= songs.size()) songPosn = 0;
            if(player.isPlaying()){
                playSong();
                buildNotification(true);}
            else {
                buildNotification(false);
            }

        EventBus.getDefault().post(new UpdateNextSongList(songs,songs.get(songPosn)));

    }


    private boolean requestAudioFocusForMyApp(final Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        // Request audio focus for playback
        int result = am.requestAudioFocus(mOnAudioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d("AudioFocus", "Audio focus received");
            return true;
        } else {
            Log.d("AudioFocus", "Audio focus NOT received");
            return false;
        }
    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.i(TAG, "AUDIOFOCUS_GAIN");
                    //restart/resume your sound
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    pausePlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    //Loss of audio focus for a short time
                    //Pause playing the sound
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    //Loss of audio focus for a short time.
                    //But one can duck. Lower the volume of playing the sound
                    break;

                default:
                    //
            }
        }
    };
    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                Log.e("Play Buttom","pressed");
                go();
                EventBus.getDefault().post(new UpdateUi(true));
                EventBus.getDefault().post(new UpdateUiforsearch(true));
                EventBus.getDefault().post(new UpdateUiforsharedalbum(true));
                EventBus.getDefault().post(new UpdateuiforsharedArtist(true));
                EventBus.getDefault().post(new UpdateuiforTags(true));
                buildNotification(isPng());
            }

            @Override
            public void onPause() {
                super.onPause();
                Log.e("pause Buttom","pressed");
                pausePlayer();
                EventBus.getDefault().post(new UpdateUi(true));
                EventBus.getDefault().post(new UpdateUiforsearch(true));
                EventBus.getDefault().post(new UpdateUiforsharedalbum(true));
                EventBus.getDefault().post(new UpdateuiforsharedArtist(true));
                EventBus.getDefault().post(new UpdateuiforTags(true));
                buildNotification(false);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                Log.e("Next","pressed");
                playNext();
                EventBus.getDefault().post(new UpdateUi(true));
                EventBus.getDefault().post(new UpdateUiforsearch(true));
                EventBus.getDefault().post(new UpdateUiforsharedalbum(true));
                EventBus.getDefault().post(new UpdateuiforsharedArtist(true));
                EventBus.getDefault().post(new UpdateuiforTags(true));
                updateMetaData();
                if(player.isPlaying()){
                    playSong();
                    buildNotification(true);}
                else {
                    buildNotification(false);
                }

            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                Log.e("Prev","pressed");
                playPrev();
                EventBus.getDefault().post(new UpdateUi(true));
                EventBus.getDefault().post(new UpdateUiforsearch(true));
                EventBus.getDefault().post(new UpdateUiforsharedalbum(true));
                EventBus.getDefault().post(new UpdateuiforsharedArtist(true));
                EventBus.getDefault().post(new UpdateuiforTags(true));
                updateMetaData();
                if(player.isPlaying()){
                    playSong();
                    buildNotification(true);}
                else {
                    buildNotification(false);
                }
            }

            @Override
            public void onStop() {
                super.onStop();
                EventBus.getDefault().post(new UpdateUi(true));
                EventBus.getDefault().post(new UpdateUiforsearch(true));
                EventBus.getDefault().post(new UpdateUiforsharedalbum(true));
                EventBus.getDefault().post(new UpdateuiforsharedArtist(true));
                EventBus.getDefault().post(new UpdateuiforTags(true));
                removeNotification();
                //Stop the service
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });


    }

    private void updateMetaData() {

        Picasso.with(getApplicationContext()).load(albumart).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                notificationalbumart = bitmap;
                mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, notificationalbumart)
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, songArtist)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, songAlbum)
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songTitle)
                        .putBitmap(MediaMetadata.METADATA_KEY_ART, notificationalbumart)
                        .build());

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        }); //replace with medias albumArt
        // Update the current metadata

    }

    private void buildNotification(Boolean isSongPlaying) {

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (isSongPlaying) {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else  {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }
        try {
            if (notificationalbumart!=null){
            notificationalbumart = MediaStore.Images.Media.getBitmap(this.getContentResolver(),albumart);}
        } catch (IOException e) {
            e.printStackTrace();
        }




        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(getResources().getColor(R.color.colorPrimary))
                // Set the large and small icons
                .setLargeIcon(notificationalbumart)
                .setSmallIcon(R.drawable.fidelnotify)
                // Set Notification content information
                .setContentText(songArtist)
                .setContentTitle(songAlbum)
                .setContentInfo(songTitle)
                // Add playback actions
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MusicService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaSessionManager == null) {
            try {
                initMediaSession();
                initMusicPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }

        }
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }
    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }


    public class HeadsetConnectionReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")){
                if (headsetConnected && intent.getIntExtra("state", 0) == 0){
                    headsetConnected = false;

                       player.pause();

                } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                    headsetConnected = true;
                }
            }
        }

    }

}