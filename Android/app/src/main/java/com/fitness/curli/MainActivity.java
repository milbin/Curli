package com.fitness.curli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextView text = findViewById(R.id.text);
        Intent intent = new Intent(MainActivity.this, InfoView.class);
        startActivity(intent);
    }

}
