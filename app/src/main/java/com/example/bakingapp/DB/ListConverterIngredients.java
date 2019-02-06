package com.example.bakingapp.DB;

import android.arch.persistence.room.TypeConverter;

import com.example.bakingapp.Models.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverterIngredients {

    @TypeConverter
    public String ingredientsFromListToString(List<Ingredient> ingredients){
        if(ingredients == null){
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>(){}.getType();

        String json = gson.toJson(ingredients, type);
        return json;
    }

    @TypeConverter
    public List<Ingredient> ingredientsToListFromString (String stepsJson) {
        if(stepsJson == null){
            return null;}

        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();

        List<Ingredient> convertedListFromJson  = gson.fromJson(stepsJson, type);
        return convertedListFromJson;
    }
}