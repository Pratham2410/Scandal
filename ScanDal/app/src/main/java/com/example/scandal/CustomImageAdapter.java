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

public class CustomImageAdapter extends ArrayAdapter<Bitmap> {
    private Context mContext;
    private List<Bitmap> mImages;

    public CustomImageAdapter(Context context, List<Bitmap> images) {
        super(context, 0, images);
        mContext = context;
        mImages = images;
    }

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

