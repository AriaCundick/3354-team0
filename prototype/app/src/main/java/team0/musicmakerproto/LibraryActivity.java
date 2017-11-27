package team0.musicmakerproto;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import java.util.ArrayList;

//NEED TO IMPLEMENT
//SERVICE FOR MUSIC
//FRAGMENT FOR NAVIGATION VIEW
//DATABASE FOR NOTES/SONG/PLAYLIST MGMT
//TESTING FOR SIMPLE STUFF

public class LibraryActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Playlist allSongs;
    private GridView playlistCollection;
    private ArrayList<Playlist> allPlaylists;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment = null;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_library:
                    fragment = new FragmentPlaylistView();
                    /* Load all playlists into the grid view. */
                    playlistCollection.setAdapter(new PlaylistAdapter(LibraryActivity.this, allPlaylists));
                    return true;
                case R.id.nav_notes:
                    fragment = new FragmentTest1();
                    fragChecker();
                    return true;
                case R.id.nav_settings:
                    //mTextMessage.setText("Settings");
                    return true;
            }

            return false;
        }

        public void switchToPlaylistFrag() {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentView, new FragmentPlaylistView()).commit();
        }


    };

    // current song activity button
    ImageButton PBarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        playlistCollection = (GridView) findViewById(R.id.PlaylistGridFragment);

        //Hardcode the All Songs playlist that queries the device everytime the activity is created
        //This allows the app to find new songs that have been recently added to the device.
        allSongs = new Playlist("All Songs");
        allPlaylists = new ArrayList<Playlist>();
		allSongs.readExistingPlaylist(getPermission());
		allPlaylists.add(allSongs); //Add the allSongs playlist to the playlist arraylist.
        allPlaylists.add(allSongs);
        allPlaylists.add(allSongs);allPlaylists.add(allSongs);
        allPlaylists.add(allSongs);
        allPlaylists.add(allSongs);
        allPlaylists.add(allSongs);
        allPlaylists.add(allSongs);
        allPlaylists.add(allSongs);
        allPlaylists.add(allSongs);


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
        PBarButton = (ImageButton) findViewById(R.id.PBarBackground);
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

	@TargetApi(Build.VERSION_CODES.M)
    private ArrayList<Song> getPermission()
    {
        //Check if permission to access external storage is granted.
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return findSongsOnDevice();
        else
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

    
    //Query the external storage of the device to find all mp3 files and compile them into an ArrayList.
    private ArrayList<Song> findSongsOnDevice(){

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        //Create an array that consists of the fields desired for the queried data.
        String[] songDataWanted = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
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
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {

                        //mp3Files.add(path);
                        mp3Files.add(new Song(title, artist, path, displayName, songDuration, mp3Files.size() + 1));
						String id = String.valueOf(mp3Files.size());
                        Log.i("TAG", id);
                    }
                }
            }

            //print to see list of paths of the mp3 files in the Logcat
			//Changed so that it uses Song instead of String - G
            for( Song file : mp3Files) {
                Log.i("TAG", file.getPath());
            }
            if(mp3Files.isEmpty())
                Log.i("TAG", "mp3Files list is empty");

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
