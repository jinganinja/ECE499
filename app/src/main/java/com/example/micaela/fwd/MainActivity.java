package com.example.micaela.fwd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.example.micaela.fwd.CustomWorkout;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class MainActivity extends AppCompatActivity {

    private Button mGenerateWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGenerateWorkout = (Button) findViewById(R.id.generateWorkoutButton);


        //Set an on-click listener to do something when generateWorkoutButton clicked
        mGenerateWorkout.setOnClickListener(new View.OnClickListener() {


            // The onClick method is triggered when this button (mDoSomethingCoolButton) is clicked
            // param v is The view that is clicked. In this case, it's mGenerateWorkout Button.

            @Override
            public void onClick(View v) {

                //set to context to go in the intent call
                Context context = MainActivity.this;

                // Store the destination activity in a class to go in the intent call
                Class destinationActivity = CustomWorkout.class;

                // Create the intent that will be used to start the CustomWorkout Activity -- intent
                // creation needs a context and a destination
                Intent startCustomWorkoutActivityIntent = new Intent(context, destinationActivity);

                // Start the CustomWorkout activity
                startActivity(startCustomWorkoutActivityIntent);
            }
        });
    }

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

