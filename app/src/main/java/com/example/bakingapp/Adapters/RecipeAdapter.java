package com.example.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.Models.Recipe;
import com.example.bakingapp.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipesViewHolder> {
    private List<Recipe> mRecipes;
    private Context mContext;
    final private RecipeClickListener recipeClickListener;

    public RecipeAdapter(Context c, List<Recipe> recipes, RecipeClickListener recipeClickListener, Boolean mTwoPane) {
        this.recipeClickListener = recipeClickListener;
        Boolean mTwoPanel = mTwoPane;
        mContext = c;
        this.mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_card;
        LayoutInflater inflater = LayoutInflater.from(c);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipesViewHolder viewHolder = new RecipesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipe recipe = this.mRecipes.get(position);
        holder.recipeNameTv.setText(recipe.getRecipeName());
        holder.recipeServingsTv.setText("Servings " + String.valueOf(recipe.getRecipeServings()));
    }

    @Override
    public int getItemCount() {
        return (null != mRecipes ? mRecipes.size() : 0);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class RecipesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView recipeNameTv;
        TextView recipeServingsTv;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            recipeNameTv = itemView.findViewById(R.id.recipe_name);
            recipeServingsTv = itemView.findViewById(R.id.recipe_servings);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe currentRecipe = mRecipes.get(getAdapterPosition());
            recipeClickListener.onClick(v, currentRecipe);
        }
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setRecipes(List<Recipe> recipeEntries) {
        mRecipes = recipeEntries;
        notifyDataSetChanged();
    }

    public interface RecipeClickListener {

        void onClick(View view, Recipe recipe);

    }
}

