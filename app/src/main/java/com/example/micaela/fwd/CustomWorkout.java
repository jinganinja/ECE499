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
import java.util.Random;

public class CustomWorkout extends AppCompatActivity {

    private ListView listExercises;
    private static final String TAG = "Custom Workout";
    private Button shuffleFab;
    private List<ExerciseListObject> exercises;
    String jsonObjectAllAndWorkout;
    String workoutGenerated;
    JSONArray allExercisesDescriptions = new JSONArray();
    JSONArray workoutGeneratedJSON = new JSONArray();
    JSONArray iterationExercises = new JSONArray();


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
                JSONObject workoutResults = new JSONObject();
                JSONArray workoutObject = new JSONArray();
                try {
                    int randomNum = 0;
                    Log.i(TAG, "the range of the loop: " + Integer.toString(exercises.size()));
                    for (int j = 0; j < iterationExercises.length(); j++) {
                        Log.i(TAG, "iteration exercises exercise number: " + Integer.toString(iterationExercises.length()) + ", name: " + iterationExercises.getJSONObject(j).getString("name"));
                    }
                    for (int i = 0; i < workoutGeneratedJSON.length(); i++) {
                        randomNum = generateRandomNumber(iterationExercises.length());
                        workoutObject.put(iterationExercises.getJSONObject(randomNum));
                        iterationExercises.remove(randomNum);
                    }
                    workoutResults.put("workoutDescriptions", workoutObject);
                    workoutResults.put("allWorkoutDescriptions", allExercisesDescriptions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Create new intent fro which to re-launch this activity
                Intent relaunchCustomWorkoutIntent = new Intent(CustomWorkout.this, CustomWorkout.class);
                //Put the data to be passed into the bundle...
                relaunchCustomWorkoutIntent.putExtra("workout", workoutResults.toString());
                //Launch new page
                startActivity(relaunchCustomWorkoutIntent);
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
            workoutGeneratedJSON = allDescriptionsAndWorkout.getJSONArray("workoutDescriptions");
            allExercisesDescriptions = allDescriptionsAndWorkout.getJSONArray("allWorkoutDescriptions");
            iterationExercises = allDescriptionsAndWorkout.getJSONArray("allWorkoutDescriptions");
            Log.i(TAG, "the number of exercise descriptions given: " + Integer.toString(allExercisesDescriptions.length()) + ", the number of exercises in workout: " + Integer.toString(workoutGeneratedJSON.length()));
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

    public int generateRandomNumber(int range) {
        Random rand = new Random();
        int randomNum = rand.nextInt(range);
        Log.i(TAG, "The random number and range: " + Integer.toString(randomNum) + ", " + Integer.toString(range));
        return randomNum;
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
