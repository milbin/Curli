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
    private ExerciseDb sqlData;
    private String[] nameList;
    private ArrayList<SearchResult> arraylist = new ArrayList<>();
    private ListViewAdapter adapter;
    private ListView list;
    private Menu menu;
    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");

        setSupportActionBar(toolbar);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        dialog = ProgressDialog.show(MuscleView.this, "", "Loading...", true);

        context = getApplicationContext();
        sqlData = new ExerciseDb(context);
        sqlData.open();

        nameList = sqlData.getExercises().toArray(new String[0]);

        for (int i = 0; i < 6; i++) {
            SearchResult name = new SearchResult(nameList[i]);
            // Binds all strings into an array
            arraylist.add(name);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        displayMuscles();

    }

    public void displayMuscles(){
        Spinner muscleGroupSpinner = findViewById(R.id.muscleGroupSpinner);
        muscleGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGroup = parent.getSelectedItem().toString();

                //Spinner father = findViewById(R.id.muscleGroupSpinner);
                if (++check > 1) {
                    if (!selectedGroup.equals("Any Muscle Group")) {
                        Intent intent = new Intent(MuscleView.this, ExerciseView.class);
                        intent.putExtra("group", selectedGroup);
                        intent.putExtra("source", "schedule_planner");
                        startActivity(intent);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        ArrayList<String> groups = sqlData.getGroups();

        int groupSize = groups.size();
        int groupNameIndex = 0;
        int cardsPerRow = 2;
        boolean adding = true;

        LinearLayout linearLayout = findViewById(R.id.MuscleViewLinearLayout);

        Display display = getWindowManager().getDefaultDisplay();

        int layoutWidth = display.getWidth();

        System.out.println(layoutWidth);
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
                catch (Exception e){

                }

                //TextView equipment = card.findViewById(R.id.equipment);
                //equipment.setText("");

                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MuscleView.this, ExerciseView.class);
                        intent.putExtra("group", titleText);
                        intent.putExtra("source", "muscle_view");
                        startActivity(intent);
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
        ActionMenuView avmMenu = toolbar.findViewById(R.id.avmMenu);
        getMenuInflater().inflate(R.menu.search_menu, avmMenu.getMenu());
        this.menu = avmMenu.getMenu();

        MenuItem searchItem = avmMenu.getMenu().findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();


        avmMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
                                               @Override
                                               public boolean onMenuItemClick(MenuItem menuItem) {
                                                   if(menuItem.getItemId() == R.id.action_search){
                                                       searchView.setIconified(false);
                                                       SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                                                       searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
                                                       searchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));
                                                       ImageView icon = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
                                                       icon.setColorFilter(Color.WHITE);
                                                   }
                                                   return false;
                                               }
                                           }


        );

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    list.setVisibility(View.INVISIBLE);
                }
                else if (hasFocus) {
                    list.setVisibility(View.VISIBLE);
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


        if (nameLabel.equals("Exercise")){
            Intent intent = new Intent(MuscleView.this, ExerciseViewInfo.class);
            intent.putExtra("exercise", name);
            startActivity(intent);
        }
    }
}
