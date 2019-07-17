package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Info_View extends AppCompatActivity {
    RelativeLayout relativeLayout;
    ProgressDialog dialog;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.information_page);
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
}
