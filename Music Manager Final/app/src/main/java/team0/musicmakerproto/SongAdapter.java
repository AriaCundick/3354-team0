package team0.musicmakerproto;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aria on 11/13/17.
 */

public class SongAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    private ArrayList<Song> searchableSongs;
    private ArrayList<Song> allSongs;

    public SongAdapter()
    {
        this.mContext = null;
        this.searchableSongs = this.allSongs = new ArrayList<Song>();

    }

    public SongAdapter(Context mContext, ArrayList<Song> s) {
        this.mContext = mContext;
        this.searchableSongs = this.allSongs = s;
    }

    public void remove(int id)
    {
        searchableSongs.remove(id);
        allSongs.remove(id);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return searchableSongs.size();
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

        final Song s = searchableSongs.get(i);

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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.count > 0)
                    searchableSongs = (ArrayList<Song>) results.values;

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                searchableSongs = allSongs;
                FilterResults results = new FilterResults();
                ArrayList<Song> FilteredArraySongs = new ArrayList<Song>();

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < searchableSongs.size(); i++) {
                    Song dataName = searchableSongs.get(i);

                    //Add song if the constraint string contains the title or artist of the song.
                    if (dataName.getTitle().toLowerCase().contains(constraint.toString())
                            || dataName.getArtist().toLowerCase().contains(constraint.toString()))  {
                        FilteredArraySongs.add(dataName);
                    }
                }

                results.count = FilteredArraySongs.size();
                results.values = FilteredArraySongs;
                //Log.e("VALUES", results.values.toString());

                return results;
            }
        };
        return filter;
    }
}
