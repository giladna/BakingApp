package com.udacity.giladna.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


import com.udacity.giladna.bakingapp.R;
import com.udacity.giladna.bakingapp.model.Ingredient;
import com.udacity.giladna.bakingapp.model.Recipe;

import java.util.List;
import java.util.Locale;


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

    public static void updateFromActivity(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {


        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

            List<Ingredient> ingredients = recipe.getIngredients();
            int i = 0;
            String data = "";

            for (Ingredient ingredient : ingredients) {
                data = data + (++i) + ") " + String.format(Locale.getDefault(), "%.1f %s %s", ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient()) + " \n";
            }
//
            views.setTextViewText(R.id.title, recipe.getName());
            views.setTextViewText(R.id.data, data);
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

