package com.example.bakingapp.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.BakingApp;
import com.example.bakingapp.DB.RecipeRepository;
import com.example.bakingapp.Models.Ingredient;
import com.example.bakingapp.Models.Recipe;
import com.example.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppRemoteViewsFactory(getApplicationContext());
    }
}

class BakingAppRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static String RECIPE_ID="recipe_id";
    private static String RECIPE_NAME="recipe_name";
    private final Context mContext;
    private Recipe recipe;
    private int recipeID;
    private RecipeRepository recipeRepository;
    private SharedPreferences sharedPreferences;
    private List<Ingredient> ingredients = new ArrayList<>();

    public BakingAppRemoteViewsFactory (Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public void onDataSetChanged() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        recipeID = sharedPreferences.getInt(RECIPE_ID, 0);
        recipeRepository = new RecipeRepository(BakingApp.getContext());
        recipe = recipeRepository.getCurrentRecipe(recipeID);
        ingredients = recipe.getRecipeIngredients();
    }

    @Override
    public void onDestroy() {

        Log.e("service" ,  "Service destroyed");
        ingredients.clear();
    }

    @Override
    public int getCount() {
        if (ingredients == null) {
            return 0;
        } else {
            return ingredients.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        views.setTextViewText(R.id.widget_list_name, ingredients.get(position).getIngredientName());
        views.setTextViewText(R.id.widget_list_measure, ingredients.get(position).getIngredientMeasure());
        views.setTextViewText(R.id.widget_list_quantity, String.valueOf(ingredients.get(position).getIngredientQuantity()));

        return views;
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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}