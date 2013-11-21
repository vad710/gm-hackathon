package com.example.gm_hackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;



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

        Button sendToCarButton = (Button) findViewById(R.id.sendToCarButton);
        View.OnClickListener sendToCarListener = new View.OnClickListener(){
            public void onClick(View v){
                try {
                    SendToCar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        sendToCarButton.setOnClickListener(sendToCarListener);

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

    private void SendToCar() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet("http://google.com"));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String responseString = out.toString();
            //..more logic
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }
}

