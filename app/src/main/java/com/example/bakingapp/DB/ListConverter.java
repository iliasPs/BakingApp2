package com.example.bakingapp.DB;

import android.arch.persistence.room.TypeConverter;

import com.example.bakingapp.Models.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {

    Gson gson = new Gson();

    @TypeConverter
    public String stepsFromListToString(List<Step> steps){
        if(steps == null){
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>(){}.getType();

        String json = gson.toJson(steps, type);
        return json;
    }


    @TypeConverter
    public List<Step> stepsToListFromString (String stepsJson) {
        if(stepsJson == null){
            return null;}


        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {}.getType();

        List<Step> convertedListFromJson  = gson.fromJson(stepsJson, type);
        return convertedListFromJson;
    }





}
