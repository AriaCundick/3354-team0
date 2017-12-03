package team0.musicmakerproto;

import android.provider.BaseColumns;

/**
 * Created by Gerardo Rodriguez on 12/2/2017.
 */

final class DBContract{

    private DBContract(){}

    static class PlaylistEntry implements BaseColumns {
        static final String TABLE_NAME = "PLAYLIST_TABLE";
        static final String COL_TITLE = "PLAYLIST_TITLE";
    }
    static class SongEntry implements BaseColumns{
        static final String TABLE_NAME = "SONG_TABLE";
        static final String COL_TITLE = "SONG_TITLE";
        static final String COL_PATH = "SONG_PATH";
        static final String COL_ARTIST = "SONG_ARTIST";
        static final String COL_DURATION = "SONG_DURATION";
    }
    static class PlaylistSongEntry implements BaseColumns{
        static final String TABLE_NAME = "PLAYLIST_SONG_TABLE";
        static final String COL_PLAYLIST_ID = "PLAYLIST_ID";
        static final String COL_SONG_ID = "SONG_ID";
    }
    static class NoteEntry implements BaseColumns{
        static final String TABLE_NAME = "NOTE_TABLE";
        static final String COL_SONG_ID = "SONG_ID";
        static final String COL_NOTE = "NOTE";
    }
}
