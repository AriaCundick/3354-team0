package team0.musicmakerproto;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aria on 12/1/17.
 */

public class EditSongAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    private  ArrayList<Song> searchableSongs;
    private  ArrayList<Song> allSongs;

    public EditSongAdapter(Context mContext, ArrayList<Song> s) {
        this.mContext = mContext;
        this.searchableSongs = allSongs = s;
    }

    //Remove a song from the playlist based on its position in the list view.
    public void removeItem(int position){
        Song s = searchableSongs.get(position);
        searchableSongs.remove(s);
        allSongs.remove(s);
        this.notifyDataSetChanged(); //Update the list view to reflect the changes made in the playlist.
    }

    public ArrayList<Song> getSearchableSongs()
    {
        return searchableSongs;
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
            convertView = layoutInflater.inflate(R.layout.linearlayout_edit_playlist, null);
        }

        //Bind the xml elements to variables to be edited soon.
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_song_name);
        final TextView artistTextView = (TextView)convertView.findViewById(R.id.textview_artist_name);
        final ImageButton deleteBtn = (ImageButton)convertView.findViewById(R.id.imageButton_delete_song);

        nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        nameTextView.setMaxLines(1);
        artistTextView.setEllipsize(TextUtils.TruncateAt.END);
        artistTextView.setMaxLines(1);

        //Set the actual text of the element to the playlist name.
        nameTextView.setText(s.getTitle());
        artistTextView.setText(s.getArtist());
        final int index = i;
        //Set on click listener for the delete button
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               removeItem(index);
            }
        });


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

                // perform your search here using the searchConstraint String.

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
