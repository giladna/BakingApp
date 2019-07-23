package com.udacity.giladna.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.udacity.giladna.bakingapp.databinding.ActivityMainBinding;
import com.udacity.giladna.bakingapp.db.AppDatabase;
import com.udacity.giladna.bakingapp.model.Recipe;

import com.udacity.giladna.bakingapp.model.RecipeView;
import com.udacity.giladna.bakingapp.test.SimpleIdlingResource;
import com.udacity.giladna.bakingapp.ui.ClickCallback;
import com.udacity.giladna.bakingapp.ui.MainAdapter;
import com.udacity.giladna.bakingapp.ui.MainItemDecoration;
import com.udacity.giladna.bakingapp.utilities.NetworkClient;
import com.udacity.giladna.bakingapp.utilities.RecipesAPI;
import com.udacity.giladna.bakingapp.utilities.WidgetUtil;
import com.udacity.giladna.bakingapp.widget.BakingAppWidget;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.udacity.giladna.bakingapp.RecipeActivity.INTENT_RECIPE;


public class MainActivity extends AppCompatActivity implements ClickCallback<Recipe> {

    private MainAdapter mAdapter;
    private ActivityMainBinding binding;
    @Nullable private SimpleIdlingResource mIdlingResource;
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.activityMainProgressBar.setVisibility(View.VISIBLE);
        binding.recipesList.setHasFixedSize(true);
        binding.recipesList.addItemDecoration(new MainItemDecoration(MainActivity.this));

        mAdapter = new MainAdapter(MainActivity.this,MainActivity.this);
        binding.recipesList.setAdapter(mAdapter);
        getIdlingResource();
        if (mIdlingResource != null){
            mIdlingResource.setIdleState(false);
        }
        appDatabase = AppDatabase.getAppDatabase(this);
        fetchRecpiesAndStart();
    }

    private void fetchRecpiesAndStart() {

        Retrofit retrofit = NetworkClient.getRetrofitClient();

        RecipesAPI recipesAPI = retrofit.create(RecipesAPI.class);
        Call<List<Recipe>> call = recipesAPI.getRecipes();

        if (call == null) {
            return;
        }

        call.enqueue(new retrofit2.Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call,
                                   Response<List<Recipe>> response) {

                if (response.body() != null) {
                    binding.activityMainProgressBar.setVisibility(View.GONE);
                    final List<Recipe> recipesList =  response.body();

                    if (recipesList != null) {
                        mAdapter.setList(recipesList);
                        final Executor executor = Executors.newFixedThreadPool(2);
                        executor.execute(() -> {
                            List<RecipeView> recipeViews = appDatabase.recipeDao().getRecipes();
                            if (recipeViews.size() != recipesList.size()) {
                                appDatabase.recipeDao().insertRecipes(recipesList);
                            }
                        });

                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                    } else {
                        showErrorMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable exception) {
                showErrorMessage();
            }

        });
        binding.activityMainProgressBar.setVisibility(View.VISIBLE);

    }
    private void showErrorMessage() {
        binding.activityMainProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(Recipe recipe) {
        BakingAppWidget.sendWidgetUpdate(this, recipe);
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(INTENT_RECIPE, recipe);
        startActivity(intent);
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
