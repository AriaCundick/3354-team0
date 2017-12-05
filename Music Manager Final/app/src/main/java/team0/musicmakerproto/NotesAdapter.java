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
 * Created by Aria on 12/2/17.
 */

public class NotesAdapter extends BaseAdapter implements Filterable {
    ArrayList<Note> searchableNotes;
    ArrayList<Note> allNotes;
    Context mContext;

    public NotesAdapter(Context mContext, ArrayList<Note> s) {
        this.mContext = mContext;
        this.searchableNotes = this.allNotes = s;
    }

    @Override
    public int getCount() {
        return searchableNotes.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final Note s = searchableNotes.get(i);
        if(s == null)
            return null;

        //Set convertView to the layout of the linearlayout_song.xml file
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_note, null);
        }

        //Bind the xml elements to variables to be edited soon.
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_note_name);

        //Set the actual text of the element to the note name.
        nameTextView.setText(s.getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.count > 0)
                    searchableNotes = (ArrayList<Note>) results.values;

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                searchableNotes = allNotes;
                FilterResults results = new FilterResults();
                ArrayList<Note> FilteredArrayNotes = new ArrayList<Note>();

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < searchableNotes.size(); i++) {
                    Note dataName = searchableNotes.get(i);

                    //Add note if the constraint string contains the title of the note.
                    if (dataName.getName().toLowerCase().contains(constraint.toString()))  {
                        FilteredArrayNotes.add(dataName);
                    }
                }

                results.count = FilteredArrayNotes.size();
                results.values = FilteredArrayNotes;
                return results;
            }
        };
        return filter;
    }
}
