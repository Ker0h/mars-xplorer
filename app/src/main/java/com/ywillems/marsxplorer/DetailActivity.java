package com.ywillems.marsxplorer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView cameraNameTextView;
    private ImageView detailImage;
    private ImageButton favouriteButton;
    private SQLiteDatabase db;

    private final String TAG = "DetailActivity";

    public static final String ID_INSTANCE = "Id";
    public static final String CAMERA_INSTANCE = "Camera";
    public static final String IMAGE_INSTANCE = "Image";

    private Intent intent;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Gets intent for the sake of saving the imahe url in onSavedInstance
        intent = getIntent();
        imageUrl = intent.getStringExtra(IMAGE_INSTANCE);

        cameraNameTextView = findViewById(R.id.detailCameraTextView);
        detailImage = findViewById(R.id.detailImage);
        favouriteButton = findViewById(R.id.favouriteButton);

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = MainActivity.databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.ID_COLUMN, intent.getStringExtra(ID_INSTANCE));
                values.put(DatabaseHelper.CAMERA_COLUMN, intent.getStringExtra(CAMERA_INSTANCE));
                values.put(DatabaseHelper.URL_COLUMN, intent.getStringExtra(IMAGE_INSTANCE));

                db.insert(DatabaseHelper.TABLE_NAME, null, values);
                Log.i(TAG, "Inserted: " + values.toString());
            }
        });

        if(savedInstanceState != null) {
            cameraNameTextView.setText(savedInstanceState.getString(CAMERA_INSTANCE));
            Picasso.with(this).load(savedInstanceState.getString(IMAGE_INSTANCE)).into(detailImage);
        }else {
            cameraNameTextView.setText(intent.getStringExtra(CAMERA_INSTANCE));
            Picasso.with(this).load(intent.getStringExtra(IMAGE_INSTANCE)).into(detailImage);
        }

        Log.i(TAG, "Created DetailActivity");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CAMERA_INSTANCE, cameraNameTextView.getText().toString());
        outState.putString(IMAGE_INSTANCE, imageUrl);
        Log.i(TAG, "Saved instance state");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cameraNameTextView.setText(savedInstanceState.getString(CAMERA_INSTANCE));
        Picasso.with(this).load(savedInstanceState.getString(IMAGE_INSTANCE)).fit().centerCrop().into(detailImage);
        Log.i(TAG, "Restored instance state");
    }
}
