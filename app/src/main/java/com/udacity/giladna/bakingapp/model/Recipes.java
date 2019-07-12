package com.udacity.giladna.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public  class Recipes implements Parcelable {

    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.recipes);
    }

    public Recipes() {
    }

    protected Recipes(Parcel in) {
        this.recipes = in.createTypedArrayList(Recipe.CREATOR);
    }

    public static final Parcelable.Creator<Recipes> CREATOR = new Parcelable.Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel source) {
            return new Recipes(source);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };
}