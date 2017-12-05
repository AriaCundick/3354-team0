package team0.musicmakerproto;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

/**
 * Class: Playback
 * Description: Provides playback functionality and keeps track of the current song being played.
 * Singleton class (since we want only one instance of playback at any given time).
 */

//TODO
    //implement this class to extend Service
@SuppressWarnings("WeakerAccess")
public class Playback extends Service {
    private static Playback instance = null;
    private MediaPlayer currentSong;
    private Playlist playlist; //Keep track of the playlist from which the current song is stored.
    private int pauseTime, id, shuffleIndex;
    private boolean isShuffling, isLooping;
    private int[] shuffleOrder;
    private String activityName;

    Context context;

    private Playback()
    {
        currentSong = null;
        pauseTime = 0;
        id = 0;
        context = null;
        isShuffling = isLooping = false;
        activityName = "NoActivity";
    }

    @Nullable
    @Override
    //Binds service with activity, else returns null.
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    //Method called when service starts
    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getIntExtra("id", 0);
        playlist = intent.getParcelableExtra("playlist");

        togglePlay(id, playlist, context);
        return START_STICKY;
    }

    public void setPlaylist(Playlist p) {playlist = p;}
    public void setId(int i) {id = i;}

    @Override
    //Method called when service stops
    public void onDestroy() {
        super.onDestroy();

        stopSong();
    }

    //Get the static instance of the playback class
    public static Playback getInstance()
    {
        if(instance == null) //If the instance is null, instantiate a new one.
            instance = new Playback();

        return instance;
    }

    //Handles playing and pausing the current song.
    public void togglePlay()
    {
        if(currentSong != null)
        {
            //If the playback is paused.
            if (!currentSong.isPlaying()) {
                if (currentSong == null) { //If song is null, instantiate an instance of a song.
                    currentSong.start();

                } else if (!currentSong.isPlaying()) {
                    currentSong.start();
                }
            } else { //else if the song is currently playing
                if (currentSong != null) {
                    currentSong.pause();
                    pauseTime = currentSong.getCurrentPosition();
                }
            }
        }
    }

    public MediaPlayer getCurrentSong() { return currentSong;}
    public void setActivityName(String s) {activityName = s;}
    public void setContext(Context c) {context = c;}

    //Stop the mediaPlayer device.
    private void stopSong()
    {
        if(currentSong != null)
        {
            currentSong.stop();
            currentSong.release();
        }
    }

    //Play a song based on id of the listview it was selected in.
    public void togglePlay(int id, Playlist p, Context c)
    {
        stopSong();

        //Create a new instance of the MediaPlayer class.
        currentSong = MediaPlayer.create(c, Uri.parse(p.getSongs().get(id).getPath()));
        updateGUIs();
        context = c;

        //Update the GUI of the current activity when the song finishes playing
        //and also go to the next song.
        currentSong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                skipForward();
                updateGUIs();
            }
        });

        togglePlay();
        playlist = p;
        this.id = id;

    }

    //Skip to the next song in the playlist
    public void skipForward()
    {
        //If playlist hasn't been initialized, don't run the function.
        if(playlist == null)
            return;


        if(isLooping) //replay the current song.
            togglePlay(id, playlist, context);
        else if(isShuffling) //play the next song in the shuffled order.
        {
            shuffleIndex = boundsCheckPos(shuffleIndex);
            togglePlay(shuffleOrder[shuffleIndex], playlist, context);
        }
        else  //play the next song in sequential order.
        {
            id = boundsCheckPos(id);
            togglePlay(id, playlist, context);
        }

    }

    //Skip to the previous song in the playlist
    public void skipBackward()
    {
        //If playlist hasn't been initialized, don't run the function.
        if(playlist == null)
            return;

        if(isLooping) //replay the current song.
            togglePlay(id, playlist, context);
        else if(isShuffling) { //play the previous song in sequential order.
            shuffleIndex = boundsCheckNeg(shuffleIndex);
            togglePlay(shuffleOrder[shuffleIndex], playlist, context);
        }
        else  //play the previous song in sequential order.
        {
            id = boundsCheckNeg(id);
            togglePlay(id, playlist, context);
        }

    }

    //Return the name of the song.
    public String getSongName()
    {
        if(playlist != null) {
            String name = playlist.getSongs().get(id).getTitle();
            if (name != null)
                return name;
        }
        return "";
    }

    //Return the name of the artist of the current song.
    public String getSongArtist()
    {
        if(playlist != null) {
            String name = playlist.getSongs().get(id).getArtist();
            if (name != null)
                return name;
        }
        return "";
    }

    //Get the path of the current song
    public String getSongPath()
    {
        if(playlist != null) {
            String name = playlist.getSongs().get(id).getPath();
            if (name != null)
                return name;
        }
        return "";
    }
    //Get the song's album art, ore return a default picture.
    public Bitmap getSongIMG(Resources r)
    {
        if(playlist != null) {
            Bitmap img = playlist.getSongs().get(id).getImage();
            if (img != null)
                return img;
        }
        return  BitmapFactory.decodeResource(r, R.drawable.ic_default_albumart2);
    }

    //GUI onClick calls this function
    //Set the shuffling boolean to the opposite value.
    public void toggleShuffling()
    {
        isShuffling = !isShuffling;

        if(isShuffling) //if true...
            shuffleOrder(); //Reorder the shuffle sequence.

    }

    //Set the looping boolean to the opposite value.
    public void toggleLooping() { isLooping = !isLooping; }
    public boolean getShuffling() {return isShuffling;}
    public boolean getLooping() {return isLooping;}

    //Updates the GUI of the current activity for the playback bar
    private void updateGUIs()
    {
        switch(activityName) //Switch case based on the current activity name passed onCreation/onResume of an activity.
        {
            case "LibraryActivity":
                TextView songName = (TextView) ((Activity)context).findViewById(R.id.collection_song_name);
                ImageView songIMG = (ImageView) ((Activity)context).findViewById(R.id.notes_albumCover);
                songName.setText(getSongName());
                songIMG.setImageBitmap(getSongIMG((context).getResources()));
                break;
            case "NotesActivity":
                TextView songName1 = (TextView) ((Activity)context).findViewById(R.id.notesview_song_name);
                ImageView songIMG1 = (ImageView) ((Activity)context).findViewById(R.id.notes_albumCover);
                songName1.setText(getSongName());
                songIMG1.setImageBitmap(getSongIMG((context).getResources()));
                break;
            case "PlaylistViewActivity":
                TextView songName2 = (TextView) ((Activity)context).findViewById(R.id.playlist_view_song_name);
                ImageView songIMG2 = (ImageView) ((Activity)context).findViewById(R.id.notes_albumCover);
                songName2.setText(getSongName());
                songIMG2.setImageBitmap(getSongIMG((context).getResources()));
                break;
            case "CurrentSongActivity":
                ImageView songIMG3 =(ImageView) ((Activity)context).findViewById(R.id.albumArtIMG);
                TextView songTitle3 = (TextView) ((Activity)context).findViewById(R.id.songName_current_song_view);
                TextView songArtist3 = (TextView) ((Activity)context).findViewById(R.id.artistName_current_song_view);
                SeekBar seeker = (SeekBar) ((Activity)context).findViewById(R.id.scrubber);
                songIMG3.setImageBitmap(getSongIMG((context).getResources()));
                songTitle3.setText(getSongName());
                songArtist3.setText(getSongArtist());
                seeker.setMax(currentSong.getDuration());
            default:
        }
    }

    //Do a bounds check to see if the ID needs to circle around to 0.
    private int boundsCheckPos(int id)
    {
        if (id + 1 >= playlist.size())
            id = 0;
        else
            id++;

        return id;
    }

    //Do a bounds check to see if the ID needs to circle around to the size of the playlist.
    private int boundsCheckNeg(int id)
    {
        if(id - 1 < 0)
            id = playlist.size() - 1;
        else
            id--;

        return id;
    }

    //Create the order in which to shuffle songs from the playlist
    private void shuffleOrder()
    {
        shuffleOrder = new int[playlist.size()];
        shuffleIndex = 0;

        for(int i = 0; i <playlist.size(); i++)
            shuffleOrder[i] = i;

        //Swap the current id to the first position
        //so it doesn't get played again in the shuffle
        shuffleOrder = swap(shuffleOrder, id, 0);

        for(int i = 1; i < playlist.size(); i++)
        {
            //Swap shuffleOrder[i] with any random element from the array
            Random rand = new Random();
            int swapIndex = rand.nextInt(playlist.size() - 1) + 1;

            //Ensures swapIndex isn't the same as i
            while(swapIndex == i)
                swapIndex = rand.nextInt(playlist.size() - 1) + 1;

            shuffleOrder = swap(shuffleOrder, i, swapIndex);
        }

    }

    //Swaps two elements of an array.
    private int[] swap(int[] arr, int i1, int i2)
    {
        int temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
        return arr;
    }

}
