package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class CurrentSongActivity extends AppCompatActivity implements OnSeekBarChangeListener {

    Playback playback = Playback.getInstance();
    private ImageButton repeat;
    private ImageButton shuffle;
    private ImageButton noteActivity;
    private ImageView songIMG;
    private TextView songTitle, songArtist, songTime, currentTimeStamp;
    private SeekBar songScrubber;
    private Handler pHandler = new Handler();

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
        songScrubber = (SeekBar) findViewById(R.id.scrubber);
        songScrubber.setOnSeekBarChangeListener(this);
        songTime = (TextView) findViewById(R.id.totalDuration);
        currentTimeStamp = (TextView) findViewById(R.id.timeStamp);
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

        // open individual note activity for song
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

        // song progress/scrubber
        //songScrubber.setOnSeekBarChangeListener(this);

        // update
        updateGUI();
        run();
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

    // Scrubber to change song Position
    public void run() {
        CurrentSongActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (playback.getCurrentSong() != null) {
                    int currentPos = playback.getCurrentSong().getCurrentPosition() / 1000;
                    songScrubber.setProgress(currentPos);
                }

                songScrubber.postDelayed(this, 1000);
            }
        }) ;
    }


    @Override
    public void onStopTrackingTouch(SeekBar songScrubber) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar songScrubber) {

    }

    // Scrubber used to change song progress
    @Override
        public void onProgressChanged(SeekBar songScrubber, int progress, boolean fromUser) {
            if (playback.getCurrentSong() != null && fromUser) {
                playback.getCurrentSong().seekTo(progress * 1000);
            }

    }

}
