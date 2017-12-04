package team0.musicmakerproto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gerardo Rodriguez on 12/2/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    static final String ID_NOT_FOUND = "ERROR";

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "MusicManager.db";

    //SQL command for creating the Playlist table
    private static final String SQL_CREATE_PLAYLIST_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DBContract.PlaylistEntry.TABLE_NAME + " (" +
            DBContract.PlaylistEntry._ID + " INTEGER PRIMARY KEY," +
            DBContract.PlaylistEntry.COL_TITLE + " TEXT)";

    //SQL command for creating the Song table
    private static final String SQL_CREATE_SONG_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DBContract.SongEntry.TABLE_NAME + " (" +
            DBContract.SongEntry._ID + " INTEGER PRIMARY KEY," +
            DBContract.SongEntry.COL_TITLE + " TEXT," +
            DBContract.SongEntry.COL_PATH + " TEXT," +
            DBContract.SongEntry.COL_ARTIST + " TEXT," +
            DBContract.SongEntry.COL_DURATION + " TEXT)";

    //SQL command for creating the PlaylistSong table
    private static final String SQL_CREATE_PLAYLIST_SONG_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DBContract.PlaylistSongEntry.TABLE_NAME + " (" +
            DBContract.PlaylistSongEntry._ID + " INTEGER PRIMARY KEY," +
            DBContract.PlaylistSongEntry.COL_PLAYLIST_ID + " TEXT," +
            DBContract.PlaylistSongEntry.COL_SONG_ID + " TEXT)";

    //SQL command for creating the Note table
    private static final String SQL_CREATE_NOTE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DBContract.NoteEntry.TABLE_NAME + " (" +
            DBContract.NoteEntry._ID + " INTEGER PRIMARY KEY, " +
            DBContract.NoteEntry.COL_SONG_ID + " TEXT," +
            DBContract.NoteEntry.COL_SONG_PATH + " TEXT," +
            DBContract.NoteEntry.COL_NOTE_TITLE + " TEXT," +
            DBContract.NoteEntry.COL_NOTE_CONTENT + " TEXT)";

    //Partial SQL command for deleting tables
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS ";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PLAYLIST_ENTRIES);
        db.execSQL(SQL_CREATE_SONG_ENTRIES);
        db.execSQL(SQL_CREATE_PLAYLIST_SONG_ENTRIES);
        db.execSQL(SQL_CREATE_NOTE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE + DBContract.NoteEntry.TABLE_NAME);
        db.execSQL(SQL_DELETE + DBContract.PlaylistSongEntry.TABLE_NAME);
        db.execSQL(SQL_DELETE + DBContract.SongEntry.TABLE_NAME);
        db.execSQL(SQL_DELETE + DBContract.PlaylistEntry.TABLE_NAME);
        onCreate(db);
    }

    //Insert playlist to Playlist table
    public boolean insertPlaylist(Playlist inPlaylist){
        //Check if playlist already exists in Playlist table and exit if it does
        String playlistID = getPlaylistID(inPlaylist);
        if(!playlistID.equals(ID_NOT_FOUND))
            return true;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.PlaylistEntry.COL_TITLE, inPlaylist.getPlaylistName());
        long result = db.insert(DBContract.PlaylistEntry.TABLE_NAME, null, contentValues);

        //Database no longer needed
        db.close();

        if(result == -1)
            return false;
        else
            return true;
    }

    //Insert song into Song table
    public boolean insertSong(Song inSong){
        //Check if song already exists in Playlist table and exit if it does
        String songID = getSongID(inSong);
        if(!songID.equals(ID_NOT_FOUND))
            return true;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.SongEntry.COL_TITLE, inSong.getTitle());
        contentValues.put(DBContract.SongEntry.COL_PATH, inSong.getPath());
        contentValues.put(DBContract.SongEntry.COL_ARTIST, inSong.getArtist());
        contentValues.put(DBContract.SongEntry.COL_DURATION, inSong.getSongDuration());

        long result = db.insert(DBContract.SongEntry.TABLE_NAME, null, contentValues);

        //Database no longer needed
        db.close();

        if(result == -1)
            return false;
        else
            return true;
    }

    //Adds a song to a playlist by joining the songID with the playlistID in the PlaylistSong table
    public boolean addSongToPlaylist(Song inSong, Playlist inPlaylist){
        //Checks if song exists in Song table
        String songID = getSongID(inSong);
        if(songID.equals(ID_NOT_FOUND))
            return false;

        //Checks if playlist exists in Playlist table
        String playlistID = getPlaylistID(inPlaylist);
        if(playlistID.equals(ID_NOT_FOUND))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.PlaylistSongEntry.COL_PLAYLIST_ID, playlistID);
        contentValues.put(DBContract.PlaylistSongEntry.COL_SONG_ID, songID);

        long result = db.insert(DBContract.PlaylistSongEntry.TABLE_NAME, null, contentValues);

        //Database no longer needed
        db.close();

        if(result == -1)
            return false;
        else
            return true;
    }

    //Adds a note to a song by using the songID in the Note table
    public boolean addNoteToSong(Note inNote, Song inSong){
        if(!inNote.getPath().equals(inSong.getPath()))
            return false;

        String songID = getSongID(inSong);
        if(songID.equals(ID_NOT_FOUND))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.NoteEntry.COL_SONG_ID, songID);
        contentValues.put(DBContract.NoteEntry.COL_SONG_PATH, inNote.getPath());
        contentValues.put(DBContract.NoteEntry.COL_NOTE_TITLE, inNote.getName());
        contentValues.put(DBContract.NoteEntry.COL_NOTE_CONTENT, inNote.getContents());

        long result = db.insert(DBContract.NoteEntry.TABLE_NAME, null, contentValues);

        //Database no longer needed
        db.close();

        if(result == -1)
            return false;
        else
            return true;
    }

    //Finds the song ID in the table Song using the provided song's path
    public String getSongID(Song inSong){
        SQLiteDatabase db = this.getReadableDatabase();

        //Specifies what columns from the database will be returned
        String[] projection = {DBContract.SongEntry._ID};

        //Filter results WHERE "selection" = 'selectionArgs'
        String selection = DBContract.SongEntry.COL_PATH + " = ?";
        String[] selectionArgs = {inSong.getPath()};

        //How results will be sorted in the Cursor
        String sortOrder = DBContract.SongEntry.COL_TITLE + " DESC";

        Cursor cursor = db.query(
            DBContract.SongEntry.TABLE_NAME,    //Table to query
            projection,                         //Columns to return
            selection,                          //Columns for WHERE clause
            selectionArgs,                      //Values for WHERE clause
            null,                      //How to group rows
            null,                       //How to filter group rows
            sortOrder                           //Sort order
        );



        //Go through cursor to get songID, return error otherwise
        String songID = ID_NOT_FOUND;
        if(cursor.moveToNext())
            songID = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.SongEntry._ID));

        //Database no longer needed
        db.close();
        return songID;
    }

    //Finds the playlist ID in the table Playlist using the provided playlist's name
    public String getPlaylistID(Playlist inPlaylist){
        SQLiteDatabase db = this.getReadableDatabase();

        //Specifies what columns from the database will be returned
        String[] projection = {DBContract.PlaylistEntry._ID};

        //Filter results WHERE "selection" = 'selectionArgs'
        String selection = DBContract.PlaylistEntry.COL_TITLE + " = ?";
        String[] selectionArgs = {inPlaylist.getPlaylistName()};

        //How results will be sorted in the Cursor
        String sortOrder = DBContract.PlaylistEntry._ID + " DESC";

        Cursor cursor = db.query(
                DBContract.PlaylistEntry.TABLE_NAME,    //Table to query
                projection,                             //Columns to return
                selection,                              //Columns for WHERE clause
                selectionArgs,                          //Values for WHERE clause
                null,                          //How to group rows
                null,                           //How to filter group rows
                sortOrder                               //Sort order
        );

        //Go through cursor to get playlistID, return error otherwise
        String playlistID = ID_NOT_FOUND;
        if(cursor.moveToNext())
            playlistID = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.PlaylistEntry._ID));

        //Database no longer needed
        db.close();

        return playlistID;
    }

    //Returns Note object with data from Note table given the Song connected to the Note
    public Note getNoteFromSong(Song inSong){
        //Check if inSong exists in Song table, return if false
        String songID = getSongID(inSong);
        if(songID.equals(ID_NOT_FOUND))
            return null;

        SQLiteDatabase db = this.getReadableDatabase();

        //Specifies what columns from the database will be returned
        String[] projection = {
                DBContract.NoteEntry.COL_NOTE_TITLE,
                DBContract.NoteEntry.COL_NOTE_CONTENT,
                DBContract.NoteEntry.COL_SONG_PATH
        };

        //Filter results WHERE "selection" = 'selectionArgs'
        String selection = DBContract.NoteEntry.COL_SONG_ID + " = ?";
        String[] selectionArgs = {songID};

        //How results will be sorted in the Cursor
        String sortOrder = DBContract.NoteEntry._ID + " DESC";

        Cursor cursor = db.query(
                DBContract.NoteEntry.TABLE_NAME,            //Table to query
                projection,                                 //Columns to return
                selection,                                  //Columns for WHERE clause
                selectionArgs,                              //Values for WHERE clause
                null,                              //How to group rows
                null,                               //How to filter group rows
                sortOrder                                   //Sort order
        );

        //Add data to Note using data from cursor result
        String noteTitle = "";
        String noteContent = "";
        String notePath = "";

        //If note exists in Note table, create and return Note object
        if(cursor.moveToNext()){
            noteTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.NoteEntry.COL_NOTE_TITLE));
            noteContent = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.NoteEntry.COL_NOTE_CONTENT));
            notePath = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.NoteEntry.COL_SONG_PATH));
            return new Note(noteTitle, noteContent, notePath);
        }

        //Database no longer needed
        db.close();
        return null;
    }

    //Returns an ArrayList with all the notes in the Note table
    public ArrayList<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        //May cause problems with cursor.getColumnIndexOrThrow() since rawQuery, but maybe not
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.NoteEntry.TABLE_NAME, null);

        //Setup
        ArrayList<Note> notes = new ArrayList<Note>();
        String noteName = "";
        String noteContents = "";
        String notePath = "";

        //Creates a note for every entry in the Note table
        //Gets note name, note contents, and note path using query
        while(cursor.moveToNext()){
            noteName = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.NoteEntry.COL_NOTE_TITLE));
            noteContents = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.NoteEntry.COL_NOTE_CONTENT));
            notePath = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.NoteEntry.COL_SONG_PATH));
            notes.add(new Note(noteName, noteContents, notePath));
        }

        //Database no longer needed
        db.close();

        return notes;
    }

    //Returns ArrayList with all the playlists in the Playlist table along with their songs
    public ArrayList<Playlist> getPlaylists(){
        SQLiteDatabase db = this.getReadableDatabase();

        //May cause problems with cursor.getColumnIndexOrThrow() since rawQuery, but maybe not
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.PlaylistEntry.TABLE_NAME, null);

        //Setup
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        String id = "";
        String name = "";


        //Creates a playlist for every entry in the Playlist table
        //Gets playlist name from query, gets playlist songs using playlist ID from query and running getSongsInPlaylist()
        while(cursor.moveToNext()){
            id = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.PlaylistEntry._ID));
            name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.PlaylistEntry.COL_TITLE));
            playlists.add(new Playlist(name, getSongsInPlaylist(id, db)));
        }

        //Database no longer needed
        db.close();

        return playlists;
    }

    //Returns ArrayList of Song objects in a particular playlist using data from Song table
    private ArrayList<Song> getSongsInPlaylist(String playlistID, SQLiteDatabase db){
        //SQLiteDatabase db = this.getReadableDatabase();

        //Specifies what columns from the database will be returned
        String[] projection = {
                DBContract.SongEntry._ID,
                DBContract.SongEntry.COL_TITLE,
                DBContract.SongEntry.COL_PATH,
                DBContract.SongEntry.COL_ARTIST,
                DBContract.SongEntry.COL_DURATION
        };

        //Filter results WHERE "selection" = 'selectionArgs'
        String selection = DBContract.SongEntry._ID + " = ?";
        String[] selectionArgs = getSongIDsInPlaylist(playlistID, db);

        //Check if playlist has no songs, return null if true
        if(selectionArgs == null)
            return null;

        //How results will be sorted in the Cursor
        String sortOrder = DBContract.SongEntry.COL_TITLE + " DESC";

        Cursor cursor = db.query(
                DBContract.SongEntry.TABLE_NAME,            //Table to query
                projection,                                 //Columns to return
                selection,                                  //Columns for WHERE clause
                selectionArgs,                              //Values for WHERE clause
                null,                              //How to group rows
                null,                               //How to filter group rows
                sortOrder                                   //Sort order
        );

        //Add songs to songList using data from cursor results
        ArrayList<Song> songList = new ArrayList<Song>();
        int songID;
        String songTitle = "";
        String songPath = "";
        String songArtist = "";
        String songDuration = "";
        while(cursor.moveToNext()){
            songID = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.SongEntry._ID));
            songTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.SongEntry.COL_TITLE));
            songPath = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.SongEntry.COL_PATH));
            songArtist = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.SongEntry.COL_ARTIST));
            songDuration = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.SongEntry.COL_DURATION));
            songList.add(new Song(songTitle, songArtist, songPath, songTitle,songDuration, songID));
            Log.i("SQL", songTitle);
        }

        return songList;
    }

    //Returns songIDs of songs in a particular playlist
    private String[] getSongIDsInPlaylist(String playlistID, SQLiteDatabase db){
        //Specifies what columns from the database will be returned
        String[] projection = {DBContract.PlaylistSongEntry.COL_SONG_ID};

        //Filter results WHERE "selection" = 'selectionArgs'
        String selection = DBContract.PlaylistSongEntry.COL_PLAYLIST_ID + " = ?";
        String[] selectionArgs = {playlistID};

        //How results will be sorted in the Cursor
        String sortOrder = DBContract.PlaylistSongEntry._ID + " DESC";

        Cursor cursor = db.query(
                DBContract.PlaylistSongEntry.TABLE_NAME,    //Table to query
                projection,                                 //Columns to return
                selection,                                  //Columns for WHERE clause
                selectionArgs,                              //Values for WHERE clause
                null,                              //How to group rows
                null,                               //How to filter group rows
                sortOrder                                   //Sort order
        );


        //Check if no songs exist in playlist, return null if true
        int numSongsInPlaylist = cursor.getCount();
        if(numSongsInPlaylist <= 0)
            return null;

         //Add songIDs from cursor results
        String[] songIDs = new String[numSongsInPlaylist];
        int index = 0;
        while(cursor.moveToNext()){
            songIDs[index] = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.PlaylistSongEntry.COL_SONG_ID));
            index++;
        }

        //Database no longer needed
        //db.close();

        return songIDs;
    }

    public boolean deletePlaylist(Playlist inPlaylist){
        String playlistID = getPlaylistID(inPlaylist);
        if(playlistID.equals(ID_NOT_FOUND))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();

        //Delete all the links connecting songs to this playlist in PlaylistSong table
        String playlistSongSelection = DBContract.PlaylistSongEntry.COL_PLAYLIST_ID + " LIKE ?";
        String[] selectionArgs = {playlistID};
        db.delete(DBContract.PlaylistSongEntry.TABLE_NAME, playlistSongSelection, selectionArgs);

        //Delete actual playlist from Playlist table
        String playlistSelection = DBContract.PlaylistEntry._ID + " LIKE ?";
        db.delete(DBContract.PlaylistEntry.TABLE_NAME, playlistSelection, selectionArgs);

        //Database no longer needed
        db.close();

        return true;
    }

}
