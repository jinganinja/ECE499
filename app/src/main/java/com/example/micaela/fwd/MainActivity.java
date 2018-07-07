package com.example.micaela.fwd;

import com.example.micaela.fwd.CustomWorkout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Declare the generate workout button
    private Button mGenerateWorkout;

    //Declare a temporary button that will take me to other pages for dev purposes
    //private Button tempButton;
    //Declare all the dropDown menus
    private Spinner spinnerWorkoutDuration, spinnerEquipment, spinnerCardioVsStrength,
            spinnerTargetedMuscles, spinnerFitnessGoal;
    //Create a JSON instance to hold the user-input
    JSONObject userInput = new JSONObject();
    //Create a JSON instance to hold the generated workout info
    JSONArray workoutResults = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //put icon into toolbar -- GOAL: move this into a theme
        //getSupportActionBar().setLogo(R.drawable.push_up_fig_1);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        final String TAG = "MainActivity";
        mGenerateWorkout = (Button) findViewById(R.id.generateWorkoutButton);
        //tempButton = (Button)findViewById(R.id.button);

        //Initialize Spinner objects and link them to their xml id
        spinnerWorkoutDuration = (Spinner) findViewById(R.id.spinnerTimeDuration);
        spinnerEquipment = (Spinner) findViewById(R.id.spinnerEquipment);
        spinnerCardioVsStrength = (Spinner) findViewById(R.id.spinnerCardioVsStrength);
        spinnerTargetedMuscles = (Spinner) findViewById(R.id.spinnerTargetedMuscles);
        spinnerFitnessGoal = (Spinner) findViewById(R.id.spinnerFitnessGoal);

        //For each spinner, call the array adapter
        createArrayAdapter(R.array.duration_of_workout, spinnerWorkoutDuration); //Right now just calling it for the duration of workout
        createArrayAdapter(R.array.equipment_available, spinnerEquipment);
        createArrayAdapter(R.array.type_of_workout, spinnerCardioVsStrength);
        createArrayAdapter(R.array.muscles_targeted, spinnerTargetedMuscles);
        createArrayAdapter(R.array.fitness_goal, spinnerFitnessGoal);

        // Spinner click listener
        spinnerWorkoutDuration.setOnItemSelectedListener(this);
        spinnerEquipment.setOnItemSelectedListener(this);
        spinnerCardioVsStrength.setOnItemSelectedListener(this);
        spinnerTargetedMuscles.setOnItemSelectedListener(this);
        spinnerFitnessGoal.setOnItemSelectedListener(this);

        //On-click listener to be used by the generate workout button
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { //All widgets are "Views" so they can always be passed as an instance of their base-class "View"
                Button b = (Button) v;
                System.out.println("the on click listener is working");

                //If the Generate workout button was clicked....
                if (b == mGenerateWorkout){
                    Log.d(TAG, "onClick: The generate workout button has been clicked");
                    Log.d(TAG, "onClick: Calling the generate workout method");

                   //Calling the TEMP class 'ExampleWorkoutOutput' for testing purposes
                    //Todo Call the backend code as an AsyncTask since since Stack output says too many activities on the main thread
                    ExampleWorkoutOutput example = new ExampleWorkoutOutput();
                    workoutResults = example.getExample();

                    //set to context to go in the intent call
                    Context context = MainActivity.this;
                    // Store the destination activity in a class to go in the intent call
                    Class destinationActivity = CustomWorkout.class;
                    // Create the intent that will be used to start the CustomWorkout Activity -- intent
                    // creation needs a context and a destination
                    Intent startCustomWorkoutActivityIntent = new Intent(context, destinationActivity);

                    //Need to convert JSON array to string to add it to intent and pass to the second page
                    startCustomWorkoutActivityIntent.putExtra("workout", workoutResults.toString());

                    //ToDO: Add extra content to the bundle to pass - the list of leftover exercises

                    Log.d(TAG, "onClick: Starting a new Activity....");
                    // Start the CustomWorkout activity
                    startActivity(startCustomWorkoutActivityIntent);
                }
            }
        };
        //Set the on-click listener for the generator workout button
        mGenerateWorkout.setOnClickListener(listener);
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
        int id  = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //Function to Create an array adapter and set adapter
    public void createArrayAdapter(int textArrayResId, Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                textArrayResId, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    //Create a function that responds to the selections from the dropdown menu
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Check which spinner was selected and which item was selected for that category
        final String TAG = "onItemsSelected";

        switch (parent.getId()) {
            case R.id.spinnerTimeDuration:
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + "for Time duration", Toast.LENGTH_LONG).show();
                //Add the selected item to the JSON object
                try {
                    userInput.put("time", item);
                    //Output the JSON object to the command line to check
                    Log.i("JSON Body", userInput.toString());
                } catch (Exception e) {
                    Log.e(TAG, "onItemSelected: Error with User Input!");
                }
                break;

            case R.id.spinnerEquipment:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + "for Equipment", Toast.LENGTH_LONG).show();
                //Add the user-input to a JSON object
                try {
                    userInput.put("equipment", item);
                    //Output the JSON object to the command line to check
                    Log.i("JSON Body", userInput.toString());
                } catch (Exception e) {
                    Log.e(TAG, "onItemSelected: Error with User Input!");
                }
                break;

            case R.id.spinnerCardioVsStrength:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + " for Cardio vs. Strength", Toast.LENGTH_LONG).show();
                //Add the user-input to a JSON object
                try {
                    userInput.put("cardioVsStrength", item);
                    //Output the JSON object to the command line to check
                    Log.i("JSON Body", userInput.toString());
                } catch(Exception e) {
                    Log.e(TAG, "onItemSelected: Error with User Input!");
                }
                break;

            case R.id.spinnerTargetedMuscles:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + " for TargetedMucles", Toast.LENGTH_LONG).show();
                //Add the user-input to a JSON object
                try {
                    userInput.put("targetedMuscles", item);
                    //Output the JSON object to the command line to check
                    Log.i("JSON Body", userInput.toString());
                } catch(Exception e) {
                    Log.e(TAG, "onItemSelected: Error with User Input!");
                }
                break;

            case R.id.spinnerFitnessGoal:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + " for Fitness Goal", Toast.LENGTH_LONG).show();
                //Add the user-input to a JSON object
                try {
                    userInput.put("fitnessGoal", item);
                    //Output the JSON object to the command line to check
                    Log.i("JSON Body", userInput.toString());
                } catch(Exception e) {
                    Log.e(TAG, "onItemSelected: Error with User Input!");
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0){
        // TODO Deal with this
    }

    //**************************Back-end******************************************************
    public void createWorkout(int duration, boolean equipment, String muscleGroup, String type) {
        // This function takes all of the potential workout exercises and iterates through them and
        // adds the applicable ones to the current workout potential exercises list. From that list
        // there will be a random number generator that picks the number of exercises that will be
        // used for the workout.


        JSONParser parser = new JSONParser();
        try {
            JSONArray iterationExercises = (JSONArray) parser.parse(new FileReader("/main/json/exercises.json"));
            JSONArray allExercises = (JSONArray) parser.parse(new FileReader("/main/json/exercises.json"));

            // check equipment to see if we can minimize exercises based on that
            if (!equipment)
                iterationExercises = removeEquipmentRequired(iterationExercises, allExercises);

            // check the muscle group to minimize the exercises
            if (!muscleGroup.equals("full body")) {
                iterationExercises = removeUnapplicableMuscleExercises(iterationExercises, allExercises, muscleGroup);
            }

            // check the type to minimize the exercises
            iterationExercises = removeUnapplicableTypeExercises(iterationExercises, allExercises, type);

            // start checking if we have enough exercises and creating the workout
            int exerciseCount = allExercises.length();
            int numExercisesNeeded = numExercisesRequired(duration);

            if (exerciseCount < numExercisesNeeded) {
                // throw an error saying that we can't make that kind of workout?
            } else {
                // apply the random number generator that gives a number between 0 and length and
                // then adds that exercises to workout then removes it from the list and repeats
                // until we have as many as we need

                JSONArray workoutGenerated = generateWorkoutFromExercises(numExercisesNeeded, allExercises);
                Boolean workoutDurationCorrect = checkWorkoutTime(workoutGenerated, type, duration);
                if (workoutDurationCorrect) {
                    // post workout
                } else {
                    // cry
                }
            }

            // check that we have the right amount of time for the duration given for the workout built
            // post workout
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int generateRandomNumber(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }

    public JSONArray createHIIT(int duration, boolean equipment, String muscleGroup, String type) {
        // create workout
        return null;

    }

    public JSONArray removeEquipmentRequired(JSONArray iterationExercises, JSONArray allExercises) {
        for (int i = 0; i < iterationExercises.length(); i++) {
            // remove all the exercises that require equipment
            try {
                JSONObject exercise = (JSONObject) iterationExercises.get(i);
                if (exercise.getBoolean("equipment"))
                    allExercises.remove(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allExercises;
    }

    public JSONArray removeUnapplicableMuscleExercises(JSONArray iterationExercises, JSONArray allExercises, String muscleGroup) {
        try {
            for (int i = 0; i < iterationExercises.length(); i++) {
                // remove exercises that are not for that muscle group
                JSONObject exercise = (JSONObject) iterationExercises.get(i);
                if (!exercise.getString("muscle group").equals(muscleGroup))
                    allExercises.remove(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allExercises;
    }

    public JSONArray removeUnapplicableTypeExercises(JSONArray iterationExercises, JSONArray allExercises, String type) {
        try {
            for (int i = 0; i < iterationExercises.length(); i++) {
                // remove exercises that are not for that muscle group
                JSONObject exercise = (JSONObject) iterationExercises.get(i);
                if (!exercise.getString("type").equals(type))
                    allExercises.remove(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allExercises;
    }

    public JSONArray generateWorkoutFromExercises(int numExercisesNeeded, JSONArray allExercises) {
        JSONArray workoutGenerated = new JSONArray();
        try {
            for (int i = 0; i < numExercisesNeeded; i++){
                int randomNumber = generateRandomNumber(allExercises.length());
                workoutGenerated.put(allExercises.get(randomNumber));
                allExercises.remove(randomNumber);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workoutGenerated;
    }

    public int numExercisesRequired(int duration) {
        // create a decision maker that decides how many exercises needed per time unit
        int num = 0;
        return num;
    }

    public boolean checkWorkoutTime(JSONArray workout, String type, int duration) {
        int totalWorkoutTime = 0;
        for (int i = 0; i < workout.length(); i++) {
            // check the type of workout
            // get the proper duration thing for that type
            // add to total duration of workout
            try {
                JSONObject exercise = (JSONObject) workout.get(i);
                JSONObject exerciseType = exercise.getJSONObject(type);
                int time = exerciseType.getInt("duration");
                totalWorkoutTime = totalWorkoutTime + time;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (duration == totalWorkoutTime)
            return true;
        else
            return false;
    }

}

