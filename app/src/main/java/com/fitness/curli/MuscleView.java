package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class MuscleView extends AppCompatActivity {
    ArrayList<String> muscles;
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    private AsyncTask getMusclesTask;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscles_view);

        dialog = ProgressDialog.show(MuscleView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.MusclesViewLinearLayout);

        getMusclesTask = new getMuscles().execute();
    }

    private class getMuscles extends AsyncTask<String, Integer, JSONObject> {
        @Override
        protected void onPreExecute(){
            Intent intent = getIntent();
            muscles = intent.getStringArrayListExtra("muscles");
            System.out.println(muscles);
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
            int length = muscles.size();

            for (int i = 0; i < length; i++){
                View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
                TextView title = card.findViewById(R.id.title);
                title.setText(muscles.get(i));

                linearLayout.addView(card);
            }

            dialog.dismiss();
        }
    }
}
