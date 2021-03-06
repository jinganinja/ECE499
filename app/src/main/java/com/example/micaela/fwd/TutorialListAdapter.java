package com.example.micaela.fwd;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TutorialListAdapter extends ArrayAdapter {

    private static final String TAG = "TutorialListAdapter";
    private final LayoutInflater layoutInflater;
    private final List<String> description;
    private final List<String> descripImgs;
    private final int layoutResource;

    //Constructor
    public TutorialListAdapter(Context context, int layoutResource, List<String> description, List<String> descripImgs) {
        super(context, layoutResource);
        this.layoutResource = layoutResource;
        this.layoutInflater = LayoutInflater.from(context);
        this.description = description;
        this.descripImgs = descripImgs;
    }

    //Method to return the number of items in the list (Number of Steps)
    @Override
    public int getCount() {
        return description.size();
    }

    //Method to use layout inflater and turn xml into a View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        //Conditional: only inflate new view if they aren't any leftover view
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Load the step Title
        viewHolder.stepTitle.setText("Step "+(position+1));

        //Load the Step Instruction
        String currentStep = description.get(position);
        viewHolder.stepInstruct.setText(currentStep);

        //Load the image if it is there
        if (descripImgs.get(position) != null) {
            //If there is an image, I want to add padding to it
            float scale = getContext().getResources().getDisplayMetrics().density;
            int dpPad = (int) (12*scale + 0.5f);
            viewHolder.stepFigure.setPadding(dpPad,0,dpPad,dpPad);
            String stepImagePath = descripImgs.get(position);

            //Changed this so that is actually reading the path
            try {
                AssetManager assetManager = getContext().getAssets();
                InputStream is = assetManager.open(stepImagePath);
                Bitmap image = BitmapFactory.decodeStream(is);
                image = CustomWorkoutListAdapter.RotateBitmap(image, 90);
                viewHolder.stepFigure.setImageBitmap(image);
            } catch (IOException e) {
                Log.e(TAG, "getView: Error reading image filepath!!!!!");
            }
        }
        return convertView;
    }

    //Class to hold the Viewtypes that get used repeatedly in this List
    private class ViewHolder {
        final TextView stepTitle; //Step 1, step 2, etc.
        final ImageView stepFigure;
        final TextView stepInstruct;

        ViewHolder(View v) {
            this.stepTitle = v.findViewById(R.id.stepTitle);
            this.stepFigure = v.findViewById(R.id.stepFigure);
            this.stepInstruct = v.findViewById(R.id.stepInstructions);
        }
    }
}



