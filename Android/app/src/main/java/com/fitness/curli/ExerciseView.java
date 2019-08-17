package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExerciseView extends AppCompatActivity {
    private Context context;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private ExerciseDb sqlData;
    private String[] nameList;
    private ListView list;
    private Toolbar toolbar;
    private ArrayList<SearchResult> arraylist = new ArrayList<>();
    private ListViewAdapter adapter;
    private Menu menu;
    private String group;
    private int muscleSpinnerSelectedCheck = 0;
    private int equipmentSpinnerSelectedCheck = 0;
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_view);

        toolbar = (Toolbar) findViewById(R.id.header);
        setTitle("");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        group = intent.getStringExtra("group");
        source = intent.getStringExtra("source");


        //create alphabet scroller linear layout
        /*
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
        */

        //final Drawable upArrow = getResources().getDrawable(R.drawable.back_button);
        //upArrow.setColorFilter(getResources().getColor(R.color.light), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);

        TextView title = findViewById(R.id.title);
        title.setText(group);

        ImageView backButton = findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseView.this, MuscleView.class);
                startActivity(intent);
            }
        });

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        dialog = ProgressDialog.show(ExerciseView.this, "", "Loading...", true);

        context = getApplicationContext();
        sqlData = new ExerciseDb(context);
        sqlData.open();

        nameList = sqlData.getExercises("MainGroup", group).toArray(new String[0]);

        for (int i = 0; i < nameList.length; i++) {
            SearchResult name = new SearchResult(nameList[i]);
            // Binds all strings into an array
            arraylist.add(name);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist, 6);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        recyclerView = findViewById(R.id.my_recycler_view);
        //linearLayout = findViewById(R.id.ExerciseViewLinearLayout);

        displaySpinner();
        //displayExercises(sqlData.getExercisesAlphabetized());
        displayExercises2(sqlData.getExercises());

        dialog.dismiss();
    }

    private void displaySpinner(){

        //muscle group spinner functionality
        final Spinner muscleGroupSpinner = findViewById(R.id.muscleGroupSpinner);

        ArrayList<String> allGroups = sqlData.getGroups();
        allGroups.remove(group);
        allGroups.add(0, group);
        allGroups.add("Any Muscle Group");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allGroups);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        muscleGroupSpinner.setAdapter(dataAdapter);

        muscleGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGroup = parent.getSelectedItem().toString();

                //Spinner father = findViewById(R.id.muscleGroupSpinner);
                if (++muscleSpinnerSelectedCheck > 1 && !selectedGroup.equals(group)) {
                    if (!selectedGroup.equals("Any Muscle Group")) {
                        Intent intent = new Intent(ExerciseView.this, ExerciseView.class);
                        intent.putExtra("group", selectedGroup);
                        intent.putExtra("source", "muscle_view");
                        startActivity(intent);
                    }
                    else if (selectedGroup.equals("Any Muscle Group")){
                        Intent intent = new Intent(ExerciseView.this, MuscleView.class);
                        startActivity(intent);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        //equipment spinner functionality
        Spinner equipmentSpinner = findViewById(R.id.equipmentSpinner);

        ArrayList<String> allEquipment = new ArrayList<>(Arrays.asList("Any Equipment", "Barbell", "Body Only", "Cables", "Dumbell"));
        //allGroups.remove(group);
        //allGroups.add(0, group);
        //allGroups.add("Any Muscle Group");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allEquipment);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        equipmentSpinner.setAdapter(dataAdapter);

        equipmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEquipment = parent.getSelectedItem().toString();

                if (++equipmentSpinnerSelectedCheck > 1){
                    if (!selectedEquipment.equals("Any Equipment")) {
                        recyclerView.removeAllViews();
                        displayExercises2(sqlData.getExercises("Equipment", selectedEquipment));
                    }
                    else if (selectedEquipment.equals("Any Equipment")){
                        recyclerView.removeAllViews();
                        displayExercises2(sqlData.getExercises());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void displayExercises(HashMap<String, ArrayList<LinkedHashMap<String,String>>> exercises){
        //card layout functionality
        Display display = getWindowManager().getDefaultDisplay();

        int layoutWidth = display.getWidth();
        int cardsPerRow = 3;

        for (String letter : exercises.keySet()){

            TextView letterSubtitle = new TextView(this);
            letterSubtitle.setText(letter);
            RelativeLayout.LayoutParams letterSubtitleParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            int exerciseSize = exercises.get(letter).size();

            if (exerciseSize > 0) {
                letterSubtitleParams.setMargins(40, 10, 0, 10);
                letterSubtitle.setLayoutParams(letterSubtitleParams);
                letterSubtitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                letterSubtitle.setTextSize(22);
                linearLayout.addView(letterSubtitle);
            }

            int exerciseNameIndex = 0;
            boolean adding = true;

            while (adding ){
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
                    params.width = layoutWidth / cardsPerRow;
                    rel.setLayoutParams(params);

                    String titleText = exercise.get("name");

                    TextView title = card.findViewById(R.id.title);
                    title.setText(titleText);

                    TextView equipment = card.findViewById(R.id.equipment);
                    equipment.setText(exercise.get("equipment"));

                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });

                    rowLayout.addView(card);

                    exerciseNameIndex++;
                }
                linearLayout.addView(rowLayout);
            }

        }
    }

    public void displayExercises2(ArrayList<String> exercisesArray){
        String[] exercises = exercisesArray.toArray(new String[]{});

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        ExerciseListAdapter mAdapter = new ExerciseListAdapter(this, source, exercises);
        recyclerView.setAdapter(mAdapter);

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        list.setVisibility(View.INVISIBLE);

        //getMenuInflater().inflate(R.menu.search_menu, menu);
        LinearLayout wrapper = toolbar.findViewById(R.id.toolbarWrapper);
        RelativeLayout topBar = wrapper.findViewById(R.id.topBar);
        ActionMenuView avmMenu = topBar.findViewById(R.id.avmMenu);

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
                RelativeLayout parent = (RelativeLayout) (v.getParent());
                TextView title = parent.findViewById(R.id.title);
                ImageView back = parent.findViewById(R.id.backbutton);

                if (!hasFocus) {
                    list.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
                else if (hasFocus) {
                    list.setVisibility(View.VISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);
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
            Intent intent = new Intent(ExerciseView.this, ExerciseViewInfo.class);
            intent.putExtra("exercise", name);
            startActivity(intent);
        }
    }

    public void onExerciseItemSelect(View v){
        TextView exercise = v.findViewById(R.id.exercise);
        String exerciseText = exercise.getText().toString();

        if (source.equals("muscle_view")) {
            Intent intent = new Intent(ExerciseView.this, ExerciseViewInfo.class);
            intent.putExtra("exercise", exerciseText);
            startActivity(intent);
        }
        else if (source.equals("schedule_planner")){
            System.out.println(exerciseText);
        }
    }
}