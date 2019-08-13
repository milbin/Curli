package com.fitness.curli;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder> {
private String[] mDataset;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public RelativeLayout cardView;
    public MyViewHolder(RelativeLayout view) {
        super(view);
        cardView = view;
    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExerciseListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExerciseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        RelativeLayout card = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercises_list_view, parent, false);
        MyViewHolder viewholder = new MyViewHolder(card);
        return viewholder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView title = holder.cardView.findViewById(R.id.exercise);
        title.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
