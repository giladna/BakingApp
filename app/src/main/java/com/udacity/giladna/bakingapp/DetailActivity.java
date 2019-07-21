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


public class DetailActivity extends AppCompatActivity {

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


        ingredients = getIntent().getParcelableArrayListExtra("ingredients");
        String recipe = getIntent().getExtras().getString("recipe");
        step = getIntent().getExtras().getParcelable("step");
        position = getIntent().getExtras().getInt("position");
        stepsList = getIntent().getExtras().getParcelableArrayList("stepsList");


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
            bundle.putParcelable("step", step);
            bundle.putParcelableArrayList("ingredients", ingredients);
            bundle.putParcelableArrayList("stepsList", stepsList);
            bundle.putInt("position", position);

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
