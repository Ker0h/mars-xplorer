package com.ywillems.marsxplorer;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ywillems on 13-3-2018.
 */

public class PhotoAsyncTask extends AsyncTask<String, Void, String> {
    private PhotoListener listener = null;
    private final String TAG = "PhotoAsyncTask";

    public PhotoAsyncTask(PhotoListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {

        InputStream inputStream = null;
        int responsCode = -1;
        // The URL recieved through .execute()
        String personUrl = strings[0];
        // Defining the return
        String response = "";

        Log.i(TAG, "doInBackground - " + personUrl);
        try {
            //Create an URL object
            URL url = new URL(personUrl);
            //Open a connection on the URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            //Initialise HTTP connection
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");

            // Execute HTTP request
            httpConnection.connect();

            //Checking response
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                Log.i(TAG, "doInBackground response = " + response);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e(TAG, "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }
        return response;
    }

    /**
     * onPostExecute verwerkt het resultaat uit de doInBackground methode.
     *
     * @param response
     */
    protected void onPostExecute(String response) {

        Log.i(TAG, "onPostExecute " + response);

        // Check of er een response is
        if(response == null || response == "") {
            Log.e(TAG, "This response empty YEET!!!");
            return;
        }

        JSONObject jsonObject;
        try {
            // Top level json object
            jsonObject = new JSONObject(response);

            // Get all photos and start looping
            JSONArray photos = jsonObject.getJSONArray("photos");
            for(int idx = 0; idx < photos.length(); idx++) {
                // array level objects and get user
                JSONObject object = photos.getJSONObject(idx);

                //Get photo id
                String photoId = object.getString("id");

                // Get camera name
                JSONObject camera = object.getJSONObject("camera");
                String cameraName = camera.getString("name");

                //Get image source
                String imgUrl = object.getString("img_src");

                Log.i(TAG, "Got photo " + photoId + "from: " + imgUrl);


                // Create new Photo object
                Photo photo = new Photo();
                photo.setId(photoId);
                photo.setUrl(imgUrl);
                photo.setCamera(cameraName);

                //Callback with new photo data
                listener.onPhotoListener(photo);
            }
        } catch( JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
    }

    //Convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    // Call back interface
//    public interface OnRandomPhotoAvailable {
//        void onRandomPhotoAvailable(Photo photo);
//    }
}
