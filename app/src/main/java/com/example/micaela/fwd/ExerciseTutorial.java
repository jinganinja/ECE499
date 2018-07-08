/*This is the Activity Page that shows the step-by-step instructions for a given exercise*/

package com.example.micaela.fwd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
public class ExerciseTutorial extends AppCompatActivity {

    private ListView tutorialSteps;
    private static final String TAG = "ExerciseTutorial";
    private List<ExerciseListObject> allExercises;
    private ExerciseListObject exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_tutorial);
        tutorialSteps = (ListView) findViewById(R.id.xmlTutListView);

        //Get data from bundle
        try {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            int position = extras.getInt("index");
            String jsonArrayWorkout = extras.getString("workout");
            ParseJSONForUI parseJSONForUI = new ParseJSONForUI(jsonArrayWorkout);
            allExercises = parseJSONForUI.getExercises();
            exercise = allExercises.get(position);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error retriving data from bundle!");
        }

        //Set up Toolbar at top to have the name of the exercise in it
        //Toolbar stufffs
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView pageTitle = (TextView) findViewById(R.id.toolbar_title);
        Log.d(TAG, "onCreate: The Name of the current Exercise is:" + exercise.getName());
        pageTitle.setText(exercise.getName());

        //Call the adapter for the Listview
        TutorialListAdapter tutorialListAdapter = new TutorialListAdapter(
                ExerciseTutorial.this, R.layout.tutorial_list_item, exercise.getDescription(), exercise.getDescripImgs());
        tutorialSteps.setAdapter(tutorialListAdapter);

    }
}
