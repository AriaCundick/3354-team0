package team0.musicmakerproto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aria on 11/13/17.
 * Class: PlaylistAdapter
 * Description: Custom Adapter class to set elements of the gridView on the library activity using a customized layout.
 */

public class PlaylistAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Playlist> playlists;

    public PlaylistAdapter(Context mContext, ArrayList<Playlist> p) {
        this.mContext = mContext;
        this.playlists = p;
    }

    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final Playlist p = playlists.get(i);

        //Set convertView to the layout of the linearlayout_playlist.xml file
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_playlist, null);
        }

        //Bind the xml elements to variables to be edited soon.
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_playlist_name);

        //Set the actual text of the element to the playlist name.
        nameTextView.setText((p.getPlaylistName()));

        return convertView;
    }
}
