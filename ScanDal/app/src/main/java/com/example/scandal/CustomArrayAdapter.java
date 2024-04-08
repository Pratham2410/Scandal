package com.example.scandal;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * CustomArrayAdapter is a custom ArrayAdapter implementation to display pairs of strings in a ListView.
 */
public class CustomArrayAdapter extends ArrayAdapter<Pair<String, String>> {
    private Context context;
    private List<Pair<String, String>> objects;
    private int resource;
    private OnItemClickListener listener;

    /**
     * Interface definition for a callback to be invoked when an item in this ArrayAdapter has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this ArrayAdapter has been clicked.
         *
         * @param position The position of the item that was clicked.
         */
        void onItemClick(int position);
    }

    /**
     * Constructor for the CustomArrayAdapter.
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<Pair<String, String>> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }

    /**
     * Sets the listener for item clicks.
     *
     * @param listener The listener to set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        // Get the data for the current position
        Pair<String, String> currentItem = objects.get(position);

        // Get the TextViews from the layout
        TextView text1 = itemView.findViewById(R.id.list_view_item1);
        TextView text2 = itemView.findViewById(R.id.list_view_item2);

        // Set text for the TextViews
        text1.setText(currentItem.first);
        text2.setText(currentItem.second);

        // Set different background color for the first TextView
        if (position == 0) {
            try {
                text1.setTextColor(ContextCompat.getColor(context, R.color.teal_A400));
                text1.setTextSize(20);
                text2.setTextColor(ContextCompat.getColor(context, R.color.teal_A400));
                text2.setTextSize(20);
            } catch (NullPointerException e) {
                Log.e("CustomArrayAdapter", "Header is null!", e);
            }
        } else {
            // Set click listener for non-header items
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        return itemView;
    }
}
