package com.example.bakingapp.DB;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.bakingapp.Adapters.RecipeAdapter;
import com.example.bakingapp.Models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeRepository {

    private static final String LOG_TAG = RecipeRepository.class.getSimpleName();

    private final RecipesDao recipesDao;


    public RecipeRepository(Application application){
        AppDatabase appDatabase =AppDatabase.getInstance(application);
        recipesDao = appDatabase.recipesDao();
    }


    public List<Recipe> getRecipes(RecipeAdapter recipeAdapter){
        try{
            return new GetRecipesAsync(recipesDao, recipeAdapter).execute().get();
        }catch (ExecutionException e){
            e.printStackTrace();
            return null;
        }catch (InterruptedException e){
            Log.e(LOG_TAG, "get Recipes interrupted", e);
            return null;
        }
    }


    private static class GetRecipesAsync extends AsyncTask<Void, Void, List<Recipe>> {

        private final RecipesDao mRecipesDao;
        private final RecipeAdapter mAdapter;

        GetRecipesAsync(RecipesDao recipesDao, RecipeAdapter adapter){
            this.mRecipesDao = recipesDao;
            this.mAdapter = adapter;
        }


        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            mAdapter.setRecipes(mRecipesDao.loadAllRecipes());
            return mRecipesDao.loadAllRecipes();
        }
    }


    public Recipe getCurrentRecipe(int id) {
        try {
            return new CheckDbAsync(recipesDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "getSingleRecipe: Interruption", e);
            return null;
        }
    }


    private static class CheckDbAsync extends AsyncTask<Integer, Void, Recipe> {
        private final RecipesDao recipesDao;

        CheckDbAsync(RecipesDao recipeDao) {
            this.recipesDao = recipeDao;
        }

        @Override
        protected Recipe doInBackground(Integer... integers) {
            int currentRecipe = integers[0];
            return recipesDao.getSingleRecipe(currentRecipe);
        }
    }


    public void insertRecipe(Recipe recipe){
        new FillDbAsync(recipesDao).execute(recipe);
    }

    private static class FillDbAsync extends AsyncTask<Recipe, Void, Void>{
        private final RecipesDao recipesDao;

        FillDbAsync(RecipesDao recipesDao){this.recipesDao = recipesDao;}

        @Override
        protected Void doInBackground(Recipe... recipes) {

            Recipe currentRecipe = recipes[0];

            Log.d(LOG_TAG, "mymessage  - recipe loading " + currentRecipe);
            recipesDao.insertRecipe(currentRecipe);
            return null;
        }
    }

}