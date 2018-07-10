package com.example.micaela.fwd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Declare the generate workout button
    private Button mGenerateWorkout;

    //Create a JSON instance to hold the user-input
    JSONObject userInput = new JSONObject();
    //Create a JSON instance to hold the generated workout info
    JSONArray workoutResults = new JSONArray();
    final String TAG = "MainActivity";
    private Spinner spinnerWorkoutDuration, spinnerEquipment, spinnerCardioVsStrength,
            spinnerTargetedMuscles;
    ImageView targetedMusclesIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main_layout);

        //Set up Custom Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            Log.e(TAG, "onCreate: Support Action bar is null!");
        }
        TextView pageTitle = (TextView) findViewById(R.id.toolbar_title);
        pageTitle.setText(getString(R.string.mainActivityWorkoutPageTitle));

        mGenerateWorkout = (Button) findViewById(R.id.generateWorkoutButton);

        //Initialize Spinner objects and link them to their xml id
        spinnerWorkoutDuration = (Spinner) findViewById(R.id.spinnerTimeDuration);
        spinnerEquipment = (Spinner) findViewById(R.id.spinnerEquipment);
        spinnerCardioVsStrength = (Spinner) findViewById(R.id.spinnerCardioVsStrength);
        spinnerTargetedMuscles = (Spinner) findViewById(R.id.spinnerTargetedMuscles);
        targetedMusclesIcon = (ImageView) findViewById(R.id.iconMuscleDropDown);

        //For each spinner, call the array adapter
        createArrayAdapter(R.array.duration_of_workout, spinnerWorkoutDuration); //Right now just calling it for the duration of workout
        createArrayAdapter(R.array.equipment_available, spinnerEquipment);
        createArrayAdapter(R.array.type_of_workout, spinnerCardioVsStrength);
        createArrayAdapter(R.array.muscles_targeted, spinnerTargetedMuscles);

        //Set the Muscles targeted to hidden
        spinnerTargetedMuscles.setVisibility(View.GONE);
        targetedMusclesIcon.setVisibility(View.GONE);

        // Spinner click listener
        spinnerWorkoutDuration.setOnItemSelectedListener(this);
        spinnerEquipment.setOnItemSelectedListener(this);
        spinnerCardioVsStrength.setOnItemSelectedListener(this);
        spinnerTargetedMuscles.setOnItemSelectedListener(this);

        //On-click listener to be used by the generate workout button
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { //All widgets are "Views" so they can always be passed as an instance of their base-class "View"
                Button b = (Button) v;
                System.out.println("the on click listener is working");

                //If the Generate workout button was clicked....
                if (b == mGenerateWorkout) {

                    //Check to make sure that all the fields are selected
                    Log.d(TAG, "onClick: " + userInput.toString());
                    if (confirmAllOptionsSelected()) {

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
                        //startCustomWorkoutActivityIntent.putExtra("workout", workoutResults.toString());

                        //ToDO: Add extra content to the bundle to pass - the list of leftover exercises
                        Log.d(TAG, "onClick: Starting a new Activity....");
                        // Start the CustomWorkout activity
                        //startActivity(startCustomWorkoutActivityIntent);
                    }
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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //Function to Create an array adapter and set adapter
    public void createArrayAdapter(int textArrayResId, Spinner spinner) {
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, getResources().getStringArray(textArrayResId));
        spinner.setAdapter(customSpinnerAdapter);
    }

    //Create a function that responds to the selections from the dropdown menu
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Check which spinner was selected and which item was selected for that category
        final String TAG = "onItemsSelected";

        switch (parent.getId()) {
            case R.id.spinnerTimeDuration:
                //Check to make sure that it isn't the default selection
                if (position == 0) {

                    try {
                        userInput.put("time", "");
                    } catch (Exception e) {
                        Log.e(TAG, "onItemSelected: Error with User Input!");
                    }
                } else {

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
                }

                break;

            case R.id.spinnerEquipment:
                //Check to make sure that it isn't the default selection
                if (position == 0) {

                    try {
                        userInput.put("equipment", "");
                    } catch (Exception e) {
                        Log.e(TAG, "onItemSelected: Error with User Input!");
                    }
                } else {

                    // On selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();
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
                }
                break;

            case R.id.spinnerTargetedMuscles:
                //Check to make sure that it isn't the default selection
                if (position == 0) {
                    try {
                        userInput.put("targetedMuscles", "");
                    } catch (Exception e) {
                        Log.e(TAG, "onItemSelected: Error with User Input!");
                    }
                } else {
                    // On selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "Selected: " + item + " for TargetedMuscles", Toast.LENGTH_LONG).show();
                    //Add the user-input to a JSON object
                    try {
                        userInput.put("targetedMuscles", item);
                        //Output the JSON object to the command line to check
                        Log.i("JSON Body", userInput.toString());

                    } catch (Exception e) {
                        Log.e(TAG, "onItemSelected: Error with User Input!");
                    }
                }
                break;

            case R.id.spinnerCardioVsStrength:
                //Check to make sure that it isn't the default selection
                if (position == 0) {
                    try {
                        userInput.put("cardioVsStrength", "");
                    } catch (Exception e) {
                        Log.e(TAG, "onItemSelected: Error with User Input!");
                    }
                } else {
                    // On selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "Selected: " + item + " for cardioVsStrength", Toast.LENGTH_LONG).show();
                    //Add the user-input to a JSON object
                    try {
                        userInput.put("cardioVsStrength", item);
                        //Output the JSON object to the command line to check
                        Log.i("JSON Body", userInput.toString());
                        //Remove the muscle group option if cardio selected
                        if (item.equals("Strength")) {
                            spinnerTargetedMuscles.setVisibility(View.VISIBLE);
                            targetedMusclesIcon.setVisibility(View.VISIBLE);

                        } else {
                            spinnerTargetedMuscles.setVisibility(View.GONE);
                            targetedMusclesIcon.setVisibility(View.GONE);
                            spinnerTargetedMuscles.setSelection(0);
                            try {
                                userInput.put("targetedMuscles", "");
                            } catch (JSONException e) {
                                Log.e(TAG, "onItemSelected: Error with User Input");
                            }

                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onItemSelected: Error with User Input!");
                    }
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Deal with this
    }


    //Function to check that the user has selected input for all fields
    private boolean confirmAllOptionsSelected() {
        try {

            Log.d(TAG, "confirmAllOptionsSelected: "+userInput.get("time"));
            if (userInput.getString("time").equals("")) {
                Log.d(TAG, "confirmAllOptionsSelected: Enter your time duration.");
                return false;
            }
            if (userInput.getString("equipment").equals("")) {
                Log.d(TAG, "confirmAllOptionsSelected: Enter your equipment.");
                return false;
            }
            if (userInput.getString("cardioVsStrength").equals("")) {
                Log.d(TAG, "confirmAllOptionsSelected: Enter your type of workout.");
                return false;
            }
            if (userInput.getString("targetedMuscles").equals("")) {
                Log.d(TAG, "confirmAllOptionsSelected: Nothing selected for targeted muscles.");
                if (userInput.getString("cardioVsStrength").equals("Strength")) {
                    Log.d(TAG, "confirmAllOptionsSelected: Enter your targeted muscles.");
                    return false;
                }
            }

        } catch (JSONException e) {
            Log.e(TAG, "confirmAllOptionsSelected: Error Reading JSON!");
            return false;
        }
        return true;
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
            if (!muscleGroup.equals("full body"))
                iterationExercises = removeUnapplicableMuscleExercises(iterationExercises, allExercises, muscleGroup);

            // check the type to minimize the exercises
            iterationExercises = removeUnapplicableTypeExercises(iterationExercises, allExercises, type);

            // start checking if we have enough exercises and creating the workout
            int exerciseCount = allExercises.length();
            int numExercisesNeeded = numExercisesRequired(duration);

            if (exerciseCount < numExercisesNeeded) {
                // throw an error saying that we can't make that kind of workout?
            } else {
                // generate workout from a random number generator that picks an exercise at random
                // and adds it to the workout (repeats until workout created)
                JSONArray workoutGenerated = new JSONArray();
                if (!muscleGroup.equals("full body"))
                    workoutGenerated = generateWorkoutFromExercises(numExercisesNeeded, allExercises);
                else {
                    workoutGenerated = generateFullBodyWorkout(duration, allExercises);
                }
                Boolean workoutDurationCorrect = checkWorkoutTime(workoutGenerated, type, duration);
                if (workoutDurationCorrect) {
                    // post workout
                } else {
                    // throw an error or retry the whole thing
                }
            }

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

    public JSONArray createHIIT(int duration, boolean equipment, String muscleGroup, String
            type) {
        // create workout
        return null;

    }

    public JSONArray removeEquipmentRequired(JSONArray iterationExercises, JSONArray
            allExercises) {
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

    public JSONArray removeUnapplicableMuscleExercises(JSONArray iterationExercises, JSONArray
            allExercises, String muscleGroup) {
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

    public JSONArray removeUnapplicableTypeExercises(JSONArray iterationExercises, JSONArray
            allExercises, String type) {
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

    public JSONArray generateWorkoutFromExercises(int numExercisesNeeded, JSONArray
            allExercises) {
        JSONArray workoutGenerated = new JSONArray();
        try {
            for (int i = 0; i < numExercisesNeeded; i++) {
                int randomNumber = generateRandomNumber(allExercises.length());
                workoutGenerated.put(allExercises.get(randomNumber));
                allExercises.remove(randomNumber);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workoutGenerated;
    }

    public JSONArray generateFullBodyWorkout(int duration, JSONArray allExercises) {
        // look at duration and consider just doing a HIIT
        // if longer than 30 then create a full body workout
        return null;
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
        if (duration < totalWorkoutTime + 3 && duration > totalWorkoutTime - 3)
            return true;
        else
            return false;
    }

}

