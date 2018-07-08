/*Class so that each exercise in the selected custom workout can be stored as an object for
* easy access in the UI
* */

package com.example.micaela.fwd;

import java.util.List;

public class ExerciseListObject {
    private String name;
    private String tagLine;
    private String img;
    private List<String> description;
    private List<String> descripImgs;
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

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getDescripImgs() {
        return descripImgs;
    }

    public void setDescripImgs(List<String> descripImgs) {
        this.descripImgs = descripImgs;
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
