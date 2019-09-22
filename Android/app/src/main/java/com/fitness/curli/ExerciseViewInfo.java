package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String group = SQLData.getGroupFromName(text);
        LinearLayout muscleTags = findViewById(R.id.muscleTags);
        View card = LayoutInflater.from(context).inflate(R.layout.tag, null);
        TextView tagText = card.findViewById(R.id.title);
        tagText.setText(group);
        muscleTags.addView(card);
        dialog.dismiss();
    }

}
