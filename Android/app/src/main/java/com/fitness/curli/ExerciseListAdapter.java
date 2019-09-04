package com.fitness.curli;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder> {
private String[] mDataset;
private Context mContext;
private SQLData sqlData;
private String source;

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
    public ExerciseListAdapter(Context context, String source, String[] myDataset) {
        mDataset = myDataset;
        mContext = context;
        this.source = source;
        sqlData = new SQLData();
        sqlData.openExerciseDB(context);
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
        final String exercise = mDataset[position];
        TextView title = holder.cardView.findViewById(R.id.exercise);
        title.setText(exercise);

        String equipmentText = sqlData.getPrimaryEquipmentFromName(exercise);
        TextView equipment = holder.cardView.findViewById(R.id.equipment);
        equipment.setText(equipmentText);

        if (source.equals("muscle_view")){
            ImageView icon = holder.cardView.findViewById(R.id.icon);
            icon.setImageResource(R.drawable.ic_navigate_next_blue_24dp);
        }
        else if (source.equals("schedule_planner")){
            ImageView icon = holder.cardView.findViewById(R.id.icon);
            icon.setImageResource(R.drawable.ic_add_blue_24dp);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
