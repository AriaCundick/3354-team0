package team0.musicmakerproto;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentSongActivity extends AppCompatActivity {

    Playback playback = Playback.getInstance();
    ImageButton repeat;
    ImageButton shuffle;
    ImageButton noteActivity;
    ImageView songIMG;
    TextView songTitle, songArtist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        //Bind GUI elements.
        repeat = (ImageButton) findViewById(R.id.repeat);
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        noteActivity = (ImageButton) findViewById(R.id.noteActivity);
        songIMG =(ImageView) findViewById(R.id.albumArtIMG);
        songTitle = (TextView) findViewById(R.id.songName_current_song_view);
        songArtist = (TextView) findViewById(R.id.artistName_current_song_view);

        // repeat button onClickListener
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat.setColorFilter(Color.parseColor("#6eb7a7"));
            }
        });

        // shuffle button onClickListener
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle.setColorFilter(Color.parseColor("#d19e4f"));
                playback.setShuffling();
            }
        });

        // open note activity
        noteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadNote = new Intent(CurrentSongActivity.this,IndividualNote.class);
                loadNote.putExtra("note_name", playback.getSongName());
                loadNote.putExtra("note_path", playback.getSongPath());
                loadNote.putExtra("note_contents",getNoteForSong());
                loadNote.putExtra("caller", "CurrentSongActivity");
                startActivity(loadNote);
            }
        });

        updateGUI();
    }

    //Call to SQL db to find out if note has been created for the song.
    private String getNoteForSong()
    {
        //implement sql call for finding the note based on its path
        //if it exists, return the contents of the note

        //else...
        return "";
    }

    private void updateGUI()
    {
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
        songTitle.setText(playback.getSongName());
        songArtist.setText(playback.getSongArtist());
    }

    public void btnPlayPause(View v) { playback.togglePlay(); }
    public void btnSkipForward(View v)
    {
        playback.skipForward();
        updateGUI();
    }

    public void btnSkipBackward(View v)
    {
        playback.skipBackward();
        updateGUI();
    }




}
