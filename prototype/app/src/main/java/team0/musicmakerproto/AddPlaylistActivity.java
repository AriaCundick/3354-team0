package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class AddPlaylistActivity extends AppCompatActivity {


    private ListView songs;
    private Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
        //Bind GUI elements programmatically
        songs = (ListView) findViewById(R.id.new_playlist_listView);
        btnDone = (Button) findViewById(R.id.btn_done_newPlaylist);

        //Get playlist data from previous activity.
        Intent i = getIntent();
        final Playlist p = (Playlist) i.getParcelableExtra("all_songs");

        //Create new playlist to keep track of selected songs.
        Playlist newPlaylist = new Playlist();
        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Set background of element to green for 'added'
                //Add song to temp playlist

            }
        });
        //Load songs onto the list view from the playlist.
        songs.setAdapter(new SongAdapter(this, p.getSongs()));

    }
}
