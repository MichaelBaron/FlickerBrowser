package com.michael.learning.flickerbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BAROM on 1/23/2016.
 */
public class GetFlickrJsonData extends GetRawData{
    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> mPhotos;
    private Uri mDestinationUri;

   // Constructor
    public GetFlickrJsonData(String searchCriteria, boolean matchAll){
        super(null);
        createAndUpdateUri(searchCriteria, matchAll);
        mPhotos = new ArrayList<Photo>();
    }

    public void execute() {
        super.setmRawURL(mDestinationUri.toString());
        DownloadJasonData downloadJasonData = new DownloadJasonData();
        Log.v(LOG_TAG, "Built URI = " + mDestinationUri.toString());
        downloadJasonData.execute(mDestinationUri.toString());

    }

    public boolean createAndUpdateUri(String searchCriteria, boolean matchAll){
        final String FLICKR_BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAGMODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String NOJSONCALLBACK_PARAM = "nojsoncallback";

        mDestinationUri = Uri.parse(FLICKR_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM,searchCriteria)
                .appendQueryParameter(TAGMODE_PARAM, matchAll ? "All" : "Any")
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(NOJSONCALLBACK_PARAM, "1")
                .build();

        return mDestinationUri != null;
    }

    public List<Photo> getMPhotos() {
        return mPhotos;
    }

    public void processResult(){
        if(getmDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error downloading raw file");
            return;
        }

    // Create constants to use to iterate through the JSON array
        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTO_URL = "m";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";
        final String FLICKR_LINK = "link";
        final String FLICKR_TAGS = "tags";

        try {

            // Parse JSON data
            // Get all the data from the superclass
            JSONObject jsonData = new JSONObject(getmData());
            // Get just the array of items from jsonData object
            JSONArray itemsArray = jsonData.getJSONArray(FLICKR_ITEMS);

            // Iterate through the array of items, getting each photo object and extracting the value for each field
            for (int i=0; i<itemsArray.length(); i++){
                JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                String title = jsonPhoto.getString(FLICKR_TITLE);
                String author = jsonPhoto.getString(FLICKR_AUTHOR);
                String author_id = jsonPhoto.getString(FLICKR_AUTHOR_ID);
                String link = jsonPhoto.getString((FLICKR_LINK));
                String tags = jsonPhoto.getString(FLICKR_TAGS);

            // Since the photo file is a JSON object, get the object then extract the URL
                JSONObject jasonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                String photoUrl = jasonMedia.getString(FLICKR_PHOTO_URL);

            // Create a new Java Photo object using all the information parsed and assigned so far
                Photo photoObject = new Photo(title, author, author_id,link,tags,photoUrl);
             // Ad the photo object to the list of photos
                this.mPhotos.add(photoObject);
            }

            // Log properties of each Photo to LogCat
            for(Photo singlePhoto: mPhotos) {
                Log.v(LOG_TAG, singlePhoto.toString());
            }

        } catch (JSONException jsone) {
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
        }

    }

    public class DownloadJasonData extends DownloadRawData {

        protected void onPostExecute(String webData){
            super.onPostExecute(webData);
            processResult();

        }
        protected String doInBackground(String... params) {
            String[] par = { mDestinationUri.toString()};
            return super.doInBackground(par);
        }
    }
}
