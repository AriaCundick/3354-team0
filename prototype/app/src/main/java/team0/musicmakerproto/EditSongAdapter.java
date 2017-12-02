package team0.musicmakerproto;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aria on 12/1/17.
 */

public class EditSongAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Song> songs;

    public EditSongAdapter(Context mContext, ArrayList<Song> s) {
        this.mContext = mContext;
        this.songs = s;
    }

    //Remove a song from the playlist based on its position in the list view.
    public void removeItem(int position){
        Song s = songs.get(position);
        songs.remove(s);
        this.notifyDataSetChanged(); //Update the list view to reflect the changes made in the playlist.
    }

    public ArrayList<Song> getSongs()
    {
        return songs;
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
}
