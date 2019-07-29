package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class MuscleView extends AppCompatActivity {
    SQLData data = new SQLData();
    ArrayList<String> muscles;
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    Menu menu;
    String[] nameList;
    ListView list;
    ArrayList<SearchResult> arraylist = new ArrayList<>();
    ListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscles_view);

        Intent intent = getIntent();
        String group = intent.getStringExtra("group");
        muscles = data.GROUP_TO_MUSCLE.get(group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(group.toUpperCase());
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        // Generate sample sqlData
        ArrayList<String> tempList = data.GROUPS;
        tempList.addAll(data.MUSCLES);
        tempList.addAll(data.EXERCISES);
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

        dialog = ProgressDialog.show(MuscleView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.MusclesViewLinearLayout);

        getMuscles();
    }

    public void getMuscles(){
        int length = muscles.size();

        for (int i = 0; i < length; i++){
            final View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(muscles.get(i));

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    TextView rl = v.findViewById(R.id.title);
                    String muscle = rl.getText().toString();
                    Intent intent = new Intent(MuscleView.this, ExerciseView.class);
                    intent.putExtra("muscle", muscle);
                    startActivity(intent);
                }
            });

            linearLayout.addView(card);
        }

    dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.menu = menu;
        list.setVisibility(View.INVISIBLE);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);

        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    menu.findItem(R.id.back).setVisible(true);
                    list.setVisibility(View.INVISIBLE);
                }
                else if (hasFocus) {
                    menu.findItem(R.id.back).setVisible(false);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onListItemSelect(View v){
        TextView label = v.findViewById(R.id.nameLabel);
        String nameLabel = label.getText().toString().replace(" : ", "");

        TextView text = v.findViewById(R.id.name);
        String name = text.getText().toString();

        if (nameLabel.equals("Group")) {
            Intent intent = new Intent(MuscleView.this, MuscleView.class);
            intent.putExtra("group", name);
            startActivity(intent);
        }
        else if (nameLabel.equals("Muscle")){
            Intent intent = new Intent(MuscleView.this, ExerciseView.class);
            intent.putExtra("muscle", name);
            startActivity(intent);
        }
        else if (nameLabel.equals("Exercise")){
            Intent intent = new Intent(MuscleView.this, Info_View.class);
            intent.putExtra("exercise", name);
            startActivity(intent);
        }
    }
}

