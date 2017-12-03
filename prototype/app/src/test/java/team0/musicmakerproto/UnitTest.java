package team0.musicmakerproto;

import android.content.Context;
import android.media.MediaPlayer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class UnitTest {
    Playlist playlist;

    @Before
    public void init()
    {
        playlist = new Playlist();
        playlist.setPlaylistName("testPlaylist");
        playlist.addSong(new Song("Test", "none", "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.weeknd, "Test", "30", 0));
    }

    @Test
    public void testSQLPlaylistCreation() {
        insertPlaylist(playlist);
        Playlist newP = new Playlist("testPlaylist");

        assertEquals("Playlist creation of playlist with existing name", false, insertPlaylist(newP));

    }

    private boolean insertPlaylist(Playlist p)
    {
        AddPlaylistActivity act = new AddPlaylistActivity();
        return act.SQLCreatePlaylist(p);
    }


}