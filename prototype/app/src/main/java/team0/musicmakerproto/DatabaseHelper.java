package team0.musicmakerproto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gerardo Rodriguez on 12/2/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "MusicManager.db";

    //SQL command for creating the Playlist table
    private static final String SQL_CREATE_PLAYLIST_ENTRIES =
            "CREATE TABLE " + DBContract.PlaylistEntry.TABLE_NAME + " (" +
            DBContract.PlaylistEntry._ID + " INTEGER PRIMARY KEY," +
            DBContract.PlaylistEntry.COL_TITLE + " TEXT)";

    //SQL command for creating the Song table
    private static final String SQL_CREATE_SONG_ENTRIES =
            "CREATE TABLE " + DBContract.SongEntry.TABLE_NAME + " (" +
            DBContract.SongEntry._ID + " INTEGER PRIMARY KEY," +
            DBContract.SongEntry.COL_TITLE + " TEXT," +
            DBContract.SongEntry.COL_PATH + " TEXT," +
            DBContract.SongEntry.COL_ARTIST + " TEXT," +
            DBContract.SongEntry.COL_DURATION + " TEXT)";

    //SQL command for creating the PlaylistSong table
    private static final String SQL_CREATE_PLAYLIST_SONG_ENTRIES =
            "CREATE TABLE " + DBContract.PlaylistSongEntry.TABLE_NAME + " (" +
            DBContract.PlaylistSongEntry._ID + " INTEGER PRIMARY KEY," +
            DBContract.PlaylistSongEntry.COL_PLAYLIST_ID + " TEXT," +
            DBContract.PlaylistSongEntry.COL_SONG_ID + " TEXT)";

    //SQL command for creating the Note table
    private static final String SQL_CREATE_NOTE_ENTRIES =
            "CREATE TABLE " + DBContract.NoteEntry.TABLE_NAME + " (" +
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.PlaylistEntry.COL_TITLE, inPlaylist.getPlaylistName());

        long result = db.insert(DBContract.PlaylistEntry.TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //Insert song into Song table
    public boolean insertSong(Song inSong){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.SongEntry.COL_TITLE, inSong.getTitle());
        contentValues.put(DBContract.SongEntry.COL_PATH, inSong.getPath());
        contentValues.put(DBContract.SongEntry.COL_ARTIST, inSong.getArtist());
        contentValues.put(DBContract.SongEntry.COL_DURATION, inSong.getSongDuration());

        long result = db.insert(DBContract.SongEntry.TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //Adds a song to a playlist by joining the songID with the playlistID in the PlaylistSong table
    public boolean addSongToPlaylist(Song inSong, Playlist inPlaylist){
        String songID = getSongID(inSong);
        if(songID.equals("ERROR"))
            return false;

        String playlistID = getPlaylistID(inPlaylist);
        if(songID.equals("ERROR"))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.PlaylistSongEntry.COL_PLAYLIST_ID, playlistID);
        contentValues.put(DBContract.PlaylistSongEntry.COL_SONG_ID, songID);

        long result = db.insert(DBContract.PlaylistSongEntry.TABLE_NAME, null, contentValues);
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
        if(songID.equals("ERROR"))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.NoteEntry.COL_SONG_ID, songID);
        contentValues.put(DBContract.NoteEntry.COL_SONG_PATH, inNote.getPath());
        contentValues.put(DBContract.NoteEntry.COL_NOTE_TITLE, inNote.getName());
        contentValues.put(DBContract.NoteEntry.COL_NOTE_CONTENT, inNote.getContents());

        long result = db.insert(DBContract.NoteEntry.TABLE_NAME, null, contentValues);
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
        String songID = "ERROR";
        while(cursor.moveToNext()){
            songID = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.SongEntry._ID));
        }
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
        String playlistID = "ERROR";
        while(cursor.moveToNext()){
            playlistID = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.PlaylistEntry._ID));
        }
        return playlistID;
    }
}
