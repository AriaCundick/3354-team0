package team0.musicmakerproto;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.GridView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.v4.content.ContextCompat.startActivity;
import static org.junit.Assert.*;

/**
 * Created by Shekhar on 12/3/17.
 */

@RunWith(AndroidJUnit4.class)
public class LibraryActivityTest   {
    ActivityTestRule<LibraryActivity> activityRule = new ActivityTestRule<>(LibraryActivity.class);
    LibraryActivity act;

    @Before
    public void setUp() throws Exception {
        activityRule.launchActivity(null);
        act = activityRule.getActivity();
    }

    @Test
    public void elementTest()
    {
        GridView playlists = (GridView) act.findViewById(R.id.PlaylistGrid);
        int existing_playlists = ((PlaylistAdapter)playlists.getAdapter()).getCount();

        assertEquals("Initial playlist for playlist grid:", true, existing_playlists >= 1 );

    }
}
