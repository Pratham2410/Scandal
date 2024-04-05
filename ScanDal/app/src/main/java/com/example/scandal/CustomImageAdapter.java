package com.example.scandal;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
/**
 * CustomImageAdapter class extends ArrayAdapter to create a custom adapter for displaying Bitmap images in a ListView.
 */
public class CustomImageAdapter extends ArrayAdapter<Bitmap> {
    /**
     * Context reference for the adapter
     */
    private Context mContext;
    /**
     * List of Bitmap images to be displayed
     */
    private List<Bitmap> mImages;
    /**
     * Constructor for CustomImageAdapter.
     *
     * @param context The context of the activity or fragment.
     * @param images  The list of Bitmap images to be displayed.
     */
    public CustomImageAdapter(Context context, List<Bitmap> images) {
        super(context, 0, images);
        mContext = context;
        mImages = images;
    }
    /**
     * Overrides the getView method of ArrayAdapter to customize the appearance of each item in the ListView.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return The View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_image, parent, false);
        }

        ImageView imageView = listItem.findViewById(R.id.imageView);
        Bitmap currentImage = mImages.get(position);
        imageView.setImageBitmap(currentImage);

        return listItem;
    }
}

