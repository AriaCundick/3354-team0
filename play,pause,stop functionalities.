package com.example.kake.androidmusicapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
MediaPlayer Song;
    int pause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view) {

        if (Song == null) {
            Song = MediaPlayer.create(this, R.raw.weeknd);
            Song.start();
                Toast.makeText(MainActivity.this, "Song Play", Toast.LENGTH_SHORT).show();
        }

        else if(!Song.isPlaying()){
            Song.seekTo(pause);
            Song.start();
            Toast.makeText(MainActivity.this, "Song Play", Toast.LENGTH_SHORT).show();
        }
    }
    public void pause(View view){
        if(Song!= null){
        Song.pause();
        pause = Song.getCurrentPosition();
            Toast.makeText(MainActivity.this, "Song Pause", Toast.LENGTH_SHORT).show();
        }

    }
    public void stop(View view){
        Song.stop();
        Song = null;
        Toast.makeText(MainActivity.this, "Song Stop", Toast.LENGTH_SHORT).show();

    }
}
