package com.fitness.curli;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class GroupsView extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    SQLData data = new SQLData();
    String[] nameList;
    ListView list;
    ArrayList<SearchResult> arraylist = new ArrayList<>();
    ListViewAdapter adapter;
    SearchView editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);

        dialog = ProgressDialog.show(GroupsView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.GroupsViewLinearLayout);

        // Generate sample data
        nameList = data.MUSCLE_TO_EXCERCISE.keySet().toArray(new String[0]);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < nameList.length; i++) {
            SearchResult name = new SearchResult(nameList[i]);
            // Binds all strings into an array
            arraylist.add(name);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

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
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}
