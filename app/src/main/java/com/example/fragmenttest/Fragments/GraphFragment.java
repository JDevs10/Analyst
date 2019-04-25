package com.example.fragmenttest.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.GraphValues;
import com.example.fragmenttest.utilies.DateAxiesFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphFragment extends Fragment {

    private View v;
    private Context mContext;
    private String TAG = GraphFragment.class.getSimpleName();

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> series;
    private GraphView graph;
    private Spinner sp_timeFilter;
    private String sp_timeFilterSelected = "by all time";

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //yyyy-MM-dd
    private DatabaseHelper db;

    private double currentAmount = 0.0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container,false);;

        sp_timeFilter = (Spinner) v.findViewById(R.id.spinner_fragmentGraph_value_filter);
        graph = (GraphView) v.findViewById(R.id.fragmentGraph_graph);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get graph values from tickets in the database
        series = new LineGraphSeries<>(setGraphValues(getGraphValuesOfAllTime()));

        //customize series
        series.setTitle("Global graph");
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(7);
        series.setColor(Color.WHITE);
        //series.setThickness(5);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Log.e(TAG, "Series: "+series.getTitle()+" \n" +
                        "DataPoint X: "+dataPoint.getX()+" ; Y: "+dataPoint.getY());
            }
        });

        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long)value));
                }
                return super.formatLabel(value, isValueX);
            }
        });

        // set date label formatter
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(getGraphValuesOfAllTime().get(0).getDateInLong());
        //graph.getViewport().setMaxX(getGraphValues().get(getGraphValues().size()-1).getDateInLong());
        graph.getViewport().setXAxisBoundsManual(true);

        //set graph zooming and scaling
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        //graph time filter
        String[] timeFilter = {"by all time", "last year", "last month", "last week", "last 24h"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, timeFilter);
        sp_timeFilter.setAdapter(adapter);
        sp_timeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_timeFilterSelected = adapterView.getSelectedItem().toString();
                switch (sp_timeFilterSelected){
                    case "by all time":
                        sdf = new SimpleDateFormat("yyyy-MM"); //yyyy-MM-dd
                        graph.removeSeries(series);
                        series.resetData(setGraphValues(getGraphValuesOfAllTime()));
                        graph.addSeries(series);
                        break;
                    case "last year":
                        sdf = new SimpleDateFormat("yyyy-MMM"); //yyyy-MM-dd
                        graph.removeSeries(series);
                        series.resetData(setGraphValues(getGraphValuesOfTheLastYear()));
                        graph.addSeries(series);
                        break;
                    case "last month":
                        sdf = new SimpleDateFormat("MMM-d"); //yyyy-MM-dd
                        graph.removeSeries(series);
                        series.resetData(setGraphValues(getGraphValuesOfTheLastMonth()));
                        graph.addSeries(series);
                        break;
                    case "last week":
                        sdf = new SimpleDateFormat("EEE-d"); //yyyy-MM-dd
                        graph.removeSeries(series);
                        series.resetData(setGraphValues(getGraphValuesOfTheLastWeek()));
                        graph.addSeries(series);
                        break;
                    case "last 24h":
                        sdf = new SimpleDateFormat("HH:mm"); //yyyy-MM-dd
                        graph.removeSeries(series);
                        series.resetData(setGraphValues(getGraphValuesOfTheLast24H()));
                        graph.addSeries(series);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private DataPoint[] setGraphValues(ArrayList<GraphValues> getGraphValues){
        DataPoint[] dp = new DataPoint[(getGraphValues.size())];
        currentAmount = getStartAmount();

        if(getGraphValues.size() != 0) {
        //Log.e(TAG, " Start Amount : "+getStartAmount()+" || Current Amount : "+currentAmount+" || DateInLong : "+getStartAmountDate());
        dp[0] = new DataPoint(getStartAmountDate(), getStartAmount());

            for (int i = 1; i < getGraphValues.size(); i++) {
                if (getGraphValues.get(i).getTransaction().equals("+")) {
                    dp[i] = new DataPoint(getGraphValues.get(i).getDateInLong(), (currentAmount += getGraphValues.get(i).getCurrency()));
                }
                if (getGraphValues.get(i).getTransaction().equals("-")) {
                    dp[i] = new DataPoint(getGraphValues.get(i).getDateInLong(), (currentAmount -= getGraphValues.get(i).getCurrency()));
                }
                Log.e(TAG, " Current Amount in graph : " + currentAmount + " size: " + i);
            }
        }else {
            Toast.makeText(mContext, "No ticket data found in the "+sp_timeFilterSelected, Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, " setGraphValues => done! ");
        return dp;
    }

    private double getStartAmount(){
        Cursor res = db.getAllSettingsData();
        double startAmount = 0.0;
        while (res.moveToNext()){
            startAmount = res.getDouble(1);
        }
        return startAmount;
    }

    private long getStartAmountDate(){
        Cursor res = db.getAllSettingsData();
        long startAmountDate = 0;
        while (res.moveToNext()){
            startAmountDate = res.getLong(5);
        }
        return startAmountDate;
    }

    private ArrayList<GraphValues> getGraphValuesOfAllTime(){
        Cursor res = db.getAllTicketData();
        ArrayList<GraphValues> values = new ArrayList<>();

        //set first value
        values.add(new GraphValues(getStartAmount(),getStartAmountDate()));

        while(res.moveToNext()){
            GraphValues graphValues = new GraphValues();
            graphValues.setTransaction(res.getString(3));
            graphValues.setCurrency(res.getDouble(4));
            graphValues.setDateInLong(res.getLong(7));
            values.add(graphValues);

            //Log.e(TAG, " X : "+res.getLong(7)+" || Y :"+res.getDouble(4)+" || Transaction : "+res.getString(3));
        }
        Log.e(TAG, " getGraphValuesOfAllTime => done! ");
        return values;
    }

    private ArrayList<GraphValues> getGraphValuesOfTheLastYear(){
        Cursor res = db.getAllTicketDataOfTheLastYear();
        ArrayList<GraphValues> values = new ArrayList<>();

        while(res.moveToNext()){
            GraphValues graphValues = new GraphValues();
            graphValues.setTransaction(res.getString(3));
            graphValues.setCurrency(res.getDouble(4));
            graphValues.setDateInLong(res.getLong(7));
            values.add(graphValues);

            //Log.e(TAG, " X : "+res.getLong(7)+" || Y :"+res.getDouble(4)+" || Transaction : "+res.getString(3));
        }
        Log.e(TAG, " getGraphValuesOfTheLastWeek => done! ");
        return values;
    }

    private ArrayList<GraphValues> getGraphValuesOfTheLastMonth(){
        Cursor res = db.getAllTicketDataOfTheLastMonth();
        ArrayList<GraphValues> values = new ArrayList<>();

        while(res.moveToNext()){
            GraphValues graphValues = new GraphValues();
            graphValues.setTransaction(res.getString(3));
            graphValues.setCurrency(res.getDouble(4));
            graphValues.setDateInLong(res.getLong(7));
            values.add(graphValues);

            //Log.e(TAG, " X : "+res.getLong(7)+" || Y :"+res.getDouble(4)+" || Transaction : "+res.getString(3));
        }
        Log.e(TAG, " getGraphValuesOfTheLastWeek => done! ");
        return values;
    }

    private ArrayList<GraphValues> getGraphValuesOfTheLastWeek(){
        Cursor res = db.getAllTicketDataOfTheLastWeek();
        ArrayList<GraphValues> values = new ArrayList<>();

        while(res.moveToNext()){
            GraphValues graphValues = new GraphValues();
            graphValues.setTransaction(res.getString(3));
            graphValues.setCurrency(res.getDouble(4));
            graphValues.setDateInLong(res.getLong(7));
            values.add(graphValues);

            //Log.e(TAG, " X : "+res.getLong(7)+" || Y :"+res.getDouble(4)+" || Transaction : "+res.getString(3));
        }
        Log.e(TAG, " getGraphValuesOfTheLastWeek => done! ");
        return values;
    }

    private ArrayList<GraphValues> getGraphValuesOfTheLast24H(){
        Cursor res = db.getAllTicketDataOfTheLast24H();
        ArrayList<GraphValues> values = new ArrayList<>();

        while(res.moveToNext()){
            GraphValues graphValues = new GraphValues();
            graphValues.setTransaction(res.getString(3));
            graphValues.setCurrency(res.getDouble(4));
            graphValues.setDateInLong(res.getLong(7));
            values.add(graphValues);

            //Log.e(TAG, " X : "+res.getLong(7)+" || Y :"+res.getDouble(4)+" || Transaction : "+res.getString(3));
        }
        Log.e(TAG, " getGraphValuesOfTheLast24H => done! ");
        return values;
    }


}
