package com.udacity.giladna.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.udacity.giladna.bakingapp.model.Ingredient;
import com.udacity.giladna.bakingapp.model.Step;

import java.util.ArrayList;

import static com.udacity.giladna.bakingapp.DetailFragment.INTENT_POSITION;
import static com.udacity.giladna.bakingapp.DetailFragment.INTENT_STEP;
import static com.udacity.giladna.bakingapp.DetailFragment.INTENT_STEP_LIST;
import static com.udacity.giladna.bakingapp.RecipeActivity.INTENT_RECIPE;


public class DetailActivity extends AppCompatActivity {

    public static String INTENT_INGREDIENTS = "ingredients";

    private Step step;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> stepsList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        ingredients = getIntent().getParcelableArrayListExtra(INTENT_INGREDIENTS);
        String recipe = getIntent().getExtras().getString(INTENT_RECIPE);
        step = getIntent().getExtras().getParcelable(INTENT_STEP);
        position = getIntent().getExtras().getInt(INTENT_POSITION);
        stepsList = getIntent().getExtras().getParcelableArrayList(INTENT_STEP_LIST);


        if (ingredients != null) {
            setTitle("Ingredients");
        }
        if (step != null) {
            setTitle(recipe + " Steps");
        }


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            DetailFragment fragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(INTENT_STEP, step);
            bundle.putParcelableArrayList(INTENT_INGREDIENTS, ingredients);
            bundle.putParcelableArrayList(INTENT_STEP_LIST, stepsList);
            bundle.putInt(INTENT_POSITION, position);

            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, RecipeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
