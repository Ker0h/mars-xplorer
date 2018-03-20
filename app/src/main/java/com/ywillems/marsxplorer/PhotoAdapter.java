package com.ywillems.marsxplorer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywillems on 13-3-2018.
 */

public class PhotoAdapter extends ArrayAdapter<Photo> {
    private List<Photo> photos = new ArrayList();
    private TextView itemIdTextView;
    private ImageView itemImage;
    private final String TAG = "PhotoAdapter";

    public PhotoAdapter(@NonNull Context context, int resource, @NonNull List photos) {
        super(context, resource, photos);
        this.photos = photos;
    }

    //Get amount of photos in array
    @Override
    public int getCount() {
        Log.i(TAG, "Amount of photos: " + photos.size());
        return photos.size();
    }

    //Inflate item for ListView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Photo photo = photos.get(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_photo_item, parent, false);

        itemIdTextView = convertView.findViewById(R.id.itemIdTextView);
        itemImage = convertView.findViewById(R.id.itemImage);

        itemIdTextView.setText("ImageID: " + photo.getId());
        Picasso.with(getContext()).load(photo.getUrl()).into(itemImage);

        Log.i(TAG, "Inflated photo item for ListView");
        return convertView;
    }
}
