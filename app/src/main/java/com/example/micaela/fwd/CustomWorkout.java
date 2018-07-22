/*This activity is where the Custom Workout is displayed. It is displayed as a list of selectable
exercises with names and thumbnails. At the bottom of the page there is a 'shuffle button' that can
re-generate your workout if you do not like the current one.
 */

package com.example.micaela.fwd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CustomWorkout extends AppCompatActivity {

    private ListView listExercises;
    private static final String TAG = "Custom Workout";
    private Button shuffleFab;
    private List<ExerciseListObject> exercises;
    String jsonObjectAllAndWorkout;
    String workoutGenerated;
    JSONArray allExercisesDescriptions = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Calling saved instance state ");
        super.onCreate(savedInstanceState);

        //Set the layout here
        Log.d(TAG, "onCreate: Setting up the Layout");
        setContentView(R.layout.activity_custom_workout_list); //Layout for this page
        listExercises = (ListView) findViewById(R.id.xmlListview);//Located in activity_custom_workout_list.xml

        //Getting the menu!
      //  onCreateOptionsMenu(R.menu.menu);

        //Set the custom Toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView pageTitle = (TextView) findViewById(R.id.toolbar_title);
        pageTitle.setText("Your Custom Workout");

        //Create floating action button to 'shuffle' workout results
        shuffleFab = (Button) findViewById(R.id.shuffle_fab);
        shuffleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomWorkout.this, "You clicked Regenerate!", Toast.LENGTH_SHORT).show();
                //ToDO: if button clicked, then need to call shuffle function. This is where you call backend.
                /*Call the shuffle button. Shuffle button is going to return new workout and leftover exercises (in case some
                 * one pushes shuffle a second time to be cruel.) Then, this page needs to be relaunched. Before being relaunched
                 * the workout and the leftover exercises need to be added to the bundle. There is an example of two things
                 * being added to the bundle below in this code when someone clicks a list item. Commented code and "To - dos" below aims to
                 * outline procedure */
                //ToDo: Backend code passes a JSON array that contains workout and leftover exercises in JSON array. Array is parsed
                //todo:...and separated afterwards when this page launches.
                //todo:...so, just need to create intent here and pass in it the JSON array like we did in main activity!
                //todo:...assume that output of backend returns JSON array called 'workoutResults'
                //Create new intent fro which to re-launch this activity
                //Intent relaunchCustomWorkoutIntent = new Intent(CustomWorkout.this, CustomWorkout.class);
                //Put the data to be passed into the bundle...
                //relaunchCustomWorkoutIntent.putExtra("workout", workoutResults.toString());
                //Launch new page
                //startActivity(relaunchCustomWorkoutIntent);
                //BAM!
            }
        });

        Log.d(TAG, "onCreate: Retrieving the custom workout info from Bundle");
        //Get the information passed in the bundle as a String (originally a JSONArray in prev Activity)
        Intent intent = getIntent();
        jsonObjectAllAndWorkout = intent.getStringExtra("workout");
        Log.i(TAG, "The output sent to the workout page: " + jsonObjectAllAndWorkout);
        try {
            JSONObject allDescriptionsAndWorkout = new JSONObject(jsonObjectAllAndWorkout);
            workoutGenerated = allDescriptionsAndWorkout.get("workoutDescriptions").toString();
            allExercisesDescriptions = allDescriptionsAndWorkout.getJSONArray("allWorkoutDescriptions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onCreate: parsing the JSON for UI");
        //Parse JSON to feed into list and call the custom array adapter for listview to inflate and populate Layout
        Log.i(TAG, "The generated workout: "+workoutGenerated);
        ParseJSONForUI parseJSONForUI = new ParseJSONForUI(workoutGenerated);

        Log.d(TAG, "onCreate: Calling the custom Workout Adapter");
        exercises = parseJSONForUI.getExercises();
        CustomWorkoutListAdapter customWorkoutListAdapter = new CustomWorkoutListAdapter(
                CustomWorkout.this, R.layout.list_record, exercises);
        listExercises.setAdapter(customWorkoutListAdapter);
        //Create onClick listener
        listExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CustomWorkout.this, exercises.get(position).getName(), Toast.LENGTH_SHORT).show();
                //set to context to go in the intent call
                Context context = CustomWorkout.this;
                // Store the destination activity in a class to go in the intent call
                Class destinationActivity = ExerciseTutorial.class;
                // Create the intent that will be used to start Exercise Tutorial
                Intent startExerciseTutorialIntent = new Intent(context, destinationActivity);
                //Add the exercise of interest to the Bundle to pass on to the next activity -- pass in the form of string that we got
                Bundle extras = new Bundle();
                extras.putString("workout", workoutGenerated);
                extras.putInt("index", position);
                startExerciseTutorialIntent.putExtras(extras);
                Log.d(TAG, "onClick: Starting Exercise Tutorial Activity....");
                startActivity(startExerciseTutorialIntent);
            }
        });
    }
    //Add the menu to the top of the screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Add functionality when button item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuNewWrkout:
                //Restart the main activity fresh
                Intent startNewWorkout = new Intent(this, MainActivity.class);
                startNewWorkout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startNewWorkout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startNewWorkout);
                return true;
            //Could add other options here
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
