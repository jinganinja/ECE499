package com.example.micaela.fwd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

        // Spinner click listener
        spinnerWorkoutDuration.setOnItemSelectedListener(this);
        spinnerEquipment.setOnItemSelectedListener(this);
        spinnerCardioVsStrength.setOnItemSelectedListener(this);
        spinnerTargetedMuscles.setOnItemSelectedListener(this);
        spinnerFitnessGoal.setOnItemSelectedListener(this);

        String hi = new String("hey");

        //WHAT is this piece of code
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { //All widgets are "Views" so they can always be passed as an instance of their base-class "View"
                Button b = (Button) v;
                System.out.println("the on click listener is working");
                //If the Generate workout button was clicked....
                if (b == mGenerateWorkout){
                    System.out.println("I can see that this was the generate workout button ");

                    //set to context to go in the intent call
                    Context context = MainActivity.this;

                    // Store the destination activity in a class to go in the intent call
                    Class destinationActivity = CustomWorkout.class;
                    // Create the intent that will be used to start the CustomWorkout Activity -- intent
                    // creation needs a context and a destination
                    Intent startCustomWorkoutActivityIntent = new Intent(context, destinationActivity);
                    System.out.println("I am calling to start a new activity ");

                    // Start the CustomWorkout activity
                    startActivity(startCustomWorkoutActivityIntent);
                }
            }
        };


        //Set the on-click listener for the generator workout button
        mGenerateWorkout.setOnClickListener(listener);

/*
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
        }); */
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

    // TODO perform a check that all the dropdown menus have an item selected (maybe not necessary w/default values?)
    //Create a function that responds to the selections from the dropdown menu
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Check which spinner was selected and which item was selected for that category
        switch (parent.getId()) {
            case R.id.spinnerTimeDuration:
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + "for Time duration", Toast.LENGTH_LONG).show();
                break;

            case R.id.spinnerEquipment:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + "for Equipment", Toast.LENGTH_LONG).show();
                break;

            case R.id.spinnerCardioVsStrength:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + " for Cardio vs. Strength", Toast.LENGTH_LONG).show();
                break;

            case R.id.spinnerTargetedMuscles:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + " for TargetedMucles", Toast.LENGTH_LONG).show();
                break;

            case R.id.spinnerFitnessGoal:
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item + " for Fitness Goal", Toast.LENGTH_LONG).show();
                break;
        }


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


}

