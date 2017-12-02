package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

//EditPlaylistActivity class is responsible for presenting the GUI for editing a playlist in ways including adding songs,
//removing songs, or deleting the entire playlist.
public class EditPlaylistActivity extends AppCompatActivity {

    private ListView songs;
    private Button btnAdd, btnDelete, btnSave;
    private EditSongAdapter adapter;
    private String playlistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_playlist);

        //Bind GUI elements to variables
        btnAdd = (Button) findViewById(R.id.btn_edit_playlist_add);
        btnDelete = (Button) findViewById(R.id.btn_edit_playlist_delete);
        btnSave = (Button) findViewById(R.id.btn_edit_playlist_save);
        songs = (ListView) findViewById(R.id.listView_edit_playlist);

        //Get the playlist sent from last activity
        Intent i = getIntent();
        final Playlist p = (Playlist) i.getParcelableExtra("selected_playlist");
        final Playlist allSongs = (Playlist) i.getParcelableExtra("all_songs_playlist");
        playlistName = p.getPlaylistName();

        //Set the song adapter for the list view.
        adapter = new EditSongAdapter(this, p.getSongs());
        songs.setAdapter(adapter);

        //Set onClick functionality for add button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSongs(p, allSongs);
            }
        });

        //Set onClick functionality for save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewPlaylist(p, allSongs);
            }
        });

    }

    //Launch activity to add songs to the current playlist
    public void addSongs(Playlist p, Playlist allSongs)
    {
        Intent intent = new Intent(EditPlaylistActivity.this, AddSongsToPlaylistActivity.class);
        intent.putExtra("selected_playlist", p);
        intent.putExtra("all_songs_playlist", allSongs);
        startActivity(intent);
    }

    //Delete the playlist from the sqlite database.
    public void deletePlaylist(View v)
    {
        //Call to SQLManager class -> delete the current playlist by its name (p.getPlaylistName())

        //Go back to main activity.
        Intent intent = new Intent(EditPlaylistActivity.this, LibraryActivity.class);
        startActivity(intent);
    }

    //Update the current playlist in the SQLite database with the current elements in the list view
    public void saveNewPlaylist(Playlist p, Playlist allSongs)
    {
        //Call to SQLManager class -> update the current playlist by its name (p.getPlaylistName())
        //call adapter.getSongs() to get the most recently edited version of the playlist.

        Intent intent = new Intent(EditPlaylistActivity.this, AddSongsToPlaylistActivity.class);
        intent.putExtra("selected_playlist", p);
        intent.putExtra("all_songs_playlist", allSongs);
        startActivity(intent);
    }


}
