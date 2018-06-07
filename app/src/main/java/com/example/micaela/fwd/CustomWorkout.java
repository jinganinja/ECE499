package com.example.micaela.fwd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CustomWorkout extends AppCompatActivity {

    private TextView mDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout);

        //Showing an example of content passed into page no. 2
        mDisplayText = (TextView) findViewById(R.id.exerciseTitle);
    }
}
