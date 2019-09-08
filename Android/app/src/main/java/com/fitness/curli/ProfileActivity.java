package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    RelativeLayout relativeLayout;
    ProgressDialog dialog;
    Context context;
    Menu menu;

    //initialize variables
    String userName;
    String userEmail;
    String currentWeight;
    String weightGoal;
    Boolean isNotifications;
    String unitChoice;
    String sexChoice;
    String heightUnit;

    //set weighing options
    String[] weights={"Pounds","Kilograms"};

    //set sex options
    String[] sexes={"Male","Female"};

    //set height options
    String[] heights={"Feet","Meters"};

    Button changeEmail;
    Button changePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        //retrieve values from edit texts into java variables

        final EditText et = (EditText) findViewById(R.id.name_input);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    userName = et.getText().toString();
                    System.out.println(userName);
                }
            }
        });


        //eventually to add email change option
        /*final EditText et1 = (EditText) findViewById(R.id.email_input);
        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    userEmail = et1.getText().toString();
                    System.out.println(userEmail);
                }
            }
        }); */

        changeEmail = (Button) findViewById(R.id.emailButton);
        changePassword = (Button) findViewById(R.id.passwordButton);

        changeEmail.setOnClickListener(ProfileActivity.this);
        changePassword.setOnClickListener(ProfileActivity.this);

        final EditText et2 = (EditText) findViewById(R.id.current_weight_input);
        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    currentWeight = et2.getText().toString();
                    System.out.println(currentWeight);
                }
            }
        });

        final EditText et3 = (EditText) findViewById(R.id.desired_weight_input);
        et3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    weightGoal = et3.getText().toString();
                    System.out.println(weightGoal);
                }
            }
        });

        //initiate the switch
        Switch notification_switch = (Switch) findViewById(R.id.notification_switch);

        // check current state of a Switch (true or false)
        //set on switch listener
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                isNotifications = isChecked;
                System.out.println(isNotifications);
            }
        });

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,weights);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        Spinner spinSex = (Spinner) findViewById(R.id.sexSpinner);

        spinSex.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter sexA = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sexes);
        sexA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spinSex.setAdapter(sexA);

        Spinner spinHeight = (Spinner) findViewById(R.id.heightSpinner);

        spinHeight.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter heightA = new ArrayAdapter(this,android.R.layout.simple_spinner_item, heights);
        heightA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spinHeight.setAdapter(heightA);

        /*TODO finish sql save data, array, method, table
        SQLData db = new SQLData();
        db.saveProfile(1, ArrayList MAKEANARRAY);
        */


        System.out.println("only runs on initil open");

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.emailButton:
                System.out.println("email button pressed");
                break;

            case R.id.passwordButton:
                System.out.println("password button pressed");
                break;

            /*case R.id.threeButton:
                // do your code
                break;
                */

            default:
                break;
        }
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

        if(heights[position].equals("Feet")){
            heightUnit = "Feet";
        }
        else if(heights[position].equals("Meters")){
            heightUnit = "Meters";
        }

        if(sexes[position].equals("Female")){
            sexChoice = "Female";
        }
        else if(sexes[position].equals("Male")){
            sexChoice = "Male";
        }

        if(weights[position].equals("Pounds")){
            unitChoice = "lbs";
        }
        else if(weights[position].equals("Kilograms")){
            unitChoice = "kg";
        }

        System.out.println("testing location");
        System.out.println(sexChoice);
        System.out.println(heightUnit);
        System.out.println(unitChoice);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO (Auto-generated method stub)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.back_button) {
            finish();
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

        }



    //all getters which will be used to store data in database
    public String getUsername(){
        return userName;
    }
    public String getEmail(){
        return userEmail;
    }
    public int getCurrentWeight() {
        int finalValue=Integer.parseInt(currentWeight);
        return finalValue;
    }
    public int getWeightGoal(){
        int finalValue=Integer.parseInt(weightGoal);
        return finalValue;
    }
    public boolean getNotifications(){
        return isNotifications;
    }
    public String getUnitChoice(){
        return unitChoice;
    }
    public String getSex(){
        return sexChoice;

    }


}
