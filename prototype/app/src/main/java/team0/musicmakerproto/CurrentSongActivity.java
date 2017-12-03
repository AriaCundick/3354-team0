package team0.musicmakerproto;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.graphics.Color;
import android.widget.ImageView;

public class CurrentSongActivity extends AppCompatActivity {

    Playback playback = Playback.getInstance();
    ImageButton repeat;
    ImageButton shuffle;
    ImageButton noteActivity;
    ImageView songIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        repeat = (ImageButton) findViewById(R.id.repeat);
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        noteActivity = (ImageButton) findViewById(R.id.noteActivity);
        songIMG =(ImageView) findViewById(R.id.albumArtIMG);


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
                startActivity(loadNote);
            }
        });

        updateGUI();
    }

    private void updateGUI()
    {
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
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
