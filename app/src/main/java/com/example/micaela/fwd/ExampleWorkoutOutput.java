/* THIS IS A TEMPORARY FUNCTION CREATED ONLY FOR TESTING PURPOSES TO TEST UI W/O BACKEND
 * Creates a JSON array with a format similar to that which will be passed from the back-end of
 * the workoutgenerator to the front end
 */
package com.example.micaela.fwd;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExampleWorkoutOutput {
    private JSONArray example = new JSONArray();
    private static final String TAG = "ExampleWorkoutOutput";

    //Constructor
    public ExampleWorkoutOutput() {

        JSONArray workout = new JSONArray();
        setExample("Pushups", 3, 11, "TagLine goes here for Pushups");
        setExample("Bicep Curls", 1, 30, "TagLine goes here for Bicep Curls");
        setExample("Burpees", 2, 15, "TagLine goes here for Burpees");
        Log.d(TAG, "ExampleWorkoutOutput: " + this.example.toString());
    }

    public void setExample(String name, int sets, int reps, String tagLine) {
        this.example = example;
        JSONObject exercise = new JSONObject();
        JSONObject step1 = new JSONObject();
        JSONObject step2 = new JSONObject();
        JSONObject step3 = new JSONObject();
        JSONObject images = new JSONObject();

        JSONArray description = new JSONArray();
        try {
            //put name of excercise
            exercise.put("name", name);

            //put description of exercise
            step1.put("Step 1", "Do this stuff for step 1 of " + name + ". Do this stuff for step 1.Do this stuff for step 1. Do this stuff for step 1.Do this stuff for step 1.");
            step2.put("Step 2", "Do this stuff for step 2 of " + name + ". Do this stuff for step 2.Do this stuff for step 2. Do this stuff for step 2.Do this stuff for step 2.");
            step3.put("Step 3", "Do this stuff for step 3 of " + name + ". Do this stuff for step 3.Do this stuff for step 3. Do this stuff for step 3.Do this stuff for step 3.");
            description.put(step1);
            description.put(step2);
            description.put(step3);
            exercise.put("description", description);

            //put images that go with Descriptions -- This set-up relies on the steps and the images having the same names
            //ToDo: Make sure Micaela knows that images and steps need to have the same name
            images.put("Step 1", "main/res/drawable/push_up_fig_1.png");
            images.put("Step 3", "main/res/drawable/push_up_fig_1.png");
            //descripImgs.put(images);
            exercise.put("descripImgs", images);

            //put reps, sets, and image path for an exercise
            exercise.put("tagLine", tagLine);
            exercise.put("reps", reps);
            exercise.put("sets", sets);
            exercise.put("coverImg", "main/res/drawable/push_up_fig_1.png");

            //Add the exercise to our output array
            this.example.put(exercise);

        } catch (JSONException e) {
            Log.d(TAG, "onClick: JSON exception!!!");
        }
    }

    //Getter
    public JSONArray getExample() {
        return this.example;
    }
}
