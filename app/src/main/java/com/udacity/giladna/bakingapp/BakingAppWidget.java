package com.udacity.giladna.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


import com.udacity.giladna.bakingapp.model.Ingredient;
import com.udacity.giladna.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;


public class BakingAppWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setRemoteAdapter(R.id.widget_lv, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateFromActivity(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int itemClicked, ArrayList<Recipe> recipes) {


        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.updated_widget_layout);

            List<Ingredient> ingredients = recipes.get(itemClicked).getIngredients();
            int i = 0;
            String data = "";
            for (Ingredient ingredient : ingredients) {
                data = data + (++i) + ". " + ingredient.getIngredient().substring(0, 1).toUpperCase() + ingredient.getIngredient().substring(1) + " \n";
            }

            views.setTextViewText(R.id.widget_title, recipes.get(itemClicked).getName() + " Ingredients");
            views.setTextViewText(R.id.widget_content, data);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

}

