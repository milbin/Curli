package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class WorkoutBuilder extends AppCompatActivity {
    Context context = this;
    ArrayList<ArrayList> exercises = new ArrayList<ArrayList>();
    EditText title;
    int workoutNumber = -1;

    //This is an example of the workout data structure that is created and returned through the intent when this activity is finished by the user
    //{"title": "Arms and Chest", "exercises": [[0, {"title": "Bench Press", "weight":135, "reps":8},
    //{"title": "Bench Press", "weight":135, "reps":8}, {"title": "Bench Press", "weight":135, "reps":8}],
    //[0, {"title": "Bicep Curl", "weight":75, "reps":8}, {"title": "Bicep Curl", "weight":75, "reps":8},
    //{"title": "Bicep Curl", "weight":75, "reps":8}]]}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_builder);
        findViewById(R.id.back_button).setOnClickListener(new onBackButtonClicked());
        findViewById(R.id.fab).setOnClickListener(new onFabClick());

        //setup editable title
        SQLData sqlData = new SQLData();
        sqlData.openUserDB(this);
        ((TextView)findViewById(R.id.title_toolbar)).setText("Workout " + (sqlData.getWorkoutCount() + 1));
        title = findViewById(R.id.title);
        title.setText("Workout " + (sqlData.getWorkoutCount() + 1));
        title.setOnKeyListener(new onTitleDoneButtonPressed());
        title.setOnFocusChangeListener(new onTitleEdit());
        findViewById(R.id.edit_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.requestFocus();
                title.setSelection(title.getText().length()); //places carret at end of edittext
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);

            }

        });

        //get intent
        Intent intent = getIntent();
        HashMap currentWorkout = (HashMap) intent.getSerializableExtra("workout");
        if(currentWorkout != null){
            workoutNumber = intent.getIntExtra("workoutNumber", -1);
            exercises = (ArrayList<ArrayList>)currentWorkout.get("exercises");
            // convert double reps to integer
            int count = 0;
            for(ArrayList<Object> exercise:exercises) {
                int count1 = 0;
                for(Object set:exercise) {
                    if (count1 != 0) {
                        ((HashMap)set).put("reps", (int)Math.round((double)((HashMap)set).get("reps")));
                        exercise.set(count1, set);
                    }else{

                        exercise.set(0, (int)Math.round((double)set));
                    }
                    count1++;
                }
                exercises.set(count, exercise);
                count++;
            }
            title.setText((String)currentWorkout.get("title"));

            //add exercises from previous workoutBuilder
            for(ArrayList<HashMap> exercise:exercises) {
                String exerciseTitle = (String)exercise.get(1).get("title");
                setupCard(exerciseTitle, exercise);
            }


        }

        //create finish button
        Button finishButton = new Button(this);
        finishButton.setText("FINISH");
        finishButton.setTextAppearance(this, R.style.Button);
        finishButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        finishButton.setOnClickListener(new onFinishClick());
        RelativeLayout.LayoutParams finishButtonLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        finishButtonLP.addRule(RelativeLayout.ALIGN_PARENT_END);
        finishButtonLP.addRule(RelativeLayout.CENTER_VERTICAL);
        finishButton.setLayoutParams(finishButtonLP);
        finishButton.setBackgroundColor(Color.TRANSPARENT);
        ((RelativeLayout) findViewById(R.id.toolbar_rl)).addView(finishButton);

        //setup toolbar expanding and collapsing functionality
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
                int actionBarSize = (int) styledAttributes.getDimension(0, 0);
                styledAttributes.recycle();
                float alpha = (float)1-(Math.abs((float)verticalOffset)/(appBarLayout.getTotalScrollRange()));
                findViewById(R.id.edit_title).setAlpha(alpha);
                findViewById(R.id.title_toolbar).setAlpha((float)1-alpha);
                findViewById(R.id.expandedRL).setAlpha(alpha);
                findViewById(R.id.toolbar_divider_collapsible).setAlpha(alpha);
                findViewById(R.id.toolbar_divider).setAlpha((float)1-alpha);
            }
        });


    }

    public class onFinishClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (exercises.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                builder.setTitle("Please Add Exercises").setMessage("You cannot finish building this workout without adding any exercises!");
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                builder.setTitle("Finish Building Workout?").setMessage("Are you sure you want to finish building this workout? It will be added to your workouts list.");
                // Add the buttons
                builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        //TODO add some sort of mechanism that saves newly created workouts
                        HashMap workout = new HashMap();
                        workout.put("title", title.getText().toString());
                        workout.put("exercises", exercises);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("workout", workout);

                        setResult(1, returnIntent);
                        SQLData sqlData = new SQLData();
                        sqlData.openUserDB(context);
                        if(workoutNumber != -1){
                            sqlData.updateWorkout(workoutNumber, workout);
                        }else {
                            sqlData.saveWorkout(workout);
                        }
                        sqlData.closeDB();
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        }
    }

    public class onBackButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (exercises.isEmpty()) {
                onBackPressed();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                builder.setTitle("Discard Workout?").setMessage("Are you sure you want to discard this workout? This action is irreversible.");
                // Add the buttons
                builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    public class onFabClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(WorkoutBuilder.this, MuscleView.class);
            intent.putExtra("FromWorkoutBuilder", true);
            startActivityForResult(intent, 1);



        }
    }

    public class AddSetButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            addSet(v, 0.0, 0);
        }
    }

    private void addSet(View v, double weight, int reps){
        LinearLayout linearLayout = (LinearLayout) (v.getParent()).getParent().getParent();
        int exerciseNumber = 0;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) == ((View) v.getParent()).getParent()) {
                exerciseNumber = i;
                break;
            }
        }
        View weightAndReps = LayoutInflater.from(context).inflate(R.layout.weight_and_reps, null);
        LinearLayout ll = ((View) v.getParent()).findViewById(R.id.weight_and_reps_ll);
        ll.addView(weightAndReps);
        weightAndReps.findViewById(R.id.weight_add_button).setOnClickListener(new onAddOrSubtractClick());
        weightAndReps.findViewById(R.id.weight_subtract_button).setOnClickListener(new onAddOrSubtractClick());
        weightAndReps.findViewById(R.id.reps_add_button).setOnClickListener(new onAddOrSubtractClick());
        weightAndReps.findViewById(R.id.reps_subtract_button).setOnClickListener(new onAddOrSubtractClick());
        EditText exerciseReps = weightAndReps.findViewById(R.id.exercise_reps);
        exerciseReps.setOnFocusChangeListener(new onUserFinishedEditing());
        exerciseReps.setOnKeyListener(new onEditTextDoneButtonPressed());
        EditText exerciseWeight = weightAndReps.findViewById(R.id.exercise_weight);
        exerciseWeight.setOnFocusChangeListener(new onUserFinishedEditing());
        exerciseWeight.setOnKeyListener(new onEditTextDoneButtonPressed());
        CharSequence title = ((TextView) ((View) v.getParent()).findViewById(R.id.exercise_name)).getText();
        LinkedHashMap set = new LinkedHashMap<>();
        ((TextView)weightAndReps.findViewById(R.id.sets_completed)).setText("SET "+ll.getChildCount());
        if(weight != -1&&reps != -1) {
            //TODO this title needs to be changed in order to support a superset
            set.put("title", title);
            set.put("weight", weight);
            set.put("reps", reps);
            ArrayList setList = exercises.get(exerciseNumber);//get old set list and add new set to it
            setList.add(set);
            //setList.set(0, (int)setList.get(0)+1);
            //exercises.set(exerciseNumber, setList);
            updateWorkoutStats();
        }

    }
    public class RemoveSetButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LinearLayout ll = ((View) v.getParent()).findViewById(R.id.weight_and_reps_ll);
            if (ll.getChildCount() != 1) {
                ll.removeViewAt(ll.getChildCount() - 1);
            }
            int exerciseNumber = 0;
            for (int i = 0; i < ll.getChildCount(); i++) {
                if (ll.getChildAt(i) == ((View) v.getParent()).getParent()) {
                    exerciseNumber = i;
                    break;
                }
            }
            exercises.get(exerciseNumber).remove(ll.getChildCount());
            updateWorkoutStats();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) { //hide keyboard and unfocus current edittext if click outside of edittext
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if(v!=null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public class onEditTextDoneButtonPressed implements EditText.OnKeyListener{
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                //this will call onUserFinishedEditing (just below)
                v.clearFocus();
                return true;
            }
            return false;
        }
    }
    public class onTitleDoneButtonPressed implements EditText.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                v.clearFocus();
                return true;
            }
            return false;
        }
    }
    public class onTitleEdit implements EditText.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            ((TextView)findViewById(R.id.title_toolbar)).setText(((EditText)v).getText());
        }
    }

    public class onUserFinishedEditing implements EditText.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                LinearLayout linearLayout = (LinearLayout)((View)((View)v.getParent()).getParent()).getParent().getParent().getParent().getParent();
                int exerciseNumber = 0;
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    if(linearLayout.getChildAt(i) == ((View)v.getParent()).getParent().getParent().getParent().getParent()){
                        exerciseNumber = i;
                        break;
                    }
                }
                int setNumber = 0;
                LinearLayout linearLayoutSets = (LinearLayout)v.getParent().getParent().getParent();
                for (int i = 0; i < linearLayoutSets.getChildCount(); i++) {
                    if(linearLayoutSets.getChildAt(i) == ((View)v.getParent()).getParent()){
                        setNumber = i+1;
                        break;
                    }
                }
                if(setNumber > exercises.get(exerciseNumber).size()-1){
                    setNumber = exercises.get(exerciseNumber).size()-1;
                }
                if(((EditText) v).getText().toString().length() != 0) {
                    if (v.getId() == R.id.exercise_reps) {
                        Integer currentReps = Integer.parseInt(((EditText) v).getText().toString());
                        ((HashMap) exercises.get(exerciseNumber).get(setNumber)).put("reps", currentReps);
                    } else if (v.getId() == R.id.exercise_weight) {
                        Double currentWeight = Double.parseDouble(((EditText) v).getText().toString());
                        ((HashMap) exercises.get(exerciseNumber).get(setNumber)).put("weight", currentWeight);
                    }
                }
            }

            updateWorkoutStats();
        }
    }

    public class onAddOrSubtractClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RelativeLayout relativeLayout = (RelativeLayout)v.getParent();
            LinearLayout linearLayout = (LinearLayout)((View)((View)v.getParent()).getParent()).getParent().getParent().getParent().getParent();
            int exerciseNumber = 0;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if(linearLayout.getChildAt(i) == ((View)v.getParent()).getParent().getParent().getParent().getParent()){
                    exerciseNumber = i;
                    break;
                }
            }
            int setNumber = 0;
            LinearLayout linearLayoutSets = (LinearLayout)v.getParent().getParent().getParent();
            for (int i = 0; i < linearLayoutSets.getChildCount(); i++) {
                if(linearLayoutSets.getChildAt(i) == ((View)v.getParent()).getParent()){
                    setNumber = i+1;
                    break;
                }
            }
            if(setNumber > exercises.get(exerciseNumber).size()-1){
                setNumber = exercises.get(exerciseNumber).size()-1;
            }
            if(v.getId() == R.id.weight_add_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_weight);
                Double currentWeight = Double.parseDouble(editText.getText().toString()) + 2.5;
                String currentWeightString = String.valueOf(currentWeight);
                editText.setText(currentWeightString);
                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("weight", currentWeight);
            }
            if(v.getId() == R.id.weight_subtract_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_weight);
                Double currentWeight = Double.parseDouble(editText.getText().toString()) - 2.5;
                if(currentWeight <= 0){
                    currentWeight = 0.0;
                }
                String currentWeightString = String.valueOf(currentWeight);
                editText.setText(currentWeightString);
                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("weight", currentWeight);


            }
            if(v.getId() == R.id.reps_add_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_reps);
                Integer currentReps = Integer.parseInt(editText.getText().toString()) + 1;
                String currentRepsString = String.valueOf(currentReps);
                editText.setText(currentRepsString);

                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("reps", currentReps);

            }
            if(v.getId() == R.id.reps_subtract_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_reps);
                Integer currentReps = Integer.parseInt(editText.getText().toString()) - 1;
                if(currentReps <= 0){
                    currentReps = 0;
                }
                String currentRepsString = String.valueOf(currentReps);
                editText.setText(currentRepsString);
                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("reps", currentReps);

            }
            updateWorkoutStats();
        }
    }

    private void updateWorkoutStats(){
        RelativeLayout rl = findViewById(R.id.expandedRL);
        SQLData sqlDataExercise = new SQLData();
        sqlDataExercise.openExerciseDB(this);
        int totalReps = 0;
        int totalSets = 0;
        int totalExercises = 0;
        ArrayList<String> equipmentList = new ArrayList<String>();
        for(ArrayList exercise:exercises){
            for(int setNum=1;setNum<exercise.size();setNum++){
                HashMap set = (HashMap) exercise.get(setNum);
                String equipmentString = sqlDataExercise.getEquipmentFromName((String)set.get("title"));
                for(String equipment: equipmentString.split(", ")){
                    if(!equipmentList.contains(equipment)){
                        equipmentList.add(equipment);
                    }
                }
                totalReps += (int)set.get("reps");
                totalSets++;
            }
            totalExercises++;
        }
        String finalEquipmentString = "";
        for(String equipment: equipmentList){
            finalEquipmentString += equipment+" · ";
        }
        finalEquipmentString = finalEquipmentString.substring(0, finalEquipmentString.length() - 3);
        ((TextView)rl.findViewById(R.id.time)).setText("~"+(((totalReps*5)+(totalSets*60))/60)+" mins"); //TODO change the 60 second rest time to the rest period of the user defined in their profile
        ((TextView)rl.findViewById(R.id.number_of_exercises)).setText(totalExercises +" Exercises");
        ((TextView)rl.findViewById(R.id.equipment)).setText(finalEquipmentString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            ArrayList<String> list = data.getStringArrayListExtra("exercisesToAdd");

            for(String title:list) {
                LinkedHashMap set = new LinkedHashMap<>();
                set.put("title", title);
                set.put("weight", 0.0);
                set.put("reps", 0);
                ArrayList setList = new ArrayList();
                setList.add(0);
                setList.add(set);
                exercises.add(setList);
                setupCard(title, setList);

            }
        }
    }

    private void setupCard(String title, ArrayList<HashMap> setList){
        LinearLayout ll = findViewById(R.id.workout_builder_ll);
        View card = LayoutInflater.from(context).inflate(R.layout.workout_builder_card, null);
        ((TextView) card.findViewById(R.id.exercise_name)).setText(title);
        card.findViewById(R.id.add_set_button).setOnClickListener(new AddSetButton());
        card.findViewById(R.id.remove_set_button).setOnClickListener(new RemoveSetButton());
        card.findViewById(R.id.weight_add_button).setOnClickListener(new onAddOrSubtractClick());
        card.findViewById(R.id.weight_subtract_button).setOnClickListener(new onAddOrSubtractClick());
        card.findViewById(R.id.reps_add_button).setOnClickListener(new onAddOrSubtractClick());
        card.findViewById(R.id.reps_subtract_button).setOnClickListener(new onAddOrSubtractClick());
        findViewById(R.id.no_exercise_tv).setVisibility(View.GONE);
        EditText exerciseReps = card.findViewById(R.id.exercise_reps);
        exerciseReps.setOnFocusChangeListener(new onUserFinishedEditing());
        exerciseReps.setOnKeyListener(new onEditTextDoneButtonPressed());
        EditText exerciseWeight = card.findViewById(R.id.exercise_weight);
        exerciseWeight.setOnFocusChangeListener(new onUserFinishedEditing());
        exerciseWeight.setOnKeyListener(new onEditTextDoneButtonPressed());
        ll.addView(card);
        int setListSize = setList.size();
        for(int i=2; i<setListSize; i++){ //the reason this starts at 2 is because there is already 1 set that comes with the card and the setnumber is first index
            View setButton = card.findViewById(R.id.add_set_button);
            addSet(setButton, -1, -1);

        }
        updateWorkoutStats();

    }


}
