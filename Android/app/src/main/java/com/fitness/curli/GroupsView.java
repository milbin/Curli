package com.fitness.curli;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class GroupsView extends AppCompatActivity {
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    SQLData data = new SQLData();
    String[] nameList;
    ListView list;
    ArrayList<SearchResult> arraylist = new ArrayList<>();
    ListViewAdapter adapter;
    SearchView editsearch;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MUSCLE GROUPS");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        // Generate sample data
        nameList = data.MUSCLE_TO_EXCERCISE.keySet().toArray(new String[0]);

        for (int i = 0; i < nameList.length; i++) {
            SearchResult name = new SearchResult(nameList[i]);
            // Binds all strings into an array
            arraylist.add(name);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        dialog = ProgressDialog.show(GroupsView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.GroupsViewLinearLayout);

        getGroups();
    }

    private void getGroups(){
        ArrayList<String> muscleGroups = new ArrayList<>(data.GROUP_TO_MUSCLE.keySet());

        int length = muscleGroups.size();

        for (int i = 0; i < length; i++){
            View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(muscleGroups.get(i));

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView rl = v.findViewById(R.id.title);
                    String group = rl.getText().toString();
                    ArrayList<String> muscles = data.GROUP_TO_MUSCLE.get(group);
                    Intent intent = new Intent(GroupsView.this, MuscleView.class);
                    intent.putExtra("muscles", muscles);
                    startActivity(intent);
                }
            });

            linearLayout.addView(card);
        }

        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        list.setVisibility(View.INVISIBLE);

        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                System.out.println("HERE");
                return true;  // Return true to expand action view
            }
        };


        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

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
    public void select(View v){
        TextView text = v.findViewById(R.id.name);
        String muscle = text.getText().toString();
        ArrayList<String> exercises = data.MUSCLE_TO_EXCERCISE.get(muscle);
        Intent intent = new Intent(GroupsView.this, ExerciseView.class);
        intent.putExtra("exercises", exercises);
        startActivity(intent);
    }
}