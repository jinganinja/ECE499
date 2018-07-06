package com.example.micaela.fwd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class CustomWorkout extends AppCompatActivity {

    private TextView mDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout);

        //Showing an example of content passed into page no. 2
        mDisplayText = (TextView) findViewById(R.id.tv_display);
    }

    public JSONObject shuffle(JSONObject unwantedExercise, JSONArray workout) {
        try {
            String type = unwantedExercise.getString("muscle group");
            JSONParser parser = new JSONParser();
            JSONArray allExercises = (JSONArray) parser.parse(new FileReader("/main/json/exercises.json"));

            for (int i = 0; i < workout.length(); i++) {
                // remove all the current workout exercises from the potential exercises to shuffle between
                for (int j = 0; j < allExercises.length(); j++) {
                    if (workout.get(i) == allExercises.get(j))
                        allExercises.remove(j);
                }
            }
            // remove all the exercises that aren't the same type as the exercises being removed
            for (int i = 0; i < allExercises.length(); i++) {
                if (!allExercises.get(i).equals(type))
                    allExercises.remove(i);
            }

            return randomExercisesPicker(allExercises);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject randomExercisesPicker(JSONArray allExercises) {
        int randomNumber = generateRandomNumber(allExercises.length()-1);
        try {
            return (JSONObject) allExercises.get(randomNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int generateRandomNumber(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }
}
