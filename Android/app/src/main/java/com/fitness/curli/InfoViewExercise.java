package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class InfoViewExercise extends AppCompatActivity {
    RelativeLayout relativeLayout;
    ProgressDialog dialog;
    Context context;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.info_view_exercise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EXERCISE INFO");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        // ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);


        dialog = ProgressDialog.show(InfoViewExercise.this, "", "Loading...", true);

        context = getApplicationContext();
        relativeLayout = findViewById(R.id.relativeLayout);

        getInfo();
    }

    public void getInfo(){
        Intent intent = getIntent();

        TextView title = relativeLayout.findViewById(R.id.title);
        String text = intent.getStringExtra("exercise");
        title.setText(text);

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
