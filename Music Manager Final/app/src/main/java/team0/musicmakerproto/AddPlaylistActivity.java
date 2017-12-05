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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddPlaylistActivity extends AppCompatActivity {

    private DatabaseHelper SQLdb;
    private ListView songs;
    private Button btnDone;
    private TextView playlistName;
    private EditText searchFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
        setTitle("Create new playlist");

        //Bind GUI elements programmatically
        songs = (ListView) findViewById(R.id.new_playlist_listView);
        btnDone = (Button) findViewById(R.id.btn_done_newPlaylist);
        playlistName = (TextView) findViewById(R.id.new_playlist_name);
        searchFilter = (EditText) findViewById(R.id.search_add_playlist_songs);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Playback.getInstance().setActivityName("AddPlaylistActivity");
        Playback.getInstance().setContext(AddPlaylistActivity.this);

        //Get playlist data from previous activity.
        Intent i = getIntent();
        final Playlist p = (Playlist) i.getParcelableExtra("all_songs");

        //Create new playlist to keep track of selected songs.
        final Playlist newPlaylist = new Playlist();

        //Add the song to the database then return to the library activity.
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                newPlaylist.setPlaylistName(playlistName.getText().toString().trim());
                SQLCreatePlaylist(newPlaylist);
            }
        });

        //Create song adapter that sets the listview to the playlist's songs.
        final SongAdapter adapter = new SongAdapter(this, p.getSongs());
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
                Song s = p.getSong(i);
                newPlaylist.addSong(s);

                //Remove the element from the listview
                adapter.remove(i);
            }
        });


    }

    //Creates a new playlist in the SQL database
    public boolean SQLCreatePlaylist(Playlist p)
    {
        //First check to see if name already exists
        String name = p.getPlaylistName();
        SQLdb = new DatabaseHelper(this);

        //if name already exists
        if(!SQLdb.getPlaylistID(p).equals(DatabaseHelper.ID_NOT_FOUND) || name.equals("All Songs"))
        {
            Toast.makeText(this, "Playlist name already exists", Toast.LENGTH_SHORT).show();
            SQLdb.close();
            return false;
        }
        else if(p.size() == 0)
        {
            Toast.makeText(this, "Playlist must have songs added to it", Toast.LENGTH_SHORT).show();
            SQLdb.close();
            return false;
        }
        //else, add the playlist to the SQL db
        else {
            SQLdb.insertPlaylist(p);
            for(Song s: p.getSongs())
                SQLdb.addSongToPlaylist(s, p);

            SQLdb.close();

            //and start the main activity again.
            Intent intent = new Intent(AddPlaylistActivity.this, LibraryActivity.class);
            startActivity(intent);
            return true;
        }
    }
}
