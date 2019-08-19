package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    ProgressDialog dialog;
    Context context;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setTitle("Profile/Settings");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        // initiate a Switch
        Switch simpleSwitch = (Switch) findViewById(R.id.simpleSwitch);

        // check current state of a Switch (true or false).
        Boolean switchState = simpleSwitch.isChecked();

        System.out.println("IT WORKED FOOOO0LLL");
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
