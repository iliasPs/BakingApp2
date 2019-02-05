package com.example.bakingapp;

import android.app.ActionBar;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.bakingapp.Adapters.IngredientsAdapter;
import com.example.bakingapp.Adapters.StepAdapter;
import com.example.bakingapp.DB.RecipeRepository;
import com.example.bakingapp.Models.Ingredient;
import com.example.bakingapp.Models.Recipe;
import com.example.bakingapp.Models.Step;
import com.example.bakingapp.Utilities.GlideApp;
import com.example.bakingapp.Utilities.GlideThumbnailTransformation;
import com.example.bakingapp.Utilities.SimpleDividerItemDecoration;
import com.example.bakingapp.widget.RecipeAppWidgetProvider;
import com.github.rubensousa.previewseekbar.PreviewLoader;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shuhart.stepview.StepView;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailsActivity}
 * on handsets.
 */



public class RecipeDetailFragment extends Fragment implements View.OnClickListener, StepView.OnStepClickListener, PreviewLoader, StepAdapter.StepClickListener {

    private RecyclerView ingredientsRV;
    private List<Step> stepList = new ArrayList<>();
    private StepView stepView;
    private Recipe recipe;
    private int savedStep;
    private TextView stepShortDescTv;
    private TextView stepLongDescTv;
    private TextView stepIdTv;
    private ImageView previewImageView;
    private SimpleExoPlayer exoPlayer;
    private PlayerView exoPlayerView;
    private int currentWindow;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private TrackSelection.Factory trackSelectionFactory;
    private TrackSelector trackSelector;
    private DataSource.Factory dataSourceFactory;
    private MediaSource mediaSource;
    private boolean playWhenReady;
    private boolean playState;
    private Uri videoUri;
    private long playbackPosition;
    private Boolean mTwoPane = false;
    private static String PLAYER_POSITION = "player_position";
    private static String CURRENT_STEP = "current_step";
    private static String WINDOW = "window";
    private static String VIDEO_URI = "video_uri";
    private static String RECIPE_ID="recipe_id";
    private static String RECIPE_NAME="recipe_name";
    private static String PLAY_WHEN_READY_SAVED = "play_ready_saved";
    private static String PLAY_WHEN_READY_STATE = "play_state";
    public static final String ARG_BOOLEAN = "boolean_two_pane";
    private List<Ingredient> ingredients = new ArrayList<>();
    private final RecipeRepository recipeRepo;
    private SharedPreferences sharedPreferences;
    public static String EXTRA_RECIPE_ID = "recipe_id_from_widget";
    private StepAdapter stepAdapter;
    View recipeRv;
    View stepsRV;



    public RecipeDetailFragment(){
        recipeRepo = new RecipeRepository(BakingApp.getContext());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYER_POSITION, exoPlayer.getCurrentPosition());
        outState.putInt(CURRENT_STEP, stepView.getCurrentStep());
        outState.putInt(WINDOW, exoPlayer.getCurrentWindowIndex());
        outState.putString(VIDEO_URI, stepList.get(stepView.getCurrentStep()).getStepVideoUrl());
        outState.putBoolean(PLAY_WHEN_READY_STATE, exoPlayer.getPlayWhenReady());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        stepsRV = (RecyclerView) getActivity().findViewById(R.id.stepsRV);

        if(getArguments() !=null && getArguments().containsKey(RECIPE_ID)){
            int recipeID = getArguments().getInt(RECIPE_ID);
            recipe = recipeRepo.getCurrentRecipe(recipeID);
            mTwoPane = getArguments().getBoolean(ARG_BOOLEAN);
            ingredients = recipe.getRecipeIngredients();
            stepList = recipe.getRecipeSteps();
        }

        if(savedInstanceState !=null){
            playbackPosition = savedInstanceState.getLong(PLAYER_POSITION, 0);
            savedStep = savedInstanceState.getInt(CURRENT_STEP, 0);
            currentWindow = savedInstanceState.getInt(WINDOW, 0);
            videoUri = Uri.parse(savedInstanceState.getString(VIDEO_URI, ""));
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_SAVED, true);
            exoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAY_WHEN_READY_STATE));

        }

        if(!stepList.get(0).getStepVideoUrl().isEmpty()){
            videoUri = Uri.parse(stepList.get(0).getStepVideoUrl());
        }



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!mTwoPane) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout collapsingToolbarLayout = activity.findViewById(R.id.toolbar_layout);
            collapsingToolbarLayout.setTitle(recipe.getRecipeName());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_details, container, false);
        exoPlayerView = rootView.findViewById(R.id.exoplayer);

        previewImageView = rootView.findViewById(R.id.previewImageView);

        FloatingActionButton fab;

        stepView = rootView.findViewById(R.id.recipe_steps);
        stepView.setStepsNumber(stepList.size());
        stepView.setOnStepClickListener(this);
        stepView.go(savedStep, true);

        initializePlayer(Uri.parse(stepList.get(stepView.getCurrentStep()).getStepVideoUrl()));

        stepShortDescTv = rootView.findViewById(R.id.stepTvShort);
        stepShortDescTv.setText(stepList.get(0).getStepShortDescription());
        stepLongDescTv = rootView.findViewById(R.id.stepLongDesc);


        stepLongDescTv.setText(stepList.get(0).getStepDescription());


        //ingredients setting
        ingredientsRV = rootView.findViewById(R.id.ingredientsRV);
        ingredientsRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ingredientsRV.setHasFixedSize(true);
        ingredientsRV.setAdapter(new IngredientsAdapter(getContext(), ingredients));

            FloatingActionButton fabNotTwoPane;
            if (mTwoPane) {
                Log.d("detFrag", "its 2 panel! ");
                fabNotTwoPane = rootView.findViewById(R.id.addToWidgetFab);
                fabNotTwoPane.show();
                fabNotTwoPane.setOnClickListener(this);

                recipeRv = getActivity().findViewById(R.id.recipeRV);
                recipeRv.setVisibility(View.GONE);

                stepsRV.setVisibility(View.VISIBLE);
                setStepRv((RecyclerView) stepsRV, recipe);
                stepView.setVisibility(View.GONE);

            }else{
                Log.d("detFrag", "its 1 panel! ");
                fabNotTwoPane = rootView.findViewById(R.id.addToWidgetFab);
                fabNotTwoPane.hide();
                fab= getActivity().findViewById(R.id.fabOnePane);
                fab.setOnClickListener(this);
            }
        return rootView;
    }

    public void initializePlayer(Uri uri) {
        if (uri == null) {
            exoPlayerView.setVisibility(View.GONE);
        } else {
            defaultBandwidthMeter = new DefaultBandwidthMeter();
            trackSelectionFactory = new AdaptiveTrackSelection.Factory(defaultBandwidthMeter);
            trackSelector = new DefaultTrackSelector(trackSelectionFactory);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "bakingapp"), defaultBandwidthMeter);
            mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
            exoPlayer.prepare(mediaSource);
            if (playbackPosition != C.TIME_UNSET) {
                exoPlayer.seekTo(playbackPosition);
            }
        }
    }

    private void setStepRv(RecyclerView recyclerView, Recipe recipe) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
        stepAdapter = new StepAdapter(getActivity().getApplicationContext(), stepList, this);
        stepList = recipe.getRecipeSteps();
        recyclerView.setAdapter(stepAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        stepAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addToWidgetFab:
            case R.id.fabOnePane:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(RECIPE_ID, recipe.getRecipeId());
                editor.putString(RECIPE_NAME, recipe.getRecipeName());
                editor.apply();
                sentToWidgetProvider();
                Toast.makeText(getContext(), "Recipe Saved to Widget", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void sentToWidgetProvider() {
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication())
                .getAppWidgetIds(new ComponentName(getActivity().getApplication(), RecipeAppWidgetProvider.class));

        Intent intent = new Intent(getActivity().getApplicationContext(), RecipeAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids);
        getActivity().getApplication().sendBroadcast(intent);
        Log.d("mymessage", "intent" + intent);
    }

    @Override
    public void onStepClick(int step) {
        int currentStep = stepView.getCurrentStep();
        if (currentStep < step) {
            stepView.go(step, true);
        } else {
            stepView.done(false);
            stepView.go(step, true);
        }
        stepShortDescTv.setText(stepList.get(step).getStepShortDescription());
        stepLongDescTv.setText(stepList.get(step).getStepDescription());
        videoUri = Uri.parse(stepList.get(step).getStepVideoUrl());

        Log.d("videouri", "videouri " + videoUri);
        initializePlayer(videoUri);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(videoUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer(videoUri);
        }
        if (exoPlayer != null) {
            if (playbackPosition > 0) {
                exoPlayer.setPlayWhenReady(playWhenReady);
                exoPlayerView.hideController();
            }
            exoPlayer.seekTo(playbackPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            updatePositionData();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            updatePositionData();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    private void updatePositionData() {
        if (exoPlayer != null) {
            playWhenReady = exoPlayer.getPlayWhenReady();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            playbackPosition = exoPlayer.getCurrentPosition();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
            dataSourceFactory = null;
            mediaSource = null;
            trackSelectionFactory = null;
        }
    }

    @Override
    public void loadPreview(long currentPosition, long max) {
        exoPlayer.setPlayWhenReady(false);
        GlideApp.with(previewImageView)
                .load(videoUri)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .transform(new GlideThumbnailTransformation(currentPosition))
                .into(previewImageView);
    }

    @Override
    public void onClick(View view, Step step) {

        stepShortDescTv.setText(step.getStepShortDescription());
        stepLongDescTv.setText(step.getStepDescription());

        videoUri = Uri.parse(step.getStepVideoUrl());
        Log.d("videouri", "videouri " + videoUri);
        initializePlayer(videoUri);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
        }
                return true;
        }

//                 RecipeAppWidgetProvider recipeAppWidgetProvider = new RecipeAppWidgetProvider();
//                   recipeAppWidgetProvider.onUpdate(this.getActivity(), AppWidgetManager.getInstance(this.getActivity()), ids);
}