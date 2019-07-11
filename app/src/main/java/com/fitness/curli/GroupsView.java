package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupsView extends AppCompatActivity {
    ArrayList<String> muscleGroups = new ArrayList<>(Arrays.asList("arms", "legs", "core"));
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    private AsyncTask getGroupsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);

        dialog = ProgressDialog.show(GroupsView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.GroupsViewLinearLayout);

        getGroupsTask = new getGroups().execute();
    }

    private class getGroups extends AsyncTask<String, Integer, JSONObject>{
        @Override
        protected void onPreExecute(){

        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            int length = muscleGroups.size();

            for (int i = 0; i < length; i++){
                View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
                TextView title = card.findViewById(R.id.title);
                title.setText(muscleGroups.get(i));

                linearLayout.addView(card);
            }

            dialog.dismiss();
        }
    }
}
