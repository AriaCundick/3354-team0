package team0.musicmakerproto;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class LibraryActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_library:
                    //mTextMessage.setText("Library");
                    return true;
                case R.id.nav_notes:
                    //mTextMessage.setText("Notes");
                    return true;
                case R.id.nav_settings:
                    //mTextMessage.setText("Settings");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		getPermission();
	}

	@TargetApi(Build.VERSION_CODES.M)
    private void getPermission()
    {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        else
        {
            //else permission is already granted.
            findSongsOnDevice();
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
                    findSongsOnDevice();
                }
                else
                    Toast.makeText(LibraryActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    
    //Change List<String> to List<Song>
	//Done - G
    private List<Song> findSongsOnDevice(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] songDataWanted = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,

                MediaStore.Audio.Media.DURATION
        };

        //Ascending order
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        List<Song> mp3Files = new ArrayList<Song>();

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
                        mp3Files.add(new Song(title, artist, path, displayName, songDuration));
						Log.i("TAG", "added song");
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
