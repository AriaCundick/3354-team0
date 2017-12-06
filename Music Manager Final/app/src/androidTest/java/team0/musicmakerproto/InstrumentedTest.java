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

    //Test the return values for no song image.
    @Test
    public void testSongIMG()
    {
        Song s = new Song("", "", "/made/up/dir", "", "", 0);
        assertEquals("Get Image object for made up path:", null, s.getImage());
        s.setPath("");
        assertEquals("Get Image object for blank path:", null, s.getImage());
        s.setPath("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.weeknd);
        assertEquals("Get Image object for valid path:", BitmapFactory.decodeResource(resources, R.drawable.ic_default_albumart2), s.getImage());

    }

}
