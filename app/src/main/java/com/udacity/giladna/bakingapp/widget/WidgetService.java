package com.udacity.giladna.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.udacity.giladna.bakingapp.widget.WidgetFactory;


public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(this.getApplicationContext(), intent);
    }
}
