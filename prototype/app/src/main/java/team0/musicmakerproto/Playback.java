package team0.musicmakerproto;

import android.app.Service;
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
public class Playback extends Service {

    public 


    private static Playback instance = null;
    private MediaPlayer currentSong;
    int pauseTime;
    Playlist playlist; //Keep track of the playlist from which the current song is stored.
    int id;

    Context context;

    private Playback()
    {
        currentSong = null;
        pauseTime = 0;
        id = 0;
        context = null;
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

        //Do a bounds check to see if the ID needs to circle around to 0.
        if(id + 1 >= playlist.size())
            id = 0;
        else
            id++;

        //Begin playback of next song.
        togglePlay(id, playlist, context);
    }

    //Skip to the previous song in the playlist
    public void skipBackward()
    {
        //If playlist hasn't been initialized, don't run the function.
        if(playlist == null)
            return;

        //Do a bounds check to see if the ID needs to circle around to 0.
        if(id - 1 < 0)
            id = playlist.size() - 1;
        else
            id--;

        //Begin playback of previous song.
        togglePlay(id, playlist, context);

    }

<<d<<<<< HEAD
    //Shuffles the playlist
    public void shufflePlaylist()
    {
        //Shuffle the playlist
        for (int i = 0; i<playlist.size(); ++i)
        {
            Random rand = new Random();
            int temp = rand.nextInt(playlist.size() - i) + i;

        }


    }




=======
    //Return the name of the song.
>>>>>>> f1a096ee41f9156e57df8cce0d23cc9e20fac441
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

}
