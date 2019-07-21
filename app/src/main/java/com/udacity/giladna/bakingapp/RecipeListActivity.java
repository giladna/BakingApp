package com.udacity.giladna.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.udacity.giladna.bakingapp.model.Recipe;
import com.udacity.giladna.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeListActivity extends AppCompatActivity {


    private boolean mTwoPane;

    private Recipe recipe;
    private List<Step> steps;
    @BindView(R.id.ingred_card_view) CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        recipe = getIntent().getExtras().getParcelable("recipe");
        steps = recipe.getSteps();
        setTitle(recipe.getName());
        Toolbar t = findViewById(R.id.toolbar);
        setSupportActionBar(t);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cardView.setOnClickListener(v -> {


            if (mTwoPane) {
                DetailFragment fragment = new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) recipe.getIngredients());

                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();

            } else {

                Intent intent = new Intent(RecipeListActivity.this, DetailActivity.class);
                intent.putParcelableArrayListExtra("ingredients", (ArrayList<? extends Parcelable>) recipe.getIngredients());
                startActivity(intent);
            }
        });


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(steps));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {


        private final List<Step> mRecipeSteps;


        public SimpleItemRecyclerViewAdapter(List<Step> items) {
            mRecipeSteps = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.stepTV.setText("Step " + (position + 1) + " : " + mRecipeSteps.get(position).getShortDescription());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {

                        DetailFragment fragment = new DetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("step", steps.get(position));
                        bundle.putBoolean("twoPane", true);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_detail_container, fragment)
                                .commit();
                    } else {

                        Intent intent = new Intent(RecipeListActivity.this, DetailActivity.class);
                        intent.putExtra("recipe", getTitle());
                        intent.putExtra("step", mRecipeSteps.get(position));
                        intent.putExtra("position", position);
                        intent.putParcelableArrayListExtra("stepsList", (ArrayList<? extends Parcelable>) steps);
                        startActivity(intent);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return steps.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView stepTV;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                stepTV = (TextView) view.findViewById(R.id.recipe_steps_tv);

            }


        }
    }


}