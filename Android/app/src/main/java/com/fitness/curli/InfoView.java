package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    ExerciseDb sqlData;
    String[] nameList;
    ListView list;
    Toolbar toolbar;
    ArrayList<SearchResult> arraylist = new ArrayList<>();
    ListViewAdapter adapter;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");


        //create alphabet scroller linear layout
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        LinearLayout alphabetScroll = findViewById(R.id.alphabetScroll);
        for (int i = 0; i < alphabet.length(); i++){
            char letter = alphabet.charAt(i);
            TextView letterTV = new TextView(this);
            letterTV.setText(Character.toString(letter));
            letterTV.setTypeface(letterTV.getTypeface(), Typeface.BOLD);
            letterTV.setTextSize(13);
            alphabetScroll.addView(letterTV);

        }

        //final Drawable upArrow = getResources().getDrawable(R.drawable.back_button);
        //upArrow.setColorFilter(getResources().getColor(R.color.light), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);

        setSupportActionBar(toolbar);


        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        // Generate sample sqlData
        //ArrayList<String> tempList = sqlData.GROUPS;
        //tempList.addAll(sqlData.MUSCLES);
        //tempList.addAll(sqlData.EXERCISES);
        nameList = new String[]{};//tempList.toArray(new String[0]);

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
        sqlData = new ExerciseDb(context);
        sqlData.open();

        linearLayout = findViewById(R.id.GroupsViewLinearLayout);

        displayExercises();
    }

    private void displayExercises(){
        HashMap<String, ArrayList<LinkedHashMap<String,String>>> exercises = sqlData.getExercises();

        Display display = getWindowManager().getDefaultDisplay();

        int layoutWidth = display.getWidth();
        int cardsPerRow = 2;

        for (String letter : exercises.keySet()){

            TextView letterSubtitle = new TextView(this);
            letterSubtitle.setText(letter);
            RelativeLayout.LayoutParams letterSubtitleBody = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            letterSubtitleBody.setMargins(40, 10, 0, 10);
            letterSubtitle.setLayoutParams(letterSubtitleBody);
            letterSubtitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            letterSubtitle.setTextSize(22);
            linearLayout.addView(letterSubtitle);

            int exerciseSize = exercises.get(letter).size();
            System.out.println(exercises.get(letter));
            int exerciseNameIndex = 0;
            boolean adding = true;

            while (adding){
                LinearLayout rowLayout = new LinearLayout(this);
                for (int x = 0; x < cardsPerRow; x++){
                    if (exerciseNameIndex == exerciseSize){
                        adding = false;
                        break;
                    }

                    LinkedHashMap<String, String> exercise = exercises.get(letter).get(exerciseNameIndex);

                    View card = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
                    RelativeLayout rel = card.findViewById(R.id.group);

                    ViewGroup.LayoutParams params = rel.getLayoutParams();
                    params.width = layoutWidth/cardsPerRow;
                    rel.setLayoutParams(params);

                    String titleText = exercise.get("name");

                    TextView title = card.findViewById(R.id.title);
                    title.setText(titleText);

                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });

                    rowLayout.addView(card);

                    if (exerciseNameIndex+1 == exerciseSize){
                        adding = false;
                        break;
                    }
                    exerciseNameIndex++;
                }
                linearLayout.addView(rowLayout);
            }

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
            Intent intent = new Intent(InfoView.this, InfoViewExercise.class);
            intent.putExtra("exercise", name);
            startActivity(intent);
        }
    }
}