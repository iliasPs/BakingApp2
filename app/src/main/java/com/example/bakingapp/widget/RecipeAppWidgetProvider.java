package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.RecipeDetailFragment;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidgetProvider extends AppWidgetProvider {

    private static String RECIPE_ID="recipe_id";
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
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter( R.id.ingredient_grid_view, intent);

        //set the intent to open activity

        Intent appIntent = new Intent(context, RecipeDetailFragment.class);
        int recipeId = sharedPreferences.getInt(RECIPE_ID, -1);
        appIntent.putExtra(RECIPE_ID, recipeId);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.ingredient_grid_view, appPendingIntent);

        //set emptyview
        views.setEmptyView(R.id.ingredient_grid_view, R.id.empty_view);

        //click handler to only launch pending intents
        views.setOnClickPendingIntent(R.id.recipe_title, appPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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
}

