package com.example.micaela.fwd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.example.micaela.fwd.CustomWorkout;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
            JSONArray workoutExercises = (JSONArray) parser.parse(new FileReader("/main/json/exercises.json"));
            JSONArray allAvailableExercises = (JSONArray) parser.parse(new FileReader("/main/json/exercises.json"));
            // check equipment to see if we can minimize exercises based on that
            if (!equipment) {
                for (Object obj:allAvailableExercises) {
                    // remove all the exercises that require equipment
                }
            }
            // check the muscle group to minimize the exercises
            if (muscleGroup != "full body") {
                for (Object obj:allAvailableExercises) {
                    // remove exercises that are not for that muscle group
                }
            }
            // check the type to minimize the exercises
            String userExerciseType;
            for (Object obj:allAvailableExercises) {
                JSONObject exercise = (JSONObject) obj;
                userExerciseType = (String) exercise.get("type");
                if (userExerciseType != type || !userExerciseType.equals("both")) {
                    // remove that type of exercise
                }
            }
            // determine how many exercises needed for the duration
            // determine if we have enough
            // build a random number generator for that many things and generate a random number
            // use the random number to add that exercise to the workout
            // remove that exercise from the list and repeat
            // complete building the workout
            // check that we have the right amount of time for the duration given for the workout built
            // post workout
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }


    }

    public JSONArray createHIIT(int duration, boolean equipment, String muscleGroup, String type) {
        // create workout
        return null;
    }
}

