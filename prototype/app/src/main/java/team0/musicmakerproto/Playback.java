package team0.musicmakerproto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Class: Playback
 * Description: Provides playback functionality and keeps track of the current song being played.
 * Singleton class (since we want only one instance of playback at any given time).
 */

//TODO
    //looping
    //skip back
    //go to next song when current song finishes playing (reaches end of time)
    //update current time of playback through a thread.
    //implement this class to extend Service
    //shuffling
public class Playback {
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

    public static Playback getInstance()
    {
        if(instance == null)
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


    public void skipBackward()
    {

    }

    public String getSongName()
    {
        if(playlist != null) {
            String name = playlist.getSongs().get(id).getTitle();
            if (name != null)
                return name;
        }
        return "";
    }

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
