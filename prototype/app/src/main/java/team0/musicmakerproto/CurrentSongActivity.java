package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentSongActivity extends AppCompatActivity {

    DatabaseHelper SQLdb;
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

        //SQL database instantiation
        SQLdb = new DatabaseHelper(this);

        //Bind GUI elements.
        repeat = (ImageButton) findViewById(R.id.repeat);
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        noteActivity = (ImageButton) findViewById(R.id.noteActivity);
        songIMG =(ImageView) findViewById(R.id.albumArtIMG);
        songTitle = (TextView) findViewById(R.id.songName_current_song_view);
        songArtist = (TextView) findViewById(R.id.artistName_current_song_view);
        updateRepeatColor();
        updateShuffleColor();

        Playback.getInstance().setActivityName("CurrentSongActivity");
        Playback.getInstance().setContext(CurrentSongActivity.this);
        // repeat button onClickListener
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playback.toggleLooping();
                updateRepeatColor();
            }
        });

        // shuffle button onClickListener
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playback.toggleShuffling();
                updateShuffleColor();
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

    //Update the color of the repeat button based on the looping boolean in the playback class
    private void updateRepeatColor()
    {
        if(playback.getLooping())
            repeat.setColorFilter(Color.parseColor("#6eb7a7"));
        else
            repeat.setColorFilter(Color.parseColor("#606060"));
    }

    //Update the color of the shuffle button based on the shuffling boolean in the playback class
    private void updateShuffleColor()
    {
        if(playback.getShuffling())
            shuffle.setColorFilter(Color.parseColor("#d19e4f"));
        else
            shuffle.setColorFilter(Color.parseColor("#606060"));
    }

    //Call to SQL db to find out if note has been created for the song.
    private String getNoteForSong()
    {
        //Needs a Song instance to check with, or at least a dummy Song instance with path
        String songPath = "";
        Song inSong = new Song("","",songPath, "", "", 0);

        //implement sql call for finding the note based on its path
        Note songNote = SQLdb.getNoteFromSong(inSong);
        //if it exists, return the contents of the note
        if(songNote!=null)
            return songNote.getContents();
        //else...
        else
            return "";
    }

    private void updateGUI()
    {
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
        songTitle.setText(playback.getSongName());
        songArtist.setText(playback.getSongArtist());
    }


}
