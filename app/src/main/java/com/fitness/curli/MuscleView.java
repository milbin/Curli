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
import java.util.Arrays;
import java.util.LinkedHashMap;

public class MuscleView extends AppCompatActivity {
    ArrayList<String> muscles;
    private Context context;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    LinkedHashMap<String, ArrayList<String>> muscleToExercise = new LinkedHashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscles_view);

        dialog = ProgressDialog.show(MuscleView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.MusclesViewLinearLayout);
        muscleToExercise.put("biceps",  new ArrayList<String>(Arrays.asList("dumbbell curl", "barbell curl")));
        muscleToExercise.put("triceps", new ArrayList<String>(Arrays.asList("skull crushers", "rope extension")));
        muscleToExercise.put("quads",   new ArrayList<String>(Arrays.asList("leg extension", "squat")));
        muscleToExercise.put("hams",    new ArrayList<String>(Arrays.asList("leg curl", "deadlift")));
        muscleToExercise.put("abs",     new ArrayList<String>(Arrays.asList("crunches", "sit ups")));
        muscleToExercise.put("lats",    new ArrayList<String>(Arrays.asList("pull up", "")));
        Intent intent = getIntent();
        muscles = intent.getStringArrayListExtra("muscles");

        getMuscles();
    }

    public void getMuscles(){
        int length = muscles.size();

        for (int i = 0; i < length; i++){
            View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(muscles.get(i));

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    TextView rl = v.findViewById(R.id.title);
                    String muscle = rl.getText().toString();
                    ArrayList<String> exercises = muscleToExercise.get(muscle);
                    Intent intent = new Intent(MuscleView.this, ExerciseView.class);
                    intent.putExtra("exercises", exercises);
                    startActivity(intent);
                }
            });

            linearLayout.addView(card);
        }

    dialog.dismiss();
    }
}

