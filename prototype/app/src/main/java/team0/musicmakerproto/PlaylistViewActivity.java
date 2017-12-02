package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlaylistViewActivity extends AppCompatActivity {

    private ListView songs;
    final Playback playback = Playback.getInstance();
    private TextView title;
    private ImageView songIMG;
    private Button btnEditPlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);

        //Bind GUI elements.
        songs = (ListView) findViewById(R.id.songList);
        title = (TextView) findViewById(R.id.playlist_view_song_name);
        songIMG = (ImageView) findViewById(R.id.albumCover);
        btnEditPlaylist = (Button) findViewById(R.id.btn_edit_playlist);

        //Get playlist data from previous activity.
        Intent i = getIntent();
        final Playlist p = (Playlist) i.getParcelableExtra("selected_playlist");
        final Playlist allSongs = (Playlist) i .getParcelableExtra("all_songs_playlist");

        for(Song s : p.getSongs())
            Log.i("playlist", s.getTitle());
        Log.i("playlist", "all songs have been shown");
        //Load songs onto the list view from the playlist.
        songs.setAdapter(new SongAdapter(this, p.getSongs()));
        updatePlaybackBar();
        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playback.togglePlay(i, p, PlaylistViewActivity.this);
                updatePlaybackBar();

            }
        });

        //set button functionality for the edit button. (open new activity to edit a playlist).
        btnEditPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistViewActivity.this, EditPlaylistActivity.class);
                intent.putExtra("selected_playlist", p);
                intent.putExtra("all_songs_playlist", allSongs);
                startActivity(intent);
            }
        });

        //Load songs onto the list view from the playlist.
       // songs.setAdapter(new SongAdapter(this, p.getSongs()));
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
