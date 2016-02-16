package com.michael.learning.flickerbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by BAROM on 2/14/2016.
 */
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImageViewHolder>{
    private List<Photo> mPhotosList;
    private Context mContext;
    private final String LOG_TAG = FlickrRecyclerViewAdapter.class.getSimpleName();
    //

    // Constructor
    public FlickrRecyclerViewAdapter(Context context, List<Photo> photosList) {
        this.mPhotosList = photosList;
        mContext = context;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse, null);
       FlickrImageViewHolder flickrImageViewHolder = new FlickrImageViewHolder(view);
        return flickrImageViewHolder;
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder flickrImageViewHolder, int i) {
        Photo photoItem = mPhotosList.get(i);
        Log.d(LOG_TAG, " Processing: " + photoItem.getmTitle() + "--> " + Integer.toString(i));
        Picasso.with(mContext).load(photoItem.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(flickrImageViewHolder.thumbnail);
        flickrImageViewHolder.title.setText(photoItem.getmTitle());
    }

    @Override
    public int getItemCount() {
        return (null != mPhotosList ? mPhotosList.size() : 0);
    }
}
