/*
CustomWorkoutListAdapter is a class to create the custom ArrayAdapter necessary for our scrollable
list of exercises. It takes the xml and turns it into actual objects.
 */
package com.example.micaela.fwd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.BitSet;
import java.util.List;

public class CustomWorkoutListAdapter extends ArrayAdapter {
    private static final String TAG = "CustomWorkoutListAdapter";
    private final LayoutInflater layoutInflater;
    private List<ExerciseListObject> exercises;
    private final int layoutResource;

    //Constructor
    public CustomWorkoutListAdapter(Context context, int layoutResource, List<ExerciseListObject> exercises) {
        super(context, layoutResource); //Calling the super constructor!
        this.layoutResource = layoutResource;
        this.layoutInflater = LayoutInflater.from(context);
        this.exercises = exercises;
    }

    //Method to return the number of items in the list (going to be called by listview)
    @Override
    public int getCount() {
        return exercises.size();
    }

    //Method to use layout inflater and turn xml into a View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Going to have this use viewholder
        ViewHolder viewHolder;
        //Want a thumbnail of our image to display in this list
        //Bitmap bmThumbnail;

        //Conditional: only inflate new view if they aren't any leftover view
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ExerciseListObject currentExercise = exercises.get(position);
        viewHolder.exerciseName.setText(currentExercise.getName());
        viewHolder.exerciseTagLine.setText(currentExercise.getTagLine());
        //ToDo Change this so that the image isn't static
        viewHolder.exerciseCoverPhoto.setImageResource(R.drawable.push_up_fig_1);

        //Deal with the thumbnail image
        //bmThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentExercise.getImg()), 120, 72);
        //viewHolder.exerciseCoverPhoto.setImageBitmap(bmThumbnail);

        return convertView;
    }

    //Class to hold the Viewtypes that get used repeatedly in this List
    private class ViewHolder{
        final TextView exerciseName;
        final ImageView exerciseCoverPhoto; //Thumbnail photo
        final TextView exerciseTagLine;  //Tagline to go in the list (Ex. duration or reps )

        ViewHolder(View v) {
            this.exerciseName =  v.findViewById(R.id.exerciseName);
            this.exerciseCoverPhoto =  v.findViewById(R.id.exerciseCoverPhoto);
            this.exerciseTagLine =  v.findViewById(R.id.exerciseTagLine);
        }
    }

}
