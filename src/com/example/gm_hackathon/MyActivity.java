package com.example.gm_hackathon;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import java.util.Calendar;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;

    private static final String DEBUG_TAG = "MyActivity";

    private void LoadCalendarInfo()
    {

        // Specify the date range you want to search for recurring
        // event instances
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2011, 9, 23, 8, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2011, 10, 24, 8, 0);
        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        // The ID of the recurring event whose instances you are searching
        // for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {"207"};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        cur =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                selection,
                selectionArgs,
                null);

        while (cur.moveToNext()) {
            String title = null;
            long eventID = 0;
            long beginVal = 0;

            // Get the field values
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);

            // Do something with the values.
            Log.i(DEBUG_TAG, "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Log.i(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));
        }
    }
}
