package team0.musicmakerproto;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    private Playlist playlist;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("team0.musicmakerproto", appContext.getPackageName());
    }

    @Before
    public void initialize()
    {
        playlist = new Playlist();
        playlist.setPlaylistName("testPlaylist");
        playlist.addSong(new Song("Test", "none", "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.weeknd, "Test", "30", 0));
    }

    //Test the MediaPlayer resourcese to ensure that resources do not run out
    //on loading 1000 songs into the MediaPlayer object.
    @Test
    public void testPlaybackResources() {
        Playback p = Playback.getInstance();

        //play the song, then skip it 100 times to make sure MediaPlayer resources don't run out in the Playback class
        p.togglePlay(0, playlist, InstrumentationRegistry.getContext());

        for(int i = 0; i < 1000; i ++)
            p.skipForward();

        assertEquals("Current song status:", true, p.getCurrentSong().isPlaying());

    }

    // Test to see if an index out of bounds exception is triggered on skipping a song in a playlist of size 1.
    @Test
    public void testBoundsChecking() {
        Playback p = Playback.getInstance();

        //play the only song in the playlist
        p.togglePlay(0, playlist, InstrumentationRegistry.getContext());
        String songName1 = p.getSongName();

        //Skip forward, which should lead to the same song being played
        p.skipForward();
        String songName2 = p.getSongName();

        //Skip backward, leading to the same song as well.
        p.skipBackward();
        String songName3 = p.getSongName();

        assertEquals("First played song and second played song:", songName1, songName2);
        assertEquals("First played song and 3rd played song:", songName1, songName3);
    }


}
