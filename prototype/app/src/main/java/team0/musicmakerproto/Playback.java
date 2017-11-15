package team0.musicmakerproto;

import android.media.MediaPlayer;
import android.widget.Toast;

/**
 * Class: Playback
 * Description: Provides playback functionality and keeps track of the current song being played.
 */

public class Playback {
    MediaPlayer currentSong;
    int pause;
    Playlist playlist; //Keep track of the playlist from which the current song is stored.
    boolean isPlaying; //Keep track of if a song is currently played or paused.

    public Playback()
    {

    }

    public void play()
    {
        if (currentSong == null) {
           // currentSong = MediaPlayer.create(this, R.raw.weeknd);
            currentSong.start();

    }
        else if(!currentSong.isPlaying()){
            currentSong.seekTo(pause);
            currentSong.start();
        }
    }

    public void play(int id)
    {

    }

    private void skipForward()
    {
    Playlist.skipForward();

    }

    private static void skipBackward()
    {
        Playback.skipBackward();
    }

}
