package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PlaylistViewActivity extends AppCompatActivity {

    private ListView songs;
    final Playback playback = Playback.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);
        songs = (ListView) findViewById(R.id.songList);

        //Get playlist data from previous activity.
        Intent i = getIntent();
        final Playlist p = (Playlist) i.getParcelableExtra("selected_playlist");


        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playback.togglePlay(i, p, PlaylistViewActivity.this);
            }
        });
        //Load songs onto the list view from the playlist.
        songs.setAdapter(new SongAdapter(this, p.getSongs()));
    }

    public void playPauseBtnPressed(View view)
    {
        playback.togglePlay();
    }

    public void skipForwardBtnPressed(View view)
    {
        playback.skipForward();
    }

}
