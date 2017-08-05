package com.example.rohan.miniproject.Profile;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rohan.miniproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Rohan on 30/03/17.
 */
public class RealtimeUpdatesfriends extends Fragment {
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 5d;
    boolean isrunning = true;
    Calendar calendar;
    TextView textView;
    Button btntogglegraph;

    TextView txtmaxbpm;
    int maxheartbeat = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
        textView = (TextView)rootView.findViewById(R.id.txtheartrate);
        btntogglegraph = (Button)rootView.findViewById(R.id.btntogglegraphflow);
        txtmaxbpm = (TextView)rootView.findViewById(R.id.txtbpmlimit);


        FirebaseDatabase.getInstance().getReference().child("Users").child(getArguments().getString("uid")).child("MaxBPM").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    txtmaxbpm.setText("Not set");


                }
                else {
                    txtmaxbpm.setText(dataSnapshot.getValue().toString());
                    maxheartbeat = Integer.parseInt(dataSnapshot.getValue().toString());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);

        /*GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>(generateData());
        graph.addSeries(mSeries1);*/

        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2);
        mSeries2 = new LineGraphSeries<>();
        graph2.addSeries(mSeries2);
        graph2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){



            @Override
            public String formatLabel(double value, boolean isValueX) {

                if (isValueX) {
                    // show normal x values

                    return new SimpleDateFormat("hh:mm").format(value);
                }
                else
                    return super.formatLabel(value,isValueX);
            }
        });
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(d1.getTime());
        graph2.getViewport().setMaxX(d1.getTime() + 20000);
       /* graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setMinY(0);
        graph2.getViewport().setMaxY(4);*/
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);

        return rootView;
    }

   /* @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }
    */
    @Override
    public void onResume() {
        super.onResume();
       /* mTimer1 = new Runnable() {
            @Override
            public void run() {
                mSeries1.resetData(generateData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);*/


        btntogglegraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isrunning == true) {
                    mHandler.removeCallbacks(mTimer2);
                    btntogglegraph.setText("Resume");
                    isrunning = false;
                } else {
                    mHandler.post(mTimer2);
                    btntogglegraph.setText("Pause");
                    isrunning = true;
                }
            }
        });

       /* ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                graph2LastXValue += 1d;
                Double previousbeatlong = (Double)(dataSnapshot.getValue());
                //String heartbeat = dataSnapshot.getValue().toString();

                int beat = previousbeatlong.intValue();


                mSeries2.appendData(new DataPoint(graph2LastXValue, beat), true, 5000);
                if(maxheartbeat - beat < 10) {
                    textView.setTextColor(Color.RED);
                    textView.setText(String.valueOf(beat));
                }
                else
                {
                    textView.setTextColor(Color.BLACK);
                    textView.setText(String.valueOf(beat));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Heartbeat").addValueEventListener(valueEventListener);
        */

        mTimer2 = new Runnable() {
            @Override
            public void run() {


                graph2LastXValue += 1d;


                mSeries2.appendData(new DataPoint(Calendar.getInstance().getTime(), getRandom()), true, 5000);
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(mTimer2, 100);

    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }


    int min= 50;
    int max = 150;
    Double mRand = Math.random();
    private int getRandom() {


        int random = (int )(Math.random() * max + 40);

        if(maxheartbeat - random < 10) {
            textView.setTextColor(Color.RED);
            textView.setText(String.valueOf(random));
        }
        else
        {
            textView.setTextColor(Color.BLACK);
            textView.setText(String.valueOf(random));
        }


       /* if(maxheartbeat - Math.floor(mLastRandom*100)/100 < 0.8) {
            textView.setTextColor(Color.RED);
            textView.setText(String.valueOf(Math.floor(mLastRandom * 100) / 100));
        }
        else
        {
            textView.setTextColor(Color.BLACK);
            textView.setText(String.valueOf(Math.floor(mLastRandom*100)/100));
        }
        */
        return random;

      /*  ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLastRandom = (Long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Heartbeat").addValueEventListener(valueEventListener);
        return mLastRandom;*/
    }
}