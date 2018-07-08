/*
This class parses the JSON received from backed into native format to be easily passed from
Activities in a bundle, and for easy access.
 */

package com.example.micaela.fwd;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
            Log.d(TAG, "onCreate: Error retrieving the JSON array of generated exercisesJSON!");
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
                Log.d(TAG, "JSONArrayToArrayList: " + exerciseJSONObj.toString());
                Log.d(TAG, "JSONArrayToArrayList: successfully created a JSON object from the String ");

                //Set the fields of the ExerciseListObject
                Log.d(TAG, "JSONArrayToArrayList: " + exerciseJSONObj.get("name").toString());
                exerciseObj.setName(exerciseJSONObj.get("name").toString());
                exerciseObj.setTagLine(exerciseJSONObj.get("tagLine").toString());
                exerciseObj.setImg(exerciseJSONObj.get("coverImg").toString());
                exerciseObj.setReps(exerciseJSONObj.get("reps").toString());
                exerciseObj.setSets(exerciseJSONObj.get("sets").toString());

                //TODO: Address whether or not this fits the final layout of instructions and images
                /*The Steps are difficult to deal with. Here I am making two arrayLists: One for the steps
                and one for the images. If there is no corresponding image for the step, the the list has a
                value of "" in that position.
                 */

                JSONArray descriptionArrayJSON = (JSONArray) exerciseJSONObj.get("description");
                //Get description in a List<String> and set the description of the current exerciseObj
                exerciseObj.setDescription(getDescriptionfromJSON(descriptionArrayJSON));

                JSONObject descripImgsObjectJSON = (JSONObject) exerciseJSONObj.get("descripImgs");
                //Get description images in a List<String> too
                exerciseObj.setDescripImgs(getImagesfromJSON(descripImgsObjectJSON, descriptionArrayJSON));

                Log.d(TAG, "JSONArrayToArrayList: Sucessfully turned the JSON objects into ExerciseListObject Objects");
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
                Log.d(TAG, "getDescription: HERE IS MY KEY:"+keyValue);
                Log.d(TAG, "getDescription: HERE IS MY VALUE:"+currentObj.getString(keyValue));
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
