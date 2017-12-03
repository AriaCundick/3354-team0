package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddSongsToPlaylistActivity extends AppCompatActivity {

    private Button btnAddSongs;
    private ListView songs;
    private Playlist newSongs;
    private EditText searchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_songs_to_playlist);

        newSongs = new Playlist();

        //Bind GUI elements.
        btnAddSongs = (Button) findViewById(R.id.btn_add_songs_existing_playlist);
        songs = (ListView) findViewById(R.id.listview_add_songs_existing_playlist);
        searchFilter = (EditText) findViewById(R.id.search_add_songs_existing_playlist);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //Get data from last activity
        Intent intent = getIntent();
        final Playlist p = (Playlist) intent.getParcelableExtra("selected_playlist");
        final Playlist allSongs = (Playlist) intent.getParcelableExtra("all_songs_playlist");

        final SongAdapter adapter = new SongAdapter(this, allSongs.getSongs());
        songs.setAdapter(adapter);

        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //Set Click Listener for selecting songs to create a new playlist with.
        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Add song to temp playlist
                Song s = allSongs.getSong(i);
                newSongs.addSong(s);

                //Remove the element from the listview
                adapter.remove(i);
            }
        });

        btnAddSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLUpdatePlaylist();
                Intent intent = new Intent(AddSongsToPlaylistActivity.this, EditPlaylistActivity.class);
                intent.putExtra("selected_playlist", mergePlaylists(p));
                intent.putExtra("all_songs_playlist", allSongs);
                startActivity(intent);
            }
        });


    }

    //Merge the existing playlist with the new songs.
    private Playlist mergePlaylists(Playlist oldSongs)
    {
        //For every song in the newSongs playlist, add it to the oldSongs playlist.
        for(Song s: newSongs.getSongs())
        {
            oldSongs.addSong(s);
        }

        return oldSongs;

    }

    private void SQLUpdatePlaylist()
    {

    }
}
