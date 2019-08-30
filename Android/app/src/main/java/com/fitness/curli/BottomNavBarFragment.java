package com.fitness.curli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BottomNavBarFragment extends Fragment {
    View fragment;
    Context context;
    String parentActivity = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.bottom_nav_bar, container, false);
        context = getActivity();

        //set on click listener for the bottom bar buttons
        ((View)fragment.findViewById(R.id.history).getParent()).setOnClickListener(new onNavbarClick());
        ((View)fragment.findViewById(R.id.schedule).getParent()).setOnClickListener(new onNavbarClick());
        ((View)fragment.findViewById(R.id.workout).getParent()).setOnClickListener(new onNavbarClick());
        ((View)fragment.findViewById(R.id.exercises).getParent()).setOnClickListener(new onNavbarClick());
        ((View)fragment.findViewById(R.id.progress).getParent()).setOnClickListener(new onNavbarClick());

        //check which activity is the parent activity
        try{
            MuscleView testForCastingError = (MuscleView) getActivity();
            parentActivity = "History";
            ((ImageView)fragment.findViewById(R.id.history)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            ((TextView)((RelativeLayout)fragment.findViewById(R.id.history).getParent()).findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

        }catch (ClassCastException e){}
        try{
            MuscleView testForCastingError = (MuscleView) getActivity();
            parentActivity = "Schedule";
            ((ImageView) fragment.findViewById(R.id.schedule)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            ((TextView)((RelativeLayout)fragment.findViewById(R.id.schedule).getParent()).findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

        }catch (ClassCastException e){}
        try{
            MainActivity testForCastingError = (MainActivity) getActivity();
            parentActivity = "Workout";
            ((ImageView)fragment.findViewById(R.id.workout)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            ((TextView)((RelativeLayout)fragment.findViewById(R.id.workout).getParent()).findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

        }catch (ClassCastException e){}
        try{
            MuscleView testForCastingError = (MuscleView) getActivity();
            parentActivity = "Exercises";
            ((ImageView) fragment.findViewById(R.id.exercises)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            ((TextView)((RelativeLayout)fragment.findViewById(R.id.exercises).getParent()).findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

        }catch (ClassCastException e){}
        try{
            MuscleView testForCastingError = (MuscleView) getActivity();
            parentActivity = "Progress";
            ((ImageView)fragment.findViewById(R.id.progress)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            ((TextView)((RelativeLayout)fragment.findViewById(R.id.progress).getParent()).findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

        }catch (ClassCastException e){}
        System.out.println(parentActivity +"HERE");

        // Inflate the layout for this fragment
        return fragment;
    }

    public class onNavbarClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //grey out all other icons and textviews
            LinearLayout linearLayout = (LinearLayout)v.getParent();
            ((ImageView)fragment.findViewById(R.id.history)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)fragment.findViewById(R.id.schedule)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)fragment.findViewById(R.id.workout)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)fragment.findViewById(R.id.exercises)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)fragment.findViewById(R.id.progress)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            int numberOfChildren = linearLayout.getChildCount();
            for(int i=0; i<numberOfChildren; i++){
                ((TextView)linearLayout.getChildAt(i).findViewById(R.id.bottom_icon_TV)).setTextColor(Color.parseColor("#5f6267")); //gray
            }

            if(v.findViewById(R.id.history) != null && !parentActivity.equals("History")){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.history)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if(v.findViewById(R.id.schedule) != null && !parentActivity.equals("Schedule")){
                //set current view as selected (color primary)
                ((ImageView) v.findViewById(R.id.schedule)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView) v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));
                Intent intent = new Intent(getActivity(), MuscleView.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0); //this disables animations
            }else if(v.findViewById(R.id.workout)!= null && !parentActivity.equals("Workout")){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.workout)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0); //this disables animations

            }else if(v.findViewById(R.id.exercises)!= null && !parentActivity.equals("Exercises")){
                //set current view as selected (color primary)
                ((ImageView) v.findViewById(R.id.exercises)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView) v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));
                Intent intent = new Intent(getActivity(), MuscleView.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0); //this disables animations


            }else if(v.findViewById(R.id.progress)!= null && !parentActivity.equals("Progress")){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.progress)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

            }

        }
    }
}
