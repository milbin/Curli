package com.fitness.curli;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;

public class WorkoutTimer{
    private long startTime;
    private String currentTime;
    private TextView textView;

    //runs without a timer by reposting this handler at the end of the runnable
    final Handler timerHandler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int hours = seconds / 3600;
            int minutes = (seconds % 3600)/60;
            seconds = (seconds % 3600)% 60;

            if(hours >= 1) {
                textView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }else{
                textView.setText(String.format("%02d:%02d", minutes, seconds));
            }
            timerHandler.postDelayed(this, 500);
        }
    };
    public void startTimer(){
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(runnable, 0);
    }

    public void setTextView(TextView tv){
        textView = tv;
    }
    public void stopTimer(){
        timerHandler.removeCallbacks(runnable);
    }
}
