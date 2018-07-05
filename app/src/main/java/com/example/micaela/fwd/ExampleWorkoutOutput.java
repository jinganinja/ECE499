/* THIS IS A TEMPORARY FUNCTION CREATED ONLY FOR TESTING PURPOSES TO TEST UI W/O BACKEND
 *
 */
package com.example.micaela.fwd;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileSystems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ExampleWorkoutOutput {
    private JSONArray example = new JSONArray();
    private static final String TAG = "ExampleWorkoutOutput";


    //Constructor
    public ExampleWorkoutOutput() {

        JSONArray workout = new JSONArray();
        setExample("Pushups", 3, 11, "TagLine goes here for Pushups");
        setExample("Bicep Curls",1, 30, "TagLine goes here for Bicep Curls");
        setExample("Burpees", 2, 15, "TagLine goes here for Burpees");
        Log.d(TAG, "ExampleWorkoutOutput: "+ this.example.toString());
    }


    public void setExample(String name, int sets, int reps, String tagLine) {
        this.example = example;
        JSONObject exercise = new JSONObject();
        JSONObject steps = new JSONObject();
        JSONArray description = new JSONArray();

        try {
            //put name of excercise
            exercise.put("name", name);

            //put description of exercise
            steps.put("Step 1", "Do this stuff for step 1 of "+ name + ". Do this stuff for step 1.Do this stuff for step 1. Do this stuff for step 1.Do this stuff for step 1.");
            steps.put("Step 2", "Do this stuff for step 2 of "+ name + ". Do this stuff for step 2.Do this stuff for step 2. Do this stuff for step 1.Do this stuff for step 2.");
            steps.put("Step 3", "Do this stuff for step 3 of "+ name + ". Do this stuff for step 3.Do this stuff for step 3. Do this stuff for step 1.Do this stuff for step 3.");
            description.put(steps);
            exercise.put("description", description);

            //put reps, sets, and image path for an exercise
            exercise.put("tagLine",tagLine);
            exercise.put("reps",reps);
            exercise.put("sets", sets);
            exercise.put("img", "main/res/drawable/push_up_fig_1.png");

            //Add the exercise to our output array
            this.example.put(exercise);

        } catch (JSONException e){
            Log.d(TAG, "onClick: JSON exception!!!");
        }
    }

    //Getter
    public JSONArray getExample() {
        return this.example;
    }


}
