package com.fitness.curli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MuscleView extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog dialog;
    private Context context;
    private SQLData sqlData;
    private String[] nameList;
    private ArrayList<SearchResult> arraylist = new ArrayList<>();
    private ListViewAdapter adapter;
    private ListView list;
    private Menu menu;
    private int check = 0;
    private ArrayList<String> exercisesToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_view);
        Intent intent = getIntent();
        if(intent.getBooleanExtra("FromWorkoutBuilder", false)){//check if this activity is getting called from workoutBuilder
            exercisesToAdd = new ArrayList<>();
            findViewById(R.id.bottom_nav_bar).setVisibility(View.GONE);
            findViewById(R.id.addedExercisesBar).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.addedExercisesBar).findViewById(R.id.numberOfExercisesAdded)).setText(exercisesToAdd.size() + " Exercises");
            findViewById(R.id.addedExercisesBar).findViewById(R.id.addExercisesButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");

        setSupportActionBar(toolbar);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        dialog = ProgressDialog.show(MuscleView.this, "", "Loading...", true);

        context = getApplicationContext();
        sqlData = new SQLData();
        sqlData.openExerciseDB(context);

        nameList = sqlData.getExercises().toArray(new String[0]);

        for (int i = 0; i < nameList.length; i++) {
            SearchResult name = new SearchResult(nameList[i]);
            // Binds all strings into an array
            arraylist.add(name);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist, 6);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        displayMuscles();

    }

    public void displayMuscles(){
        ArrayList<String> groups = sqlData.getGroups();

        int groupSize = groups.size();
        int groupNameIndex = 0;
        int cardsPerRow = 2;
        boolean adding = true;

        LinearLayout linearLayout = findViewById(R.id.MuscleViewLinearLayout);

        Display display = getWindowManager().getDefaultDisplay();

        int layoutWidth = display.getWidth();


        while (adding){
            LinearLayout rowLayout = new LinearLayout(this);
            for (int x = 0; x < cardsPerRow; x++){
                View card = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
                RelativeLayout rel = card.findViewById(R.id.group);

                ViewGroup.LayoutParams params = rel.getLayoutParams();
                params.width = layoutWidth/cardsPerRow;
                rel.setLayoutParams(params);

                final String titleText = groups.get(groupNameIndex);
                TextView title = card.findViewById(R.id.title);
                title.setText(titleText);
                title.bringToFront();

                RelativeLayout group = card.findViewById(R.id.group);
                group.removeView(card.findViewById(R.id.equipment));

                try{
                    ImageView icon = card.findViewById(R.id.icon);
                    int imageId = context.getResources().getIdentifier(titleText.toLowerCase().replaceAll(" ", "_"), "drawable", context.getPackageName());
                    icon.setImageResource(imageId);
                }
                catch (Exception e){}

                //TextView equipment = card.findViewById(R.id.equipment);
                //equipment.setText("");

                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MuscleView.this, ExerciseView.class);
                        intent.putExtra("group", titleText);
                        if(exercisesToAdd == null) {
                            intent.putExtra("source", "muscle_view");
                            startActivity(intent);
                        }else{
                            intent.putExtra("source", "WorkoutBuilder");
                            intent.putExtra("exercisesToAdd", exercisesToAdd);
                            startActivityForResult(intent, 1);
                        }

                    }
                });

                rowLayout.addView(card);

                if (groupNameIndex+1 == groupSize){
                    adding = false;
                    break;
                }
                groupNameIndex++;
            }
            linearLayout.addView(rowLayout);
        }

        dialog.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        list.setVisibility(View.INVISIBLE);

        //getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = toolbar.findViewById(R.id.searchView);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    list.setVisibility(View.INVISIBLE);
                    toolbar.findViewById(R.id.title).setVisibility(View.VISIBLE);
                }
                else if (hasFocus) {
                    list.setVisibility(View.VISIBLE);
                    toolbar.findViewById(R.id.title).setVisibility(View.INVISIBLE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.setVisibility(View.VISIBLE);
                String text = newText;
                adapter.filter(text);
                return false;
            }
        });

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    public void onListItemSelect(View v){
        TextView label = v.findViewById(R.id.nameLabel);
        String nameLabel = label.getText().toString().replace(" : ", "");

        TextView text = v.findViewById(R.id.name);
        String name = text.getText().toString();


        if (nameLabel.equals("exercise")){
            Intent intent = new Intent(MuscleView.this, ExerciseViewInfo.class);
            intent.putExtra("exercise", name);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {
            ArrayList<String> exercisesList = data.getStringArrayListExtra("exercisesToAdd");


            for(String exercise:exercisesList){
                if(!exercisesToAdd.contains(exercise)){
                    exercisesToAdd.add(exercise);
                }
            }
            if(data.getBooleanExtra("shouldFinish", false)){ //this means the add exercises button was pressed in the child activity of this one
                onBackPressed();
            }
            ((TextView)findViewById(R.id.addedExercisesBar).findViewById(R.id.numberOfExercisesAdded)).setText(exercisesToAdd.size() + " Exercises");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("exercisesToAdd", exercisesToAdd);
        setResult(1, intent);
        super.onBackPressed();
    }
}
