package com.example.gm_hackathon;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.NumberPicker;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import java.util.Calendar;

//variable for selection intent


public class MyActivity extends Activity {

    //variable for selection intent
    private final int PICKER = 1;
    //variable to store the currently selected image
    private int currentPic = 0;
    //gallery object
    private Gallery picGallery;
    //image view for larger display
    private ImageView picView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.InitializeControls();
    }

    private void InitializeControls()
    {
        Button selectPhotoButton= (Button) findViewById(R.id.selectPictureButton);

        View.OnClickListener pickPhotoListener = new View.OnClickListener() {
            public void onClick(View v){
                PickAPhoto();
            }
        };

        selectPhotoButton.setOnClickListener(pickPhotoListener);

    }

    private void PickAPhoto()
    {


        //take the user to their chosen image selection app (gallery or file manager)
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        //we will handle the returned data in onActivityResult
        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);
    }
}

