package com.example.bakingapp.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "recipesTable")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int recipeId;
    private String recipeName;
    private List<Ingredient> recipeIngredients = null;
    private List<Step> recipeSteps = null;
    private int recipeServings;
    private String recipeImage;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<Ingredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<Ingredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<Step> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<Step> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public int getRecipeServings() {
        return recipeServings;
    }

    public void setRecipeServings(int recipeServings) {
        this.recipeServings = recipeServings;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }
}