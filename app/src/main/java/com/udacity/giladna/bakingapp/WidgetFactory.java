package com.udacity.giladna.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.giladna.bakingapp.model.Ingredient;
import com.udacity.giladna.bakingapp.model.Recipe;
import com.udacity.giladna.bakingapp.utilities.NetworkClient;
import com.udacity.giladna.bakingapp.utilities.RecipesAPI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Recipe> recipes;

    public WidgetFactory(Context applicationContext, Intent intent) {

        mContext = applicationContext;


    }

    @Override
    public void onCreate() {
        new AsyncClass().execute();
        SystemClock.sleep(2000);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipes != null) {
            return recipes.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Recipe recipe = recipes.get(position);
        List<Ingredient> ingredients = recipe.getIngredients();
        String data = "";
        int i = 0;
        for (Ingredient ingredient : ingredients) {
            data = data + (++i) + ". " + ingredient.getIngredient() + " \n";
        }


        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_lv_item);
        rv.setTextViewText(R.id.title, recipe.getName());
        rv.setTextViewText(R.id.data, data);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    class AsyncClass extends AsyncTask<Void, Void, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(Void... params) {
            List<Recipe> recipeList = null;

            Retrofit retrofit = NetworkClient.getRetrofitClient();
            RecipesAPI client = retrofit.create(RecipesAPI.class);
            Call<List<Recipe>> call = client.getRecipes();

            try {
                recipeList = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return recipeList;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipeList) {
            recipes = recipeList;
        }
    }
}
