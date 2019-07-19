package com.fitness.curli;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ExerciseView extends AppCompatActivity {
    ProgressDialog dialog;
    Context context;
    ArrayList<String> exercises;
    LinearLayout linearLayout;
    Integer defaultHeight = -1;
    ArrayList<View> cardsList = new ArrayList<>();
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EXERCISES");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        dialog = ProgressDialog.show(ExerciseView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.ExerciseViewLinearLayout);
        Intent intent = getIntent();
        exercises = intent.getStringArrayListExtra("exercises");

        getExcercises();
    }

    public void getExcercises(){
        int length = exercises.size();

        for (int i = 0; i < length; i++){
            View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(exercises.get(i));
            final TextView description = new TextView(context);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView t = v.findViewById(R.id.title);
                    String title = t.getText().toString();
                    Intent intent = new Intent(ExerciseView.this, Info_View.class);
                    intent.putExtra("title", title);
                    startActivity(intent);

                    /*
                    RelativeLayout rlNested = v.findViewById(R.id.group);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlNested.getLayoutParams();
                    ImageView img = v.findViewById(R.id.group_image);
                    RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) img.getLayoutParams();

                    if (defaultHeight == -1){
                        defaultHeight = params.height;
                    }
                    if (rlNested.getHeight() == defaultHeight){
                        for (View c : cardsList){
                            TextView e = c.findViewById(R.id.title);
                            String exercise = e.getText().toString();

                            TextView text = c.findViewById(R.id.title);
                            String t = text.getText().toString();
                            if (c != v) {
                                System.out.println("HERE -----> " + c);
                                int height = c.findViewById(R.id.group).getHeight();
                                if (height > defaultHeight && defaultHeight != -1){
                                    c.performClick();
                                }
                            }
                        }

                        params.height = defaultHeight*2;
                        rlNested.setLayoutParams(params);

                        imgParams.topMargin += 40;
                        imgParams.height *= 2;
                        imgParams.width *= 2;

                        RelativeLayout.LayoutParams descriptionParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        description.setId(View.generateViewId());
                        description.setText("description:");
                        descriptionParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        descriptionParams.addRule(RelativeLayout.BELOW, R.id.group_image);
                        descriptionParams.topMargin = 20;
                        description.setLayoutParams(descriptionParams);
                        rlNested.addView(description);
                    }
                    else if (rlNested.getHeight() > defaultHeight){

                        params.height = defaultHeight;
                        rlNested.setLayoutParams(params);
                        rlNested.removeView(description);

                        imgParams.topMargin -= 40;
                        imgParams.height /= 2;
                        imgParams.width /= 2;
                    }
                    */
                }
            });

            cardsList.add(card);
            linearLayout.addView(card);
        }

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
