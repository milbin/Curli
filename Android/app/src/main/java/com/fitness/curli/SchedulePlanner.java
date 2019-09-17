package com.fitness.curli;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class SchedulePlanner extends AppCompatActivity {

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_planner);
        findViewById(R.id.edit_button).setOnClickListener(new onEditButtonClick());



    }
    public class onEditButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final Dialog mBottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
            View view = LayoutInflater.from(context).inflate(R.layout.edit_workout_schedule_popup, null);
            mBottomSheetDialog.setContentView(view); // your custom view.
            mBottomSheetDialog.setCancelable(true);
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            float dp = 450f;
            float fpixels = metrics.density * dp;
            int pixels = (int) (fpixels + 0.5f);
            mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, pixels);
            mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
            mBottomSheetDialog.show();
        }
    }


}
