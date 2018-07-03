package com.example.micaela.fwd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class CustomWorkout extends AppCompatActivity {

    private TextView mDisplayText;
    private static final String TAG = "Custom Workout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_workout);

        //Get the information passed in the bundle
        Intent intent  = getIntent();
        String jsonArrayWorkout = intent.getStringExtra("workout");

        //Turn the string back into JSON Array
        try {
            JSONArray workout = new JSONArray(jsonArrayWorkout);
            Log.d(TAG, "onCreate: "+ workout.toString());

        } catch (JSONException e) {
            Log.d(TAG, "onCreate: Error retrieving the JSON array of generated workout!");
        }

        //Showing an example of content passed into page no. 2
        mDisplayText = (TextView) findViewById(R.id.exerciseTitle);
    }
}
