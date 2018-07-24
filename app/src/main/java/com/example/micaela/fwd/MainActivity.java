package com.example.micaela.fwd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.content.res.AssetManager;
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
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
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
    JSONObject workoutResults = new JSONObject();
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
        pageTitle.setTextColor(Color.parseColor("#000000"));



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
        showTargetedMuscles(false);

        // Spinner click listener
        spinnerWorkoutDuration.setOnItemSelectedListener(this);
        spinnerEquipment.setOnItemSelectedListener(this);
        spinnerCardioVsStrength.setOnItemSelectedListener(this);
        spinnerTargetedMuscles.setOnItemSelectedListener(this);

        //On-click listener to be used by the generate workout button
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { //All widgets are "Views" so they can always be passed as an instance of their base-class "View"
                Context context = MainActivity.this;
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
                        //Todo: parse the time from strings "20 min" to int "20"
                        try {
                            Log.i(TAG, "The user input: " + userInput);
                            boolean equipment = false;
                            String durationString = userInput.getString("time");
                            Log.i(TAG, "The String of duration: " + durationString);
                            int duration;
                            if (durationString.equals("1 hour"))
                                duration = 60;
                            else
                                duration = Integer.parseInt(durationString.substring(0, durationString.length() - 5));
                            if (userInput.getString("equipment").equals("Gym Facility"))
                                equipment = true;
                            else if (userInput.getString("equipment").equals("Bodyweight Only"))
                                equipment = false;
                            String muscleGroup = "";
                            if (userInput.has("targetedMuscles"))
                                muscleGroup = userInput.getString("targetedMuscles").toLowerCase();
                            String type = userInput.getString("cardioVsStrength");
                            Log.i(TAG, "The duration: " + Integer.toString(duration) + " equipment: " + Boolean.toString(equipment) + " muscleGroup: " + muscleGroup + " type: " + type);
                            workoutResults = createWorkout(context, duration, equipment, muscleGroup, type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
/*                              ExampleWorkoutOutput example = new ExampleWorkoutOutput();
                              workoutResults = example.getExample();*/
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        //set to context to go in the intent call
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
            }
        };
        //Set the on-click listener for the generator workout button
        mGenerateWorkout.setOnClickListener(listener);
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
                        showTargetedMuscles(false);
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
                            showTargetedMuscles(true);
                        } else {
                            showTargetedMuscles(false); }
                    } catch (JSONException e) {
                        Log.e(TAG, "onItemSelected: Error with User Input!");
                    }
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        //Mandatory function, no idea of its purpose but I get errors when I delete it
    }

    public void showTargetedMuscles(boolean show){
        if (show){
            spinnerTargetedMuscles.setVisibility(View.VISIBLE);
            targetedMusclesIcon.setVisibility(View.VISIBLE);
        } else {
            spinnerTargetedMuscles.setVisibility(View.GONE);
            targetedMusclesIcon.setVisibility(View.GONE);
            spinnerTargetedMuscles.setSelection(0);
            if (userInput.has("targetedMuscles")) {userInput.remove("targetedMuscles");}
        }
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
            //If there is only 1 item, then message is simple
            if (missingItems.size() == 1){
                message = message + " " + missingItems.get(0) + ".";
            } else { //Need to loop through the items

                int i; //Declare counter of items
                for (i = 0; i < missingItems.size() - 2; i++) { //subtract 1 to deal with last 2 items separately
                    message = message + " " + missingItems.get(i) + ",";
                }
                //Add the last item to the message
                message = message + " " + missingItems.get(i) + " and " + missingItems.get(i+1) + ".";
            }
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
    public JSONObject createWorkout(Context context, int duration, boolean equipment, String muscleGroup, String type) {
        // This function takes all of the potential workout exercises and iterates through them and
        // adds the applicable ones to the current workout potential exercises list. From that list
        // there will be a random number generator that picks the number of exercises that will be
        // used for the workout.
        Log.i(TAG, "The duration: " + Integer.toString(duration) + " equipment: " + Boolean.toString(equipment) + " muscleGroup: " + muscleGroup + " type: " + type);

        if (type.equals("Cardio")) {
            return createCardioWorkout(context, duration, equipment);
        } else if (type.equals("Strength")) {
            return createStrengthWorkout(context, duration, equipment, muscleGroup);
        } else if (type.equals("HIIT")) {
            return createHIITWorkout(context, duration, equipment);
        } else {
            Log.d(TAG, "Returning null in createWorkout: " + type);
            JSONObject returnEmpty = new JSONObject();
            return returnEmpty;
        }
    }

    public int generateRandomNumber(int range) {
        Random rand = new Random();
        int randomNum = rand.nextInt(range);
        Log.i(TAG, "The random number and range: " + Integer.toString(randomNum) + ", " + Integer.toString(range));
        return randomNum;
    }

    public JSONObject createCardioWorkout(Context context, int duration, boolean equipment) {
        JSONParser parser = new JSONParser();
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("json/cardioExercises.json");

            JSONArray allExercises = new JSONArray(parser.parse(new InputStreamReader(is, "UTF-8")).toString());
            JSONArray iterationExercises = allExercises;
            if (!equipment)
                allExercises = removeEquipmentRequired(iterationExercises, allExercises);
            int exerciseCount = cardioNumExercisesForDuration(duration);
            if (exerciseCount < allExercises.length()){
                return generateWorkoutFromExercises(exerciseCount, allExercises, "cardio");
            } else {
                Log.d(TAG, "Returning null in createCardioWorkout: " + Integer.toString(exerciseCount));
                JSONObject returnEmpty = new JSONObject();
                return returnEmpty;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Returning null in createCardioWorkout");
        JSONObject returnEmpty = new JSONObject();
        return returnEmpty;
    }

    public JSONObject createStrengthWorkout(Context context, int duration, boolean equipment, String muscleGroup) {
        JSONParser parser = new JSONParser();
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("json/strengthExercises.json");

            JSONArray allExercises = new JSONArray(parser.parse(new InputStreamReader(is, "UTF-8")).toString());
            JSONArray iterationExercises = allExercises;
            if (!equipment)
                allExercises = removeEquipmentRequired(iterationExercises, allExercises);
            allExercises = removeUnapplicableMuscleExercises(iterationExercises, allExercises, muscleGroup);
            final int allExercisesLength = allExercises.length();
            for (int i = 0; i < allExercisesLength; i ++) {
                String exercise = allExercises.getJSONObject(i).getString("name");
                Log.i(TAG, "Exercise name kept: " + exercise);
            }

            int exerciseCount = strengthNumExercisesForDuration(duration);
            if (exerciseCount < allExercises.length()){
                return generateWorkoutFromExercises(exerciseCount, allExercises, "strength");
            } else {
                Log.d(TAG, "Returning null in createStrengthWorkout: " + Integer.toString(exerciseCount));
                JSONObject returnEmpty = new JSONObject();
                return returnEmpty;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Returning null in createStrengthWorkout");
        JSONObject returnEmpty = new JSONObject();
        return returnEmpty;
    }

    public JSONObject createHIITWorkout(Context context, int duration, boolean equipment) {
        JSONParser parser = new JSONParser();
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("json/HIITExercises.json");

            JSONArray allExercises = new JSONArray(parser.parse(new InputStreamReader(is, "UTF-8")).toString());
            JSONArray iterationExercises = allExercises;
            if (!equipment)
                allExercises = removeEquipmentRequired(iterationExercises, allExercises);
            int exerciseCount = HIITNumExercisesForDuration(duration);
            if (exerciseCount < allExercises.length()){
                return generateWorkoutFromExercises(exerciseCount, allExercises, "HIIT");
            } else {
                Log.d(TAG, "Returning null in createHIITWorkout: " + Integer.toString(exerciseCount));
                for (int i = 0; i < allExercises.length(); i++) {
                    String name = allExercises.getJSONObject(i).getString("name");
                    Log.i(TAG, "Exercise number " + Integer.toString(i) + " is: " + name);
                }
                JSONObject returnEmpty = new JSONObject();
                return returnEmpty;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Returning null in createHIITWorkout");
        JSONObject returnEmpty = new JSONObject();
        return returnEmpty;
    }

    public JSONArray removeEquipmentRequired(JSONArray iterationExercises, JSONArray
            allExercises) {
        for (int i = 0; i < iterationExercises.length(); i++) {
            // remove all the exercises that require equipment
            try {
                JSONObject exercise = (JSONObject) iterationExercises.getJSONObject(i);
                if (exercise.getBoolean("equipment"))
                    Log.i(TAG, "Removing exercise b/c no equipment available: " + exercise.getString("name"));
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
                return 15;

            case 45:
                return 18;

            case 60:
                return 20;
        }
        return 0;
    }

    public JSONArray removeUnapplicableMuscleExercises(JSONArray iterationExercises, JSONArray allExercises, String muscleGroup) {
        try {
            for (int i = 0; i < iterationExercises.length(); i++) {
                // remove exercises that are not for that muscle group
                JSONObject exercise = (JSONObject) iterationExercises.get(i);
                if (!exercise.getString("muscle group").equals(muscleGroup))
                    Log.i(TAG, "Removing exercise b/c of muscle group: " + exercise.getString("name"));
                    allExercises.remove(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allExercises;
    }

    public JSONObject generateWorkoutFromExercises(int numExercisesNeeded, JSONArray allExercises, String typeOfWorkout) {
        // need to rearrange this so we maintain all exercises for the data given back
        // data give nack needs to be changed to a jsonobject that includes all exercises and the workout
        // all exercises needs to be the descriptions
        JSONParser parser = new JSONParser();
        JSONArray allWorkoutDescriptions = new JSONArray();
        JSONObject allData = new JSONObject();
        JSONArray exerciseOutput = new JSONArray();
        try {
            AssetManager assetManager = getAssets();
            if (typeOfWorkout.equals("strength")){
                InputStream is = assetManager.open("json/strengthOutputExercises.json");
                exerciseOutput = new JSONArray(parser.parse(new InputStreamReader(is, "UTF-8")).toString());
            }
            else if (typeOfWorkout.equals("cardio")){
                InputStream is = assetManager.open("json/cardioOutputExercises.json");
                exerciseOutput = new JSONArray(parser.parse(new InputStreamReader(is, "UTF-8")).toString());
            }

            else if (typeOfWorkout.equals("HIIT")) {
                InputStream is = assetManager.open("json/HIITOutputExercises.json");
                exerciseOutput = new JSONArray(parser.parse(new InputStreamReader(is, "UTF-8")).toString());
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            int allExercisesLength = allExercises.length();
            for (int i = 0; i < allExercisesLength; i++) {
                int exerciseOutputLength = exerciseOutput.length();
                for (int j = 0; j < exerciseOutputLength; j++) {
                    if (exerciseOutput.getJSONObject(j) != null && allExercises.getJSONObject(i) != null) {
                        if(exerciseOutput.getJSONObject(j).getString("name") != null && allExercises.getJSONObject(i).getString("name") != null) {
                            if (exerciseOutput.getJSONObject(j).getString("name").equals(allExercises.getJSONObject(i).getString("name"))) {
                                allWorkoutDescriptions.put(exerciseOutput.getJSONObject(j));
                                break;
                            }
                        } else {
                            Log.i(TAG, "Something doesn't have a name.......");
                        }
                    } else {
                        Log.i(TAG, "Something is null: " + exerciseOutput.getJSONObject(j).toString() + ", " + allExercises.getJSONObject(i).toString());
                    }
                }
            }
            allData.put("allWorkoutDescriptions", allWorkoutDescriptions);
            JSONArray workoutGenerated = new JSONArray();
            Log.i(TAG, "The number of needed exercises: " + Integer.toString(numExercisesNeeded) + ", The number of exercises available: " + Integer.toString(allWorkoutDescriptions.length()));
            for (int i = 0; i < numExercisesNeeded; i++) {
                int randomNumber = generateRandomNumber(allWorkoutDescriptions.length());
                workoutGenerated.put(allWorkoutDescriptions.get(randomNumber));
                allWorkoutDescriptions.remove(randomNumber);
            }
            allData.put("workoutDescriptions", workoutGenerated);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "allData before returned: " + allData);
        return allData;
    }
}

