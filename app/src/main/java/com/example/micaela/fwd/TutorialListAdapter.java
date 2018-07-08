package com.example.micaela.fwd;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TutorialListAdapter extends ArrayAdapter {

    private static final String TAG = "TutorialListAdapter";
    private final LayoutInflater layoutInflater;
    private final List<String> description;
    private final List<String> descripImgs;
    private final int layoutResource;

    //Constructor
    public TutorialListAdapter(Context context, int layoutResource, List<String> description, List<String> descripImgs) {
        super(context, layoutResource); //Calling the super constructor!
        Log.d(TAG, "TutorialListAdapter: MAKE IT TO THE CONSTRUCTOR");
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
            //todo: change this so that it is actually reading file path!
            viewHolder.stepFigure.setImageResource(R.drawable.push_up_fig_1);
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



