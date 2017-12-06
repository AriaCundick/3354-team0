package team0.musicmakerproto;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static team0.musicmakerproto.Song.resources;


@RunWith(AndroidJUnit4.class)
public class BoundsTestCheck {
    private Playlist playlist;


    @Before
    public void initialize()
    {
        playlist = new Playlist();
        playlist.setPlaylistName("testPlaylist");
        playlist.addSong(new Song("Test", "none", "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.weeknd, "Test", "30", 0));
    }

    // Test to see if an index out of bounds exception is triggered on skipping a song in a playlist of size 1.
    @Test
    public void testBoundsChecking() {
        Playback p = Playback.getInstance();

        // Play the only song in the playlist
        p.togglePlay(0, playlist, InstrumentationRegistry.getContext());
        String songName1 = p.getSongName();

        // Skip forward, which should lead to the same song being played
        p.skipForward();
        String songName2 = p.getSongName();

        // Skip backward, leading to the same song as well.
        p.skipBackward();
        String songName3 = p.getSongName();

        assertEquals("First played song and second played song:", songName1, songName2);
        assertEquals("First played song and 3rd played song:", songName1, songName3);
    }

}