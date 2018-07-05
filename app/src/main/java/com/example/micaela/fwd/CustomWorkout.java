/*This activity is where the Custom Workout is displayed. It is displayed as a list of selectable
exercises with names and thumbnails. At the bottom of the page there is a 'shuffle button' that can
re-generate your workout if you do not like the current one.
 */

package com.example.micaela.fwd;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class CustomWorkout extends AppCompatActivity {

    private ListView listExercises;
    private static final String TAG = "Custom Workout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Calling saved instance state ");
        super.onCreate(savedInstanceState);

        //Set the layout here
        Log.d(TAG, "onCreate: Setting up the Layout");
        setContentView(R.layout.activity_custom_workout_list); //Layout for this page
        listExercises = (ListView) findViewById(R.id.xmlListview);//Located in activity_custom_workout_list.xml

        Log.d(TAG, "onCreate: Retrieving the custom workout info from Bundle");
        //Get the information passed in the bundle as a String (originally a JSONArray in prev Activity)
        Intent intent  = getIntent();
        String jsonArrayWorkout = intent.getStringExtra("workout");

        Log.d(TAG, "onCreate: parsing the JSON for UI");
        //Parse JSON to feed into list and call the custom array adapter for listview to inflate and populate Layout
       ParseJSONForUI parseJSONForUI = new ParseJSONForUI(jsonArrayWorkout);

        Log.d(TAG, "onCreate: Calling the custom Workout Adapter");
        CustomWorkoutListAdapter customWorkoutListAdapter = new CustomWorkoutListAdapter(
                CustomWorkout.this, R.layout.list_record, parseJSONForUI.getExercises());
        listExercises.setAdapter(customWorkoutListAdapter);
    }



}
