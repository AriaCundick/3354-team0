package team0.musicmakerproto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotesActivity extends AppCompatActivity {

    private Playback playback = Playback.getInstance();
    private TextView title;
    private ImageView songIMG;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_library:
                    Intent intent = new Intent(NotesActivity.this, LibraryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.nav_notes:

                    return true;
                case R.id.nav_settings:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //Bind GUI elements;
        title = (TextView) findViewById(R.id.notesview_song_name);
        songIMG = (ImageView) findViewById(R.id.notes_albumCover);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true); // Set navigationView Notes item to be marked.

        updatePlaybackBar();
    }

    //Maps to the play/pause button in the xml file.
    //Plays or pauses the current song.
    public void playPauseBtnPressed(View view)
    {
        playback.togglePlay();
    }

    //Maps to the skipForward button in the xml file
    //Skips to the next song.
    public void skipForwardBtnPressed(View view)
    {
        playback.skipForward();
        updatePlaybackBar();
    }

    //Updates the GUI to show information regarding the current song being played.
    private void updatePlaybackBar()
    {
        title.setText(playback.getSongName());
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
    }

}
