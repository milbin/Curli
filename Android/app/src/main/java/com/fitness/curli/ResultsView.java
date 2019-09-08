package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.Calendar;
import java.util.Date;

public class ResultsView extends AppCompatActivity {
    ProgressDialog dialog;
    Context context;
    boolean fabClick = false;
    LinearLayout fabList;
    LinearLayout resultsLinearLayout;
    FloatingActionButton fab;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");

        dialog = ProgressDialog.show(this, "Loading...", "", false);

        context = getApplicationContext();

        displayResults();

        dialog.dismiss();
    }


    public void displayResults(){
        resultsLinearLayout = findViewById(R.id.resultsList);
        fab = findViewById(R.id.fab);
        final String[] resultsList = new String[]{"Weight", "Blood pressure", "Muscle Mass"};
        fabList = findViewById(R.id.fab_list);
        for (int index = 0; index < resultsList.length; index++){
            String resultName = resultsList[index];
            RelativeLayout card = new RelativeLayout(this);
            card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            if (index == 0){
                System.out.println("ROUNDED");
                card.setBackground(ContextCompat.getDrawable(this, R.drawable.white_card_rounded_top));
            }
            else if (index == resultsList.length-1){
                card.setBackground(ContextCompat.getDrawable(this, R.drawable.white_card_rounded_bottom));
            }
            else {
                card.setBackgroundColor(getResources().getColor(R.color.white));
            }
            TextView title = new TextView(this);
            title.setId(R.id.title);
            title.setText(resultName);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            card.setGravity(Gravity.CENTER_HORIZONTAL);
            card.addView(title);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View resultCard = LayoutInflater.from(context).inflate(R.layout.result_card_expanded, null);
                    final RelativeLayout recordResultWrapper = resultCard.findViewById(R.id.recordResultWrapper);
                    recordResultWrapper.setVisibility(View.GONE);

                    final GraphView graph = resultCard.findViewById(R.id.graph);
                    final LineGraphSeries series = new LineGraphSeries();
                    series.setThickness(10);
                    graph.addSeries(series);
                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));

                    graph.getViewport().setXAxisBoundsManual(true);

                    graph.getGridLabelRenderer().setHumanRounding(false);

                    final EditText editText = resultCard.findViewById(R.id.editText);

                    TextView titleText = v.findViewById(R.id.title);
                    String titleTextString = titleText.getText().toString();
                    TextView title = resultCard.findViewById(R.id.title);
                    title.setText(titleTextString);

                    Button button = resultCard.findViewById(R.id.button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button vButton = (Button) v;
                            TextView nothing = findViewById(R.id.nothing);
                            nothing.setVisibility(View.GONE);
                            if (recordResultWrapper.getVisibility() == View.GONE) {
                                vButton.setText("DONE");
                                recordResultWrapper.setVisibility(View.VISIBLE);
                            }
                            else if (recordResultWrapper.getVisibility() == View.VISIBLE){
                                Calendar calendar = Calendar.getInstance();
                                vButton.setText("RECORD RESULT");
                                double y = Double.valueOf(editText.getText().toString());
                                //double x = series.getHighestValueX()+1;
                                Date x = calendar.getTime();
                                series.appendData(new DataPoint(x, y), true, 10);
                                graph.addSeries(series);
                                recordResultWrapper.setVisibility(View.GONE);
                            }
                        }
                    });
                    resultsLinearLayout.addView(resultCard);
                    fab.callOnClick();
                }
            });
            fabList.addView(card);
        }
        fabList.setVisibility(View.GONE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabClick) {
                    fabList.setVisibility(View.GONE);
                    fabClick = false;
                }
                else if (!fabClick){
                    fabList.setVisibility(View.VISIBLE);
                    fabClick = true;
                }
            }
        });
        /*
        String[] resultsList = new String[]{"Weight", "Blood pressure", "Muscle Mass"};
        LinearLayout resultLinearLayout = findViewById(R.id.resultsList);

        for (String result : resultsList){
            View card = LayoutInflater.from(context).inflate(R.layout.result_card, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(result);
            resultLinearLayout.addView(card);
        }
        */
    }
}
