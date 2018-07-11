package com.example.micaela.fwd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;
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
                        try {
                        boolean equipment = false;
                            int duration = Integer.parseInt(userInput.getString("time"));
                            if (userInput.getString("equipment").equals("Gym Facility"))
                                equipment = true;
                            else if (userInput.getString("equipment").equals("None (Bodyweight"))
                                equipment = false;
                            String muscleGroup = userInput.getString("targetedMuscles");
                            String type = userInput.getString("cardioVsStrength");
                            workoutResults = createWorkout(duration, equipment, muscleGroup, type);
                            ExampleWorkoutOutput example = new ExampleWorkoutOutput();
                            workoutResults = example.getExample();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                      
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

                if (b == mGenerateWorkout){
                    Log.d(TAG, "onClick: The generate workout button has been clicked");
                    Log.d(TAG, "onClick: Calling the generate workout method");

                   //Calling the TEMP class 'ExampleWorkoutOutput' for testing purposes
                    //Todo Call the backend code as an AsyncTask since since Stack output says too many activities on the main thread
                    try {
                        boolean equipment = false;
                        int duration = Integer.parseInt(userInput.getString("time"));
                        if (userInput.getString("equipment").equals("Gym Facility"))
                            equipment = true;
                        else if (userInput.getString("equipment").equals("None (Bodyweight"))
                            equipment = false;
                        String muscleGroup = userInput.getString("targetedMuscles");
                        String type = userInput.getString("cardioVsStrength");
                        workoutResults = createWorkout(duration, equipment, muscleGroup, type);
                        ExampleWorkoutOutput example = new ExampleWorkoutOutput();
                        workoutResults = example.getExample();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
                                if (userInput.has("targetedMuscles")) {userInput.remove("targetedMuscles");}
                               // userInput.put("targetedMuscles", "");
                            } catch (Exception e) {
                                Log.e(TAG, "onItemSelected: Error with User Input");
                            }
                        }
                    } catch (JSONException e) {
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
        String message = "Enter your";
        List<String> missingItems= new ArrayList<String> ();
        boolean allOptionsSelected = true;// Start off with the assumption everything is good!
        try {
            Log.d(TAG, "confirmAllOptionsSelected: userInput" + userInput.toString());
            if (userInput.getString("time").equals("")) {
                 missingItems.add("time duration");
                Log.d(TAG, "confirmAllOptionsSelected: Enter your time duration.");
                allOptionsSelected = false;
            }
            if (userInput.getString("equipment").equals("")) {
                missingItems.add("equipment");
                Log.d(TAG, "confirmAllOptionsSelected: Enter your equipment.");
                allOptionsSelected = false;
            }
            if (userInput.getString("cardioVsStrength").equals("")) {
                missingItems.add("type of workout");
                Log.d(TAG, "confirmAllOptionsSelected: Enter your type of workout.");
                allOptionsSelected = false;
            }
            if (userInput.has("targetedMuscles") && userInput.getString("targetedMuscles").equals("")){
                Log.d(TAG, "confirmAllOptionsSelected: Nothing selected for targeted muscles.");
                if (userInput.getString("cardioVsStrength").equals("Strength")) {
                    missingItems.add("targeted muscles");
                    Log.d(TAG, "confirmAllOptionsSelected: Enter your targeted muscles.");
                    allOptionsSelected = false;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "confirmAllOptionsSelected: Error Reading JSON!");
            showAlertDialogButtonClicked("Error Reading JSON!");
            return false;
        }

        if (!allOptionsSelected){
            //Add all the missing items to the message to be displayed
            int i; //Declare counter of items
            for (i= 0; i < missingItems.size()-1; i++){ //subtract 1 to deal with last item separately
                message = message+" "+missingItems.get(i)+",";
            }
            //Add the last item to the message
            message = message + " and "+ missingItems.get(i)+".";
            showAlertDialogButtonClicked(message);
            return false;
        }
        return true;
    }

    //Make a function to have a pop-up when fields not all selected
    public void showAlertDialogButtonClicked(String message) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You Forgot Something!");
        builder.setMessage(message);

        // add the buttons
        builder.setPositiveButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // we're good
            }
        });
    }


    //**************************Back-end******************************************************
    public JSONArray createWorkout(int duration, boolean equipment, String muscleGroup, String type) {
        // This function takes all of the potential workout exercises and iterates through them and
        // adds the applicable ones to the current workout potential exercises list. From that list
        // there will be a random number generator that picks the number of exercises that will be
        // used for the workout.

        if (type.equals("Cardio")) {
            return createCardioWorkout(duration, equipment);
        } else if (type.equals("Strength")) {
            return createStrengthWorkout(duration, equipment, muscleGroup);
        } else if (type.equals("HIIT")) {
            return createHIITWorkout(duration, equipment);
        } else {
            return null;
        }
    }

    public int generateRandomNumber(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }

    public JSONArray createCardioWorkout(int duration, boolean equipment) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray allExercises = (JSONArray) parser.parse(new FileReader("/main/json/cardioExercises.json"));
            JSONArray iterationExercises = (JSONArray) parser.parse(new FileReader("/main/json/cardioExercises.json"));
            if (!equipment)
                allExercises = removeEquipmentRequired(iterationExercises, allExercises);
            int exerciseCount = cardioNumExercisesForDuration(duration);
            if (exerciseCount < allExercises.length()){
                return generateWorkoutFromExercises(exerciseCount, allExercises);
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray createStrengthWorkout(int duration, boolean equipment, String muscleGroup) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray allExercises = (JSONArray) parser.parse(new FileReader("/main/json/strengthExercises.json"));
            JSONArray iterationExercises = (JSONArray) parser.parse(new FileReader("/main/json/strengthExercises.json"));
            if (!equipment)
                allExercises = removeEquipmentRequired(iterationExercises, allExercises);
            allExercises = removeUnapplicableMuscleExercises(iterationExercises, allExercises, muscleGroup);
            int exerciseCount = strengthNumExercisesForDuration(duration);
            if (exerciseCount < allExercises.length()){
                return generateWorkoutFromExercises(exerciseCount, allExercises);
            } else {
                return null;
            }        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public JSONArray createHIITWorkout(int duration, boolean equipment) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray allExercises = (JSONArray) parser.parse(new FileReader("/main/json/HIITExercises.json"));
            JSONArray iterationExercises = (JSONArray) parser.parse(new FileReader("/main/json/HIITExercises.json"));
            if (!equipment)
                allExercises = removeEquipmentRequired(iterationExercises, allExercises);
            int exerciseCount = HIITNumExercisesForDuration(duration);
            if (exerciseCount < allExercises.length()){
                return generateWorkoutFromExercises(exerciseCount, allExercises);
            } else {
                return null;
            }        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public int cardioNumExercisesForDuration(int duration) {
        switch(duration) {
            case 10:
                return 1;

            case 15:
                return 2;

            case 20:
                return 2;

            case 30:
                return 3;

            case 45:
                return 4;

            case 60:
                return 6;
        }
        return 0;
    }

    public int strengthNumExercisesForDuration(int duration) {
        switch(duration) {
            case 10:
                return 3;

            case 15:
                return 5;

            case 20:
                return 7;

            case 30:
                return 10;

            case 45:
                return 15;

            case 60:
                return 20;
        }
        return 0;
    }

    public int HIITNumExercisesForDuration(int duration) { // duration can only equal one of: [10, 15, 20, 30, 45, 60]
        switch(duration) {
            case 10:
                return 7;

            case 15:
                return 10;

            case 20:
                return 14;

            case 30:
                return 21;

            case 45:
                return 30;

            case 60:
                return 39;
        }
        return 0;
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
}

