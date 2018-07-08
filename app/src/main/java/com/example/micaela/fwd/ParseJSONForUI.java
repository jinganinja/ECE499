/*
This class parses the JSON received from backed into native format to be easily displayed in ListView
and for easy front-end access.
 */

package com.example.micaela.fwd;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
            Log.e(TAG, "onCreate: Error retrieving the JSON array of generated exercisesJSON!");
        }
    }

    private void JSONArrayToArrayList(JSONArray exercisesJSONArray) {
        ArrayList<JSONObject> exercisesJSONObjects;

        //Loop through each item in the JSONArray and feed it into my ListArray
        for (int i = 0; i < exercisesJSONArray.length(); i++) {
            try {
                //Create a temporary ExerciseListObject and JSONObject to hold the current item in array
                ExerciseListObject exerciseObj = new ExerciseListObject();
                JSONObject exerciseJSONObj = exercisesJSONArray.getJSONObject(i);

                //Set the fields of the ExerciseListObject
                exerciseObj.setName(exerciseJSONObj.get("name").toString());
                exerciseObj.setTagLine(exerciseJSONObj.get("tagLine").toString());
                exerciseObj.setImg(exerciseJSONObj.get("coverImg").toString());
                exerciseObj.setReps(exerciseJSONObj.get("reps").toString());
                exerciseObj.setSets(exerciseJSONObj.get("sets").toString());

                //Get description in a List<String> and set the description of the current exerciseObj
                JSONArray descriptionArrayJSON = (JSONArray) exerciseJSONObj.get("description");
                exerciseObj.setDescription(getDescriptionfromJSON(descriptionArrayJSON));

                //Get description images in a List<String> too
                JSONObject descripImgsObjectJSON = (JSONObject) exerciseJSONObj.get("descripImgs");
                exerciseObj.setDescripImgs(getImagesfromJSON(descripImgsObjectJSON, descriptionArrayJSON));

                //Add each ExerciseListObject to my ArrayList
                this.exercises.add(exerciseObj);

            } catch (JSONException e) {
                Log.e(TAG, "JSONArrayToArrayList: Error Converting JSONArray to ArrayList!");
            }
        }
    }

    public List<String> getDescriptionfromJSON(JSONArray descriptionArray) {
        //Loop through the items and put them into a list (Need an ordered data type to display in ListView)
        List<String> description = new ArrayList<String>();
        for (int i = 0; i < descriptionArray.length(); i++) {
            //Retrieve each Object from the array, take that one object and put its value into list as String
            try {
                JSONObject currentObj;
                currentObj = descriptionArray.getJSONObject(i);
                //Get the value of the first object
                String keyValue = getKeyIdentifier(currentObj);
                description.add(currentObj.getString(keyValue));
            } catch (JSONException e) {
                Log.e(TAG, "getDescription: Error reading description from JSON!");
            }
        }//End of for loop
        return description;
    }

    public List<String> getImagesfromJSON(JSONObject descripImgsObject,JSONArray descriptionArray ) {
        List<String> descripImgs = new ArrayList<String>();
        for (int i = 0; i < descriptionArray.length(); i++) {
            //Retrieve each Object from the array, take that one object and put its value into list as String
            try {
                JSONObject currentObj;
                currentObj = descriptionArray.getJSONObject(i);
                //Get the value of the first object
                String descripKey = getKeyIdentifier(currentObj);

                //Loop through all the image keys looking for one that matches the description key
                Iterator imageKeys = descripImgsObject.keys();
                boolean foundImage = false;
                while (imageKeys.hasNext()) {
                    String imageKey = (String) imageKeys.next();
                    //If the description and the image both have the same key...
                    if (imageKey == descripKey) {
                        descripImgs.add(descripImgsObject.getString(imageKey));
                        foundImage = true;
                    }
                }
                if (foundImage == false) descripImgs.add(null);
            } catch (JSONException e) {
                Log.e(TAG, "getDescription: Error reading description from JSON!");
            }
        }//End of for loop
        return descripImgs;
    }

    //getKeyIdentifier only appropirate for use on description JSONArray where each Object contains only
    //ONE key-value pair
    public String getKeyIdentifier (JSONObject currentObject){
        Iterator<String> keys = currentObject.keys();
        String keyValue = (String) keys.next(); //Only need to do this once because only one key-value pair here!
        return keyValue;
    }

    //Returns the information that was in the JSONArray in the format of a list of ExerciseListObjects
    public ArrayList<ExerciseListObject> getExercises() {
        return exercises;
    }
}
