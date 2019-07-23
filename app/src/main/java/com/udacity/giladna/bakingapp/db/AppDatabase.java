package com.udacity.giladna.bakingapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.udacity.giladna.bakingapp.model.Ingredient;
import com.udacity.giladna.bakingapp.model.Recipe;
import com.udacity.giladna.bakingapp.model.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String BAKINGAPP_DB = "bakingapp.db";
    private static AppDatabase DB_INSTANCE;


    public static AppDatabase getAppDatabase(Context context) {
        if (DB_INSTANCE == null) {
            DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, BAKINGAPP_DB).build();
        }
        return DB_INSTANCE;
    }

    public static void destroyInstance() {
        DB_INSTANCE = null;
    }

    abstract public RecipeDao recipeDao();
}
