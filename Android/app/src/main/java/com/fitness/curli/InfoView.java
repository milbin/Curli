package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class InfoView extends AppCompatActivity {
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    SQLData sqlData = new SQLData();
    String[] nameList;
    ListView list;
    ArrayList<SearchResult> arraylist = new ArrayList<>();
    ListViewAdapter adapter;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //final Drawable upArrow = getResources().getDrawable(R.drawable.left_white);
        //upArrow.setColorFilter(getResources().getColor(R.color.light), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);

        setSupportActionBar(toolbar);


        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        // Generate sample sqlData
        ArrayList<String> tempList = sqlData.GROUPS;
        tempList.addAll(sqlData.MUSCLES);
        tempList.addAll(sqlData.EXERCISES);
        nameList = tempList.toArray(new String[0]);

        for (int i = 0; i < nameList.length; i++) {
            SearchResult name = new SearchResult(nameList[i]);
            // Binds all strings into an array
            arraylist.add(name);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        dialog = ProgressDialog.show(InfoView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.GroupsViewLinearLayout);

        displayExercises();
    }

    private void displayExercises(){
        HashMap<String, ArrayList<LinkedHashMap<String,String>>> exercises = sqlData.getExercises();

        int length = 0;
        for (HashMap.Entry<String, ArrayList<LinkedHashMap<String,String>>> entryLetter : exercises.entrySet()) {
            //each itteration of this loop is equal to one letter of the sqldata data structure
            //seperates the exercises by their first letter
            TextView letterSeparator = new TextView(this);
            letterSeparator.setText(entryLetter.getKey());
            RelativeLayout.LayoutParams letterSeparatorLp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            letterSeparatorLp.setMargins(40, 10, 0, 10);
            letterSeparator.setLayoutParams(letterSeparatorLp);
            letterSeparator.setTextColor(getResources().getColor(R.color.colorPrimary));
            letterSeparator.setTextSize(22);
            linearLayout.addView(letterSeparator);

            //here 3 of the exercise cards are added to each horizontal linear layout created below
            int numberOfViewsAdded = 0;
            LinearLayout subll = new LinearLayout(this);
            for (LinkedHashMap<String, String> exercise : entryLetter.getValue()) {
                System.out.println("HERE");
                View card = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
                if(numberOfViewsAdded%3 == 0 && numberOfViewsAdded != 0){
                    linearLayout.addView(subll);
                    subll = new LinearLayout(this);
                    numberOfViewsAdded = -1;
                }
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView rl = v.findViewById(R.id.title);
                        String group = rl.getText().toString();
                        Intent intent = new Intent(InfoView.this, InfoViewExercise.class);
                        intent.putExtra("group", group);
                        startActivity(intent);
                    }
                });
                TextView title = card.findViewById(R.id.title);
                TextView equipment = card.findViewById(R.id.equipment);
                title.setText(exercise.get("name"));
                System.out.println(exercises);
                equipment.setText(exercise.get("equipment"));
                subll.addView(card);
                numberOfViewsAdded++;
            }
            if(numberOfViewsAdded != 2){
                linearLayout.addView(subll);
            }
        }


        /*for (int i = 0; i < length; i++){
            View card = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(muscleGroups.get(i));

            View card1 = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
            TextView title1 = card.findViewById(R.id.title);
            title1.setText(muscleGroups.get(i));

            View card2 = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
            TextView title2 = card.findViewById(R.id.title);
            title2.setText(muscleGroups.get(i));

            View card3 = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
            TextView title3 = card.findViewById(R.id.title);
            title3.setText(muscleGroups.get(i));

            View card4 = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
            TextView title4 = card.findViewById(R.id.title);
            title4.setText(muscleGroups.get(i));


            LinearLayout subll = new LinearLayout(this);
            subll.addView(card);
            subll.addView(card1);
            subll.addView(card2);
            LinearLayout subll1 = new LinearLayout(this);
            if(i ==1){

                subll1.addView(card3);
                subll1.addView(card4);
            }
            TextView tv = new TextView(this);
            if(i==0) {
                tv.setText("A");
            }else{
                tv.setText("B");
            }
            RelativeLayout.LayoutParams tvLp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tvLp.setMargins(40, 10, 0, 10);
            tv.setLayoutParams(tvLp);
            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv.setTextSize(22);
            linearLayout.addView(tv);
            linearLayout.addView(subll);
            linearLayout.addView(subll1);

        }*/

        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        list.setVisibility(View.INVISIBLE);

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));
        ImageView icon = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        icon.setColorFilter(Color.WHITE);

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
            Intent intent = new Intent(InfoView.this, InfoViewExercise.class);
            intent.putExtra("exercise", name);
            startActivity(intent);
        }
    }
}