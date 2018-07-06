/*Class so that each exercise in the selected custom workout can be stored as an object for
* easy access in the UI
* */

package com.example.micaela.fwd;

import org.json.JSONArray;

public class ExerciseListObject {
    private String name;
    private String tagLine;
    private String img;
    private JSONArray description; //ToDo Deal with description as a JSON array (and images associated for each step?)
    private String reps;
    private String sets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public JSONArray getDescription() {
        return description;
    }

    public void setDescription(JSONArray description) {
        this.description = description;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

}
