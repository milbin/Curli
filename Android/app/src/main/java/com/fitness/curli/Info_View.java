package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Info_View extends AppCompatActivity {
    RelativeLayout relativeLayout;
    ProgressDialog dialog;
    Context context;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.information_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        // ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);


        dialog = ProgressDialog.show(Info_View.this, "", "Loading...", true);

        context = getApplicationContext();
        relativeLayout = findViewById(R.id.relativeLayout);

        getInfo();
    }

    public void getInfo(){
        Intent intent = getIntent();

        TextView title = relativeLayout.findViewById(R.id.title);
        String text = intent.getStringExtra("title");
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
