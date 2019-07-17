package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class MuscleView extends AppCompatActivity {
    SQLData data = new SQLData();
    ArrayList<String> muscles;
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscles_view);

        dialog = ProgressDialog.show(MuscleView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.MusclesViewLinearLayout);
        Intent intent = getIntent();
        muscles = intent.getStringArrayListExtra("muscles");

        getMuscles();
    }

    public void getMuscles(){
        int length = muscles.size();

        for (int i = 0; i < length; i++){
            final View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(muscles.get(i));

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    TextView rl = v.findViewById(R.id.title);
                    String muscle = rl.getText().toString();
                    ArrayList<String> exercises = data.MUSCLE_TO_EXCERCISE.get(muscle);
                    Intent intent = new Intent(MuscleView.this, ExerciseView.class);
                    intent.putExtra("exercises", exercises);
                    startActivity(intent);
                }
            });

            ImageView info = card.findViewById(R.id.info);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView t = card.findViewById(R.id.title);
                    System.out.println(t);
                    String title = t.getText().toString();

                    Intent intent = new Intent(MuscleView.this, Info_View.class);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            });

            linearLayout.addView(card);
        }

    dialog.dismiss();
    }
}

