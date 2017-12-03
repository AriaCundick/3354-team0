package team0.musicmakerproto;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private Playback playback = Playback.getInstance();
    private TextView title;
    private ImageView songIMG;
    private ListView notesListView;
    private ArrayList<Note> notes;
    private EditText searchFilter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_library:
                    Intent intent = new Intent(NotesActivity.this, LibraryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.nav_notes:

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
        setContentView(R.layout.activity_notes);

        //Bind GUI elements;
        title = (TextView) findViewById(R.id.notesview_song_name);
        songIMG = (ImageView) findViewById(R.id.notes_albumCover);
        notesListView = (ListView) findViewById(R.id.listView_notes_activity);
        searchFilter = (EditText) findViewById(R.id.searchBar_notes_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true); // Set navigationView Notes item to be marked.

        updatePlaybackBar();
        notes = new ArrayList<Note>();
        SQLGetNotes();

        final NotesAdapter notesAdapter = new NotesAdapter(this, notes);
        notesListView.setAdapter(notesAdapter);
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notesAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NotesActivity.this, IndividualNote.class);
                intent.putExtra("note_path", notes.get(i).getPath());
                intent.putExtra("note_name", notes.get(i).getName());
                intent.putExtra("note_contents", notes.get(i).getContents());
                intent.putExtra("caller", this.getClass().getSimpleName());

                startActivity(intent);
            }
        });

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

    private void SQLGetNotes()
    {
        //assign notes from the DB to the notes ArrayList
    }

    //Updates the GUI to show information regarding the current song being played.
    private void updatePlaybackBar()
    {
        title.setText(playback.getSongName());
        songIMG.setImageBitmap(playback.getSongIMG(getResources()));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updatePlaybackBar();
    }

}
