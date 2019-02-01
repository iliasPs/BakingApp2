package com.example.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bakingapp.Adapters.RecipeAdapter;
import com.example.bakingapp.DB.RecipeRepository;
import com.example.bakingapp.Models.Recipe;
import com.example.bakingapp.Utilities.JsonUtils;
import com.example.bakingapp.Utilities.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickListener{


    final static private String BASE_RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Runnable saveDB;
    private static final String LOG_TAG = ItemListActivity.class.getSimpleName();
    private  RecipeRepository recipeRepository;
    private String jsonString;
    private static final String RECIPE_ID="recipe_id";
    public static final String ARG_BOOLEAN = "boolean_two_pane";
    private List<Recipe> mRecipes;
    private RecipeAdapter mAdapter;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        recipeRepository = new RecipeRepository(getApplication());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        runnables(recipeRepository);
        new Handler().postDelayed(saveDB, 0);
        View recyclerView = findViewById(R.id.recipeRV);
        assert recyclerView != null;
        setRv((RecyclerView) recyclerView);

    }

    private void setRv(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mAdapter = new RecipeAdapter(this, mRecipes, this, mTwoPane);
        mRecipes = recipeRepository.getRecipes(mAdapter);
        Log.d(LOG_TAG, "mymessage Recipes are " + mRecipes);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.notifyDataSetChanged();
    }


    private void runnables(final RecipeRepository recipeRepository) {
        Log.d(LOG_TAG, "mymessage runnables called ");
        saveDB = new Runnable() {
            @Override
            public void run() {
                URL searchRecipeObjectUrl = createUrl();
                NetworkUtils networkUtils = new NetworkUtils();
                try {
                    jsonString = networkUtils.execute(searchRecipeObjectUrl.toString()).get();
                    Log.d(LOG_TAG, "mymessage Json response string " + jsonString);
                } catch (ExecutionException e) {
                    Log.e(LOG_TAG, "mymessage Problem making the HTTP request.", e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JsonUtils.extractFeatureFromJsonToRoom(jsonString, recipeRepository);
            }
        };

    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl() {
        Uri builtUri = Uri.parse(BASE_RECIPE_URL);
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    @Override
    public void onClick(View view, Recipe recipe) {

        if(mTwoPane){
            Bundle bundle = new Bundle();
            bundle.putInt(RECIPE_ID, recipe.getRecipeId());
            bundle.putBoolean(ARG_BOOLEAN, mTwoPane);
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, recipeDetailFragment).commit();
        }else{

            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(ItemListActivity.this, RecipeDetailsActivity.class);
            intent.putExtra(RECIPE_ID, recipe.getRecipeId());
            startActivity(intent);
        }

    }
}
