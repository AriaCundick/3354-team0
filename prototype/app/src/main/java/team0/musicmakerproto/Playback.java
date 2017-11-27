package team0.musicmakerproto;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Class: Playback
 * Description: Provides playback functionality and keeps track of the current song being played.
 * Singleton class (since we want only one instance of playback at any given time).
 */

public class Playback {
    private static Playback instance = null;
    private MediaPlayer currentSong;
    int pauseTime;
    Playlist playlist; //Keep track of the playlist from which the current song is stored.
    boolean isPlaying; //Keep track of if a song is currently played or paused.
    int id;

    Context context;

    private Playback()
    {
        currentSong = null;
        pauseTime = 0;
        isPlaying = false;
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
        //If the playback is paused.
        if(!isPlaying) {
            isPlaying = !isPlaying;
            if (currentSong == null) { //If song is null, instantiate an instance of a song.
                // currentSong = MediaPlayer.create(this, R.raw.weeknd);
                currentSong.start();

            } else if (!currentSong.isPlaying()) {
                currentSong.start();
            }
        }
        else { //else if the song is currently playing
            isPlaying = !isPlaying;
            if(currentSong != null) {
                currentSong.pause();
                pauseTime = currentSong.getCurrentPosition();
            }
        }
    }


    public void togglePlay(int id, Playlist p, Context c)
    {
        if(isPlaying)
            currentSong.stop();

        isPlaying = false;
        currentSong = MediaPlayer.create(c, Uri.parse(p.getSongs().get(id).getPath()));
        togglePlay();
        playlist = p;
        this.id = id;
        context = c;
    }


    //Skip to the next song in the playlist
    public void skipForward()
    {
        //Stop the current song being played.
        if(isPlaying)
            currentSong.stop();

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

}
