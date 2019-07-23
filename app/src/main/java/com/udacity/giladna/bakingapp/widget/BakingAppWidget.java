package com.udacity.giladna.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


import com.udacity.giladna.bakingapp.MainActivity;
import com.udacity.giladna.bakingapp.R;
import com.udacity.giladna.bakingapp.model.Recipe;
import com.udacity.giladna.bakingapp.utilities.WidgetUtil;

public class BakingAppWidget extends AppWidgetProvider {


    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

            views.setTextViewText(R.id.appwidget_text, WidgetUtil.getWidgetTitle(context));

            views.setRemoteAdapter(R.id.widget_lv,
                    WidgetService.getIntent(context));

            views.setPendingIntentTemplate(R.id.widget_lv, pendingIntent);

            views.setOnClickPendingIntent(R.id.widget_parent_layout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidget(context, appWidgetManager, appWidgetIds);
    }

    public static  void sendWidgetUpdate(Context context, Recipe recipe) {
        //Update Widget
        WidgetUtil.setWidgetTitle(context, recipe.getName());
        WidgetUtil.setWidgetRecipeId(context, recipe.getId());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, BakingAppWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lv);

        BakingAppWidget.updateAppWidget(context, appWidgetManager, appWidgetIds);
    }
}
