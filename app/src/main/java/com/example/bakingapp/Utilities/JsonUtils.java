package com.example.bakingapp.Utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.bakingapp.DB.RecipeRepository;
import com.example.bakingapp.Models.Ingredient;
import com.example.bakingapp.Models.Recipe;
import com.example.bakingapp.Models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {


    public static void extractFeatureFromJsonToRoom(String recipeJson, RecipeRepository recipeRepository) {
        if (TextUtils.isEmpty(recipeJson)) {
        }

        List<Ingredient> ingredients;
        List<Step> steps;

        try {

            JSONArray baseJsonResponse = new JSONArray(recipeJson);

            for(int i = 0; i<baseJsonResponse.length(); i++) {
                Recipe currentRecipe = new Recipe();
                ingredients = new ArrayList<>();
                steps = new ArrayList<>();

                JSONObject recipeJsonObject = baseJsonResponse.getJSONObject(i);

                String currentRecipeName = recipeJsonObject.getString("name");
                int currentRecipeId = recipeJsonObject.getInt("id");
                int currentRecipeServings = recipeJsonObject.getInt("servings");
                String currentRecipeImage = recipeJsonObject.getString("image");

                JSONArray ingredientsArray = recipeJsonObject.getJSONArray("ingredients");

                for (int x = 0; x < ingredientsArray.length(); x++) {

                    JSONObject currentIngredientsJson = ingredientsArray.getJSONObject(x);
                    Ingredient currentIngredient = new Ingredient();
                    currentIngredient.setIngredientName(currentIngredientsJson.getString("ingredient"));
                    currentIngredient.setIngredientQuantity(currentIngredientsJson.getDouble("quantity"));
                    currentIngredient.setIngredientMeasure(currentIngredientsJson.getString("measure"));
                    ingredients.add(currentIngredient);
                }
                JSONArray stepsArray = recipeJsonObject.getJSONArray("steps");
                for (int y = 0; y < stepsArray.length(); y++) {

                    JSONObject currentStepsJson = stepsArray.getJSONObject(y);

                    Step currentStep = new Step();
                    currentStep.setStepId(currentStepsJson.getInt("id"));
                    currentStep.setStepShortDescription(currentStepsJson.getString("shortDescription"));
                    currentStep.setStepDescription(currentStepsJson.getString("description"));
                    currentStep.setStepVideoUrl(currentStepsJson.getString("videoURL"));
                    currentStep.setStepThumbVideo(currentStepsJson.getString("thumbnailURL"));
                    steps.add(currentStep);
                }

                currentRecipe.setRecipeId(currentRecipeId);
                currentRecipe.setRecipeName(currentRecipeName);
                currentRecipe.setRecipeServings(currentRecipeServings);
                currentRecipe.setRecipeImage(currentRecipeImage);
                currentRecipe.setRecipeIngredients(ingredients);
                currentRecipe.setRecipeSteps(steps);
                recipeRepository.insertRecipe(currentRecipe);

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
    }






    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
