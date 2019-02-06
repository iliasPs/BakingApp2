package com.example.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.Models.Recipe;
import com.example.bakingapp.Models.Step;
import com.example.bakingapp.R;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepsViewHolder> {

    private List<Step> stepList;
    private Context mContext;
    final private StepClickListener stepClickListener;

    public StepAdapter(Context c, List<Step> steps, StepClickListener stepClickListener) {
        this.stepClickListener = stepClickListener;
        mContext = c;
        this.stepList = steps;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context c = parent.getContext();
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        StepsViewHolder stepsViewHolder = new StepsViewHolder(view);
        return stepsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder stepsViewHolder, int i) {
        Step step = this.stepList.get(i);
        stepsViewHolder.stepShort.setText(step.getStepShortDescription());
        stepsViewHolder.stepId.setText(String.valueOf(step.getStepId()+1) + ".");
    }


    @Override
    public int getItemCount() {
        return (null != stepList ? stepList.size() : 0);
    }


    /**
     * Cache of the children views for a list item.
     */
    public class StepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView stepShort;
        TextView stepId;

        public StepsViewHolder(View itemView) {
            super(itemView);
            stepShort = itemView.findViewById(R.id.step_short);
            stepId = itemView.findViewById(R.id.step_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Step currentStep = stepList.get(getAdapterPosition());
            stepClickListener.onClick(v, currentStep);
        }
    }


    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setSteps(List<Step> recipeSteps) {
        stepList = recipeSteps;
        notifyDataSetChanged();
    }


    public interface StepClickListener {

        void onClick(View view, Step step);

    }
}
