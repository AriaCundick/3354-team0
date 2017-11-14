package team0.musicmakerproto;

import android.media.MediaPlayer;

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

    private Playback()
    {
        currentSong = null;
        pauseTime = 0;
        isPlaying = false;
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
                currentSong.seekTo(pauseTime);
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

    //
    public void togglePlay(int id)
    {

    }


    private static void skipForward()
    {

    }

    private static void skipBackward()
    {

    }

}
