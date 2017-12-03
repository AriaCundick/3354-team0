package team0.musicmakerproto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.util.Collections;
import java.util.Random;

/**
 * Class: Playback
 * Description: Provides playback functionality and keeps track of the current song being played.
 * Singleton class (since we want only one instance of playback at any given time).
 */

//TODO
    //looping
    //go to next song when current song finishes playing (reaches end of time)
    //update current time of playback through a thread.
    //implement this class to extend Service
    //shuffling
public class Playback {
    private static Playback instance = null;
    private MediaPlayer currentSong;
    private int pauseTime, id, shuffleIndex;
    private Playlist playlist; //Keep track of the playlist from which the current song is stored.
    private boolean isShuffling, isLooping;
    private int[] shuffleOrder;

    Context context;

    private Playback()
    {
        currentSong = null;
        pauseTime = 0;
        id = 0;
        context = null;
        isShuffling = isLooping = false;
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
                    // currentSong = MediaPlayer.create(this, R.raw.weeknd);
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

        currentSong = MediaPlayer.create(c, Uri.parse(p.getSongs().get(id).getPath()));
        Log.i("rip1", "here");
        togglePlay();
        playlist = p;
        this.id = id;
        context = c;
    }

    //Skip to the next song in the playlist
    public void skipForward()
    {
        //If playlist hasn't been initialized, don't run the function.
        if(playlist == null)
            return;


        if(isShuffling) //play the next song in the shuffled order.
        {
            shuffleIndex = boundsCheckPos(shuffleIndex);
            togglePlay(shuffleIndex, playlist, context);
        }
        else if(!isLooping) //play the next song in sequential order.
        {
            id = boundsCheckPos(id);
            togglePlay(id, playlist, context);
        }
        else
            togglePlay(id, playlist, context); //replay the current song.
    }

    //Skip to the previous song in the playlist
    public void skipBackward()
    {
        //If playlist hasn't been initialized, don't run the function.
        if(playlist == null)
            return;

        if(isShuffling) //play the next song in the shuffled order.
        {
            shuffleIndex = boundsCheckNeg(shuffleIndex);
            togglePlay(shuffleIndex, playlist, context);
        }
        else if(!isLooping) //play the next song in sequential order.
        {
            id = boundsCheckNeg(id);
            togglePlay(id, playlist, context);
        }
        else
            togglePlay(id, playlist, context); //replay the current song.

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

    public void setShuffling() { isShuffling = !isShuffling; }
    public void setLooping() { isLooping = !isLooping; }

    //Do a bounds check to see if the ID needs to circle around to 0.
    private int boundsCheckPos(int id)
    {
        if (id + 1 >= playlist.size())
            id = 0;
        else
            id++;

        return id;
    }

    //Do a bounds check to see if the ID needs to circle around to 0.
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
            while(swapIndex == i) swapIndex = rand.nextInt(playlist.size() - 1) + 1; //Ensures swapIndex isn't the same as i
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
