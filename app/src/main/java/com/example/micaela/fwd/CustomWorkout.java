/*This activity is where the Custom Workout is displayed. It is displayed as a list of selectable
exercises with names and thumbnails. At the bottom of the page there is a 'shuffle button' that can
re-generate your workout if you do not like the current one.
 */

package com.example.micaela.fwd;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class CustomWorkout extends AppCompatActivity {

    private ListView listExercises;
    private static final String TAG = "Custom Workout";
    private Button shuffleFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Calling saved instance state ");
        super.onCreate(savedInstanceState);

        //Set the layout here
        Log.d(TAG, "onCreate: Setting up the Layout");
        setContentView(R.layout.activity_custom_workout_list); //Layout for this page
        listExercises = (ListView) findViewById(R.id.xmlListview);//Located in activity_custom_workout_list.xml

        //Create floating action button to 'shuffle' workout results
        shuffleFab = (Button) findViewById(R.id.shuffle_fab);
        shuffleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDO: if button clicked, then need to call shuffle function
                //Need to ensure that shuffle function retrieves new workout and new 'leftover' exercises arrays
                //Need to add both of these items to the bundle, and modify the bundle before calling this activity to restart
//                Intent shuffleWorkoutIntent = new Intent(CustomWorkout.this, CustomWorkout.class);
//                shuffleWorkoutIntent.putExtra(....);
//                startActivity(shuffleWorkoutIntent);
            }
        });


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
