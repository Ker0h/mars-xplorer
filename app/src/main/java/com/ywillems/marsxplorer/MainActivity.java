package com.ywillems.marsxplorer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoListener {

    private ListView listView;
    private ArrayList<Photo> photos = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private PhotoAdapter adapter;
    private String camera = "";

    private final String TAG = "MainActivity";

    public static final String PHOTOS_INSTANCE = "Photos";
    public static DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks if there is a saved instance state and if it contains a photos array
        if(savedInstanceState != null && savedInstanceState.getSerializable(PHOTOS_INSTANCE) != null){
            photos = (ArrayList<Photo>) savedInstanceState.getSerializable(PHOTOS_INSTANCE);
        }else {
            photos = new ArrayList<>();
            fillPhotos();
        }

        databaseHelper = new DatabaseHelper(this);


        adapter = new PhotoAdapter(this, 0, photos);

        spinner = findViewById(R.id.spinner);
        String[] cameras = getResources().getStringArray(R.array.cameras);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cameras);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "Selected camera: " + (String) adapterView.getItemAtPosition(i));

                //If the selected index is greater then 0, camera string equals the selected camera
                if(adapterView.getSelectedItemPosition() > 0){
                    camera = "camera=" + adapterView.getItemAtPosition(i) + "&";
                }else{
                    camera = "";
                }
                photos.clear();
                fillPhotos();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = findViewById(R.id.photoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.ID_INSTANCE, photos.get(i).getId());
                intent.putExtra(DetailActivity.CAMERA_INSTANCE, photos.get(i).getCamera());
                intent.putExtra(DetailActivity.IMAGE_INSTANCE, photos.get(i).getUrl());
                startActivity(intent);
            }
        });
    }

    //Inflates menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.lists, menu);
        Log.i(TAG, "Inflated menu");
        return true;
    }

    //Creates intent depending on the selected menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.home_item:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Log.i(TAG, "Going to the MainActivity");
                return true;
            case R.id.favourites_item:
                intent = new Intent(getApplicationContext(), FavouriteActivity.class);
                startActivity(intent);
                Log.i(TAG, "Going to the FavouriteActivity");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Creates and executes an Async task to fetch data from the API
    private void fillPhotos(){
            String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&page=1&" + camera + "api_key=HRhyY0bcLpgMWPnmgpC4MTOmeuZjt9w02UFyartt";
            PhotoAsyncTask task = new PhotoAsyncTask(this);
            task.execute(url);
    }

    //Listens for completion of the Async task
    @Override
    public void onPhotoListener(Photo photo) {
        photos.add(photo);
        adapter.notifyDataSetChanged();
        Log.i(TAG, "Dataset changed");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PHOTOS_INSTANCE, photos);
        Log.i(TAG, "Saved instance state");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        photos = (ArrayList<Photo>) savedInstanceState.getSerializable(PHOTOS_INSTANCE);
        Log.i(TAG, "Saved instance restored");
    }
}
