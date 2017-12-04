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

    DatabaseHelper SQLdb;
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

        //SQL database instantiation
        SQLdb = new DatabaseHelper(this);

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

    //Updates the GUI of the activity.
    private void updateGUI()
    {
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
        songTitle.setText(playback.getSongName());
        songArtist.setText(playback.getSongArtist());
        int currentSongLength = playback.getCurrentSong().getDuration();
        songTime.setText(milliToTime(currentSongLength));
        songScrubber.setMax(currentSongLength);
        songScrubber.setProgress(playback.getCurrentSong().getCurrentPosition());
    }

    // Scrubber to change song Position
    public void run() {
        CurrentSongActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (playback.getCurrentSong() != null) {
                    int currentPos = playback.getCurrentSong().getCurrentPosition() / 1000;
                    songScrubber.setProgress(currentPos);
                    currentTimeStamp.setText(String.valueOf(currentPos));
                }

                songScrubber.postDelayed(this, 1000);
            }
        }) ;
    }


    @Override
    public void onStopTrackingTouch(SeekBar songScrubber) {
        pHandler.removeCallbacks(updateTimer);
        int songDuration = getSongDuration();
        int currentPosition = progressTimerConversion(songScrubber.getProgress(), songDuration);

        playback.getCurrentSong().seekTo(currentPosition);
        updateProgress();
    }

    @Override
    public void onStartTrackingTouch(SeekBar songScrubber) {
        pHandler.removeCallbacks(updateTimer);
    }

    // Scrubber used to change song progress
    @Override
        public void onProgressChanged(SeekBar songScrubber, int progress, boolean fromUser) {
            if (playback.getCurrentSong() != null && fromUser) {
                playback.getCurrentSong().seekTo(progress * 1000);
            }

    }

    // Get a song's current duration
    public int getSongDuration() {
        int songDuration = playback.getCurrentSong().getDuration();

        return songDuration;
    }

    // Get a song's current position
    public int getCurrentPosition() {
        int  songPosition = playback.getCurrentSong().getCurrentPosition();

        return songPosition;
    }

    // Convert playback time for single song duration
    public String milliToTime(int songTime) {

        String secs = "";
        String finalTime = "";
        long originalTime = songTime;

        // conversion of milliseconds
        int minutes = (int)(originalTime % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int)((originalTime % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // add "0" to seconds if it is single digit
        if (seconds < 10) {
            secs = "0" + seconds;
        }
        else {
            secs = "" + seconds;
        }

        // append strings
        finalTime = minutes + ":" + secs;

        // return song duration
        return finalTime;
    }

    // Current song progress (current is current song duration, total is total song duration)
    public int getProgressPercentage(long current, long total) {
        Double percent = (double) 0;

        long currentSec = (int) (current / 1000);
        long totalSecs = (int) (total / 1000);

        // calculate percentage
        percent = (((double) currentSec / totalSecs));

        // return percentage
        return percent.intValue();

    }

    public int progressTimerConversion(int progress, int total) {
        int current = 0;
        total = (int) (total / 1000);
        current = (int) ((((double) progress / 100) * total));

        return current * 1000;
    }

    public void updateProgress() {
        pHandler.postDelayed(updateTimer, 100);
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            long totalDuration = getSongDuration();
            long currentDuration = getCurrentPosition();

            // display completed time
            currentTimeStamp.setText(milliToTime((int)currentDuration));

            // update progress bar
            int progress = (int)getProgressPercentage(currentDuration, totalDuration);
            songScrubber.setProgress(progress);

            pHandler.postDelayed(this, 100);
        }
    };
}
