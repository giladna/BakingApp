package com.udacity.giladna.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.giladna.bakingapp.model.Recipe;

import com.udacity.giladna.bakingapp.utilities.NetworkClient;
import com.udacity.giladna.bakingapp.utilities.RecipesAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchRecpiesAndStart();

    }

    private void fetchRecpiesAndStart() {
        Retrofit retrofit = NetworkClient.getRetrofitClient();

        RecipesAPI recipesAPI = retrofit.create(RecipesAPI.class);
        Call<List<Recipe>> call = recipesAPI.getRecipes();

        if (call == null) {
            return;
        }
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        call.enqueue(new retrofit2.Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call,
                                   Response<List<Recipe>> response) {

                if (response.body() != null) {
                    final List<Recipe> recipesList =  response.body();
                    if (mLoadingIndicator != null) {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                    }
                    if (recipesList != null) {

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

    }
    private void showErrorMessage() {
        /* First, hide the currently visible data */
//        mRecyclerView.setVisibility(View.INVISIBLE);
//        /* Then, show the error */
//        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


}
