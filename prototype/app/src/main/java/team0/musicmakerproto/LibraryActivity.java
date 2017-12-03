package team0.musicmakerproto;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import java.util.ArrayList;


public class LibraryActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Playlist allSongs;
    private ArrayList<Playlist> allPlaylists;
    private TextView songName;
    private Playback playback = Playback.getInstance();
    private ImageButton pBarButton; // current song activity button
    private Button addButton;
    private ImageView songIMG;
    private EditText searchFilter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_library:
                    return true;
                case R.id.nav_notes:
                    Intent intent = new Intent(LibraryActivity.this, NotesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.nav_settings:
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        GridView playlistCollection = (GridView) findViewById(R.id.PlaylistGrid);

        //Bind GUI objects
        songName = (TextView) findViewById(R.id.collection_song_name);
        addButton = (Button) findViewById(R.id.new_playlist_btn);
        pBarButton = (ImageButton) findViewById(R.id.PBarBackground);
        songIMG = (ImageView) findViewById(R.id.notes_albumCover);
        searchFilter = (EditText) findViewById(R.id.search_library_songs);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //Set static attribute of Song class for later use in album art.
        Song.setResources(getResources());

        //Initialize playlist collection with hardcoded All Songs playlist.
        allSongs = new Playlist("All Songs");
        allPlaylists = new ArrayList<Playlist>();
		allSongs.readExistingPlaylist(getPermission());
        Playlist tplist = new Playlist();
        tplist.addSong(allSongs.getSong(0));

        /* Load the adapter for the playlist arraylist. */
        final PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, allPlaylists);
        playlistCollection.setAdapter(playlistAdapter);

		allPlaylists.add(allSongs); //Add the allSongs playlist to the playlist arraylist.
        allPlaylists.add(tplist);
        findPlaylistsOnDevice();

        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                playlistAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Press playlist element on the grid view.
        playlistCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LibraryActivity.this, PlaylistViewActivity.class);
                intent.putExtra("selected_playlist", allPlaylists.get(i));
                intent.putExtra("all_songs_playlist", allPlaylists.get(0));
                startActivity(intent);
            }
        });

        //Set the functionality for the add button. (Add new playlist).
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LibraryActivity.this, AddPlaylistActivity.class);
                intent.putExtra("all_songs", allPlaylists.get(0));
                startActivity(intent);
            }
        });

        //Set functionality for playlist bar button. (Open current song details)
        pBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadCurrentSongActivity = new Intent(LibraryActivity.this,CurrentSongActivity.class);
                startActivity(loadCurrentSongActivity);
            }
        });




	}

	//Calls the SQLite manager to find every playlist in the database,
    //and loads them into the allPlaylists object.
	private void findPlaylistsOnDevice()
    {
        //Call to SQLiteManager -> for every playlist found, read it into a new playlist object
        //append it to the allPlaylists ArrayList.
    }
	@Override
	protected void onResume()
    {
        super.onResume();
        updatePlaybackBar();
        updatePlaylists();

    }

    //update all playlists except for the all songs playlist
    private void updatePlaylists()
    {
        for(int i = 1; i < allPlaylists.size(); i++)
            allPlaylists.remove(i);

        findPlaylistsOnDevice(); //Changes might occur from other activities,
                                 //so have to make sure playlists are up to date.
    }

    //Description: Updates the playback bar on the bottom of the screen.
	public void updatePlaybackBar()
    {
        songName.setText(playback.getSongName());
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
        playback.setActivityName("LibraryActivity");
        playback.setContext(LibraryActivity.this);
    }

    //Description: Gets permission from the user to query the device's external storage
    //Returns ArrayList of all songs on the device.
	@TargetApi(Build.VERSION_CODES.M)
    private ArrayList<Song> getPermission()
    {
        //Check if permission to access external storage is granted.
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        //return the queried songs on the external storage of the device
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return findSongsOnDevice();
        else //If permission is not granted, return an empty playlist.
            return new ArrayList<Song>();
    }

    //method is invoked when user hits grant/deny on a permission pop-up.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) //switch case for request code
        {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Permission granted! query songs.
                    Toast.makeText(LibraryActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(LibraryActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
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

    
    //Query the external storage of the device to find all mp3 files and compile them into an ArrayList.
    private ArrayList<Song> findSongsOnDevice(){

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        //Create an array that consists of the fields desired for the queried data.
        String[] songDataWanted = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        //Sort in Ascending order based on the current languages alphabet/conventions.
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        ArrayList<Song> mp3Files = new ArrayList<Song>();

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, songDataWanted, selection, null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast() ){
                    //Song data queried specified in the songDataWanted array.
                    String title = cursor.getString(0).trim();
                    if(title.contains(".mp3"))
                        title = title.substring(0, title.indexOf(".mp3"));
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {
                        mp3Files.add(new Song(title, artist, path, displayName, songDuration, mp3Files.size()));
                    }
                }
            }

        } catch (Exception e) {
            Log.e("es_querying_error", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }

        return mp3Files;

    }

}
