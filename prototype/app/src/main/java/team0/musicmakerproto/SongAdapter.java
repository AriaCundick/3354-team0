package team0.musicmakerproto;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aria on 11/13/17.
 */

public class SongAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Song> songs;

    public SongAdapter(Context mContext, ArrayList<Song> s) {
        this.mContext = mContext;
        this.songs = s;
    }

    public void remove(int id)
    {
        songs.remove(id);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songs.size();
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

        final Song s = songs.get(i);

        //Set convertView to the layout of the linearlayout_song.xml file
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_song, null);
        }

        //Bind the xml elements to variables to be edited soon.
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_song_name);
        final TextView artistTextView = (TextView)convertView.findViewById(R.id.textview_artist_name);

        nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        nameTextView.setMaxLines(1);
        //Set the actual text of the element to the playlist name.
        nameTextView.setText(s.getTitle());
        artistTextView.setText(s.getArtist());

        return convertView;
    }
}
