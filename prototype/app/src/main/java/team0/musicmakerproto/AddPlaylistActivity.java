package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddPlaylistActivity extends AppCompatActivity {


    private ListView songs;
    private Button btnDone;
    private TextView playlistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
        //Bind GUI elements programmatically
        songs = (ListView) findViewById(R.id.new_playlist_listView);
        btnDone = (Button) findViewById(R.id.btn_done_newPlaylist);
        playlistName = (TextView) findViewById(R.id.new_playlist_name);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

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
                SQLCreatePlaylist(newPlaylist);
            }
        });

        //Set Click Listener for selecting songs to create a new playlist with.
        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Set background of element to green for 'added'
                view.setBackgroundColor(getResources().getColor(R.color.green));

                //Add song to temp playlist
                Song s = p.getSong(i);
                newPlaylist.addSong(s);
            }
        });

        //Load songs onto the list view from the playlist.
        songs.setAdapter(new SongAdapter(this, p.getSongs()));

    }

    //Creates a new playlist in the SQL database
    private void SQLCreatePlaylist(Playlist p)
    {
        //First check to see if name already exists
        String name = playlistName.getText().toString().trim();

        //insert code to query the db for the name

        //if name already exists, Toast with message saying "Playlist name already exists"

        //else, add the playlist to the SQL db
        //and run the below code
        //Intent intent = new Intent(AddPlaylistActivity.this, LibraryActivity.class);
        //startActivity(intent);

    }
}
