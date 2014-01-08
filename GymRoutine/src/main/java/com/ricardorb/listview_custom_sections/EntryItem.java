package com.ricardorb.listview_custom_sections;


public class EntryItem implements Item {

    public final String title;
    public final String subtitle;
    public final int numMuscle;
    public final int numExercise;

    public EntryItem(String title, String subtitle, int numMuscle, int numExercise) {
        this.title = title;
        this.subtitle = subtitle;
        this.numMuscle = numMuscle;
        this.numExercise = numExercise;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    public int getNumMuscle() {
        return this.numMuscle;
    }

    public int getNumExercise(){return  this.numExercise;}

}
