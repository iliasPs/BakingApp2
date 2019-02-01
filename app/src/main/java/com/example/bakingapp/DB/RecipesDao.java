package com.example.bakingapp.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.bakingapp.Models.Recipe;

import java.util.List;

@Dao
public interface RecipesDao {


    @Query("SELECT * FROM recipesTable ORDER BY recipeId")
    List<Recipe> loadAllRecipes();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    @Query("SELECT recipeName FROM recipesTable WHERE recipeName = :recipeName")
    LiveData<String> searchRecipesByName(String recipeName);

    @Query("SELECT * FROM recipesTable WHERE recipeId = :id")
    Recipe getSingleRecipe(int id);


}
