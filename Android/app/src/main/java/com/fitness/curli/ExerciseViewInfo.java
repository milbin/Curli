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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ExerciseViewInfo extends AppCompatActivity {
    RelativeLayout relativeLayout;
    ProgressDialog dialog;
    Context context;
    Menu menu;
    SQLData SQLData;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.exercise_view_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        // ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);


        dialog = ProgressDialog.show(ExerciseViewInfo.this, "", "Loading...", true);

        context = getApplicationContext();
        SQLData = new SQLData();
        SQLData.openExerciseDB(context);
        relativeLayout = findViewById(R.id.relativeLayout);

        getInfo();
    }

    public void getInfo(){
        Intent intent = getIntent();

        TextView title = relativeLayout.findViewById(R.id.title);
        String text = intent.getStringExtra("exercise");
        title.setText(text);

        String group = SQLData.getGroup1FromName(text);
        LinearLayout muscleTags = findViewById(R.id.muscleTags);
        View card = LayoutInflater.from(context).inflate(R.layout.tag, null);
        TextView tagText = card.findViewById(R.id.title);
        tagText.setText(group);
        muscleTags.addView(card);
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
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

}
