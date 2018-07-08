/*
This class parses the JSON received from backed into native format to be easily passed from
Activities in a bundle, and for easy access.
 */

package com.example.micaela.fwd;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ParseJSONForUI {
    private static final String TAG = "ParseJSONForUI";
    private ArrayList<ExerciseListObject> exercises;

    //Constructor
    public ParseJSONForUI(String exercisesJSONString) {

        this.exercises = new ArrayList<>();

        //Turn the String passed in the bundle back into JSON Array
        try {
            JSONArray exercisesJSON = new JSONArray(exercisesJSONString);
            //Call function to Convert JSON array to ArrayList
            JSONArrayToArrayList(exercisesJSON);

        } catch (JSONException e) {
            Log.d(TAG, "onCreate: Error retrieving the JSON array of generated exercisesJSON!");
        }
    }

    private void JSONArrayToArrayList(JSONArray exercisesJSONArray) {
        ArrayList<JSONObject> exercisesJSONObjects = null;

        //Loop through each item in the JSONArray and feed it into my ListArray
        for (int i = 0; i < exercisesJSONArray.length(); i++) {
            try {
                //Create a temporary ExerciseListObject and JSONObject to hold the current item in array
                ExerciseListObject exerciseObj = new ExerciseListObject();
                JSONObject exerciseJSONObj = exercisesJSONArray.getJSONObject(i);
                Log.d(TAG, "JSONArrayToArrayList: "+exerciseJSONObj.toString());
                Log.d(TAG, "JSONArrayToArrayList: successfully created a JSON object from the String ");

                //Set the fields of the ExerciseListObject
                Log.d(TAG, "JSONArrayToArrayList: " + exerciseJSONObj.get("name").toString());
                exerciseObj.setName(exerciseJSONObj.get("name").toString());
                exerciseObj.setTagLine(exerciseJSONObj.get("tagLine").toString());
                exerciseObj.setImg(exerciseJSONObj.get("img").toString());
                //Todo: Add the description here -- figure out how to deal with the steps
                exerciseObj.setReps(exerciseJSONObj.get("reps").toString());
                exerciseObj.setSets(exerciseJSONObj.get("sets").toString());

                Log.d(TAG, "JSONArrayToArrayList: Sucessfully turned the JSON objects into ExerciseListObject Objects");
                //Add each ExerciseListObject to my ArrayList
                this.exercises.add(exerciseObj);

            } catch (JSONException e) {
                Log.e(TAG, "JSONArrayToArrayList: Error Converting JSONArray to ArrayList!");
            }
        }
    }

    //Returns the information that was in the JSONArray in the format of a list of ExerciseListObjects
    public ArrayList<ExerciseListObject> getExercises() {
        return exercises;
    }

}
