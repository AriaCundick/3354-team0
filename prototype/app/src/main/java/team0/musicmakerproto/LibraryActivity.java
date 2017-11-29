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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    private ImageButton PBarButton; // current song activity button
    private Button addButton;
    private ImageView songIMG;
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
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        GridView playlistCollection = (GridView) findViewById(R.id.PlaylistGrid);

        //Bind GUI objects
        songName = (TextView) findViewById(R.id.collection_song_name);
        addButton = (Button) findViewById(R.id.new_playlist_btn);
        PBarButton = (ImageButton) findViewById(R.id.PBarBackground);
        songIMG = (ImageView) findViewById(R.id.albumCover);

        //Set static attribute of Song class for later use in album art.
        Song.setResources(getResources());

        //Initialize playlist collection with hardcoded All Songs playlist.
        allSongs = new Playlist("All Songs");
        allPlaylists = new ArrayList<Playlist>();
		allSongs.readExistingPlaylist(getPermission());

		allPlaylists.add(allSongs); //Add the allSongs playlist to the playlist arraylist.

        //Press playlist element on the grid view.
        playlistCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LibraryActivity.this, PlaylistViewActivity.class);
                intent.putExtra("selected_playlist", allPlaylists.get(i));
                startActivity(intent);
            }
        });

        // open current song activity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LibraryActivity.this, AddPlaylistActivity.class);
                intent.putExtra("all_songs", allPlaylists.get(0));
                startActivity(intent);
            }
        });
        PBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadCurrentSongActivity = new Intent(LibraryActivity.this,CurrentSongActivity.class);
                startActivity(loadCurrentSongActivity);
            }
        });

        /* Load all playlists into the grid view. */
        playlistCollection.setAdapter(new PlaylistAdapter(this, allPlaylists));


	}

	@Override
	protected void onResume()
    {
        super.onResume();
        updatePlaybackBar();
    }

    /*
        Description: Updates the playback bar on the bottom of the screen.
     */
	private void updatePlaybackBar()
    {
        songName.setText(playback.getSongName());
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
    }

    /*
        Description: Gets permission from the user to query the device's external storage
        Returns ArrayList of all songs on the device.
     */
	@TargetApi(Build.VERSION_CODES.M)
    private ArrayList<Song> getPermission()
    {
        //Check if permission to access external storage is granted.
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Log.i("info", "findSongsOnDevice() called");
            return findSongsOnDevice();
        }
        else {
            Log.i("info", "findSongsOnDevice() NOT called");
            return new ArrayList<Song>();
        }

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

    
    //Query the external storage of the device to find all mp3 files and compile them into an ArrayList.
    private ArrayList<Song> findSongsOnDevice(){

        Log.i("songs", "started findSongs..()");
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
                        mp3Files.add(new Song(title, artist, path, displayName, songDuration, mp3Files.size() + 1));
                    }
                }
            }

        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }

        return mp3Files;

    }



}
