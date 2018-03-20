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

public class FavouriteActivity extends AppCompatActivity implements PhotoListener {

    private ListView listView;
    private ArrayList<Photo> photos = new ArrayList<>();
    private PhotoAdapter adapter;
    private SQLiteDatabase db;
    private Cursor cursor;

    private final String TAG = "FavouriteActivity";

    public static final String PHOTOS_INSTANCE = "Photos";
    public static DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();


        //Checks if there is a savedInstanceState and if it has a photos array
        if(savedInstanceState != null && savedInstanceState.getSerializable(PHOTOS_INSTANCE) != null){
            photos = (ArrayList<Photo>) savedInstanceState.getSerializable(PHOTOS_INSTANCE);
        }else {
            photos = new ArrayList<>();
            fillPhotos();
        }

        adapter = new PhotoAdapter(this, 0, photos);

        listView = findViewById(R.id.favouriteList);
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

        Log.i(TAG, "Created FavouriteActivity");
    }

    //Inflates the menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.lists, menu);
        Log.i(TAG, "Inflated menu");
        return true;
    }

    //Creates Intent object depending on the selected item in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.home_item:
                Log.i(TAG, "Going to MainActivity");
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.favourites_item:
                Log.i(TAG, "Going to FavouriteActivity");
                intent = new Intent(getApplicationContext(), FavouriteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillPhotos(){
        //Selects all favourites from the SQLite database, makes Photo objects and puts those in the photos array
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;
        cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_COLUMN));
                String camera = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CAMERA_COLUMN));
                String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.URL_COLUMN));

                Photo photo = new Photo(id, url, camera);
                Log.i(TAG, "Selected: " + photo.toString());
                photos.add(photo);
            }
        }finally {
            cursor.close();
        }
    }

    //Listens for completion of Async task
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
        Log.i(TAG, "Restored instance state");
    }
}
