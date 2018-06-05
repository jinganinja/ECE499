package com.example.micaela.fwd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Declare the generate workout button
    private Button mGenerateWorkout;
    //Declare all the dropDown menus
    private Spinner spinnerWorkoutDuration, spinnerEquipment, spinnerCardioVsStrength,
        spinnerTargetedMuscles, spinnerFitnessGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGenerateWorkout = (Button) findViewById(R.id.generateWorkoutButton);


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

    //Function to Create an array adapter and set adapter
    public void createArrayAdapter(int textArrayResId, Spinner spinner){

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                textArrayResId, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }





}

