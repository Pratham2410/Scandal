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

public class CustomArrayAdapter extends ArrayAdapter<Pair<String, String>> {
    private Context context;
    private List<Pair<String, String>> objects;
    private int resource;
    private OnItemClickListener listener;

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<Pair<String, String>> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
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

        // Set text and background color for the TextViews
        Log.d("etowsley", currentItem.first);
        Log.d("etowsley", currentItem.second);
        String text1_item = currentItem.first;
        String text2_item = currentItem.second;
//        if (text1_item != null) {
//            text1.setText(text1_item);
//        }
//        else {
//            text1.setText("");
//        }
//        if (text2_item != null) {
//            text2.setText(text2_item);
//        }
//        else {
//            text2.setText("");
//        }
        text1.setText(currentItem.first);
        text2.setText(currentItem.second);

        // Set different background color for the first TextView
        if (position == 0) {
            try {
                text1.setTextColor(ContextCompat.getColor(context, R.color.teal_A400));
                text1.setTextSize(20);
                text2.setTextColor(ContextCompat.getColor(context, R.color.teal_A400));
                text2.setTextSize(20);
            }
            catch (NullPointerException e) {
                // Code to handle the exception
                System.out.println("Header is null!: " + e.getMessage());
            }
        } else {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
        Log.d("etowsley", "item was added");


        return itemView;
    }
}

