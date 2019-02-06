package com.example.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.Models.Ingredient;
import com.example.bakingapp.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Ingredient> ingredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients){
        this.mLayoutInflater = LayoutInflater.from(context);
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.ingredient_list_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        String ingredientName = ingredient.getIngredientName();
        String ingredientMeasurement  = ingredient.getIngredientMeasure();
        String ingredientQuantity = String.valueOf(ingredient.getIngredientQuantity());

        holder.ingredientName.setText(ingredientName);
        holder.ingredientMeasure.setText(ingredientMeasurement);
        holder.ingredientQuantity.setText(ingredientQuantity);

    }

    @Override
    public int getItemCount()  {
        return (null != ingredients ? ingredients.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ingredientName;
        final TextView ingredientMeasure;
        final TextView ingredientQuantity;

        private ViewHolder(View ingredientView) {
            super(ingredientView);
            ingredientName= ingredientView.findViewById(R.id.ingredient_name);
            ingredientMeasure = ingredientView.findViewById(R.id.ingredient_measurement);
            ingredientQuantity = ingredientView.findViewById(R.id.ingredient_quantity);
        }
    }
}
