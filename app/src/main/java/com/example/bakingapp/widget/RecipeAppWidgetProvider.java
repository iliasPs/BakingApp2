package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.bakingapp.ItemListActivity;
import com.example.bakingapp.R;
import com.example.bakingapp.RecipeDetailFragment;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidgetProvider extends AppWidgetProvider {

    private static String RECIPE_NAME="recipe_name";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //get recipe from preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = sharedPreferences.getString(RECIPE_NAME, "BakingApp");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.gridview_for_widget);
        views.setTextViewText(R.id.recipe_title, recipeName);

        //intent to for gridview
        Intent intent = new Intent(context, RecipeService.class);
        views.setRemoteAdapter( R.id.ingredient_grid_view, intent);
        //set the intent to open activity
        Intent appIntent = new Intent(context, ItemListActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
        views.setPendingIntentTemplate(R.id.ingredient_grid_view, appPendingIntent);
        //set emptyview
        views.setEmptyView(R.id.ingredient_grid_view, R.id.empty_view);
        //click handler to only launch pending intents
       views.setOnClickPendingIntent(R.id.recipe_title, appPendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredient_grid_view);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)){
            int[] ids = intent.getExtras().getIntArray(AppWidgetManager.EXTRA_APPWIDGET_ID);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        }else
        super.onReceive(context, intent);
    }
}

