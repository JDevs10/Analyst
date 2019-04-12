package com.example.fragmenttest.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphFragment extends Fragment {

    private View v;
    private Context mContext;
    private String TAG = GraphFragment.class.getSimpleName();

    private GraphView graph;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private DatabaseHelper db;

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

        graph = (GraphView) v.findViewById(R.id.graph);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get graph values from tickets in the database
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getGraphValues1(getGraphValues()));

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
        graph.getViewport().setMinX(getGraphValues().get(0).getDateInLong());
        graph.getViewport().setMaxX(getGraphValues().get(getGraphValues().size()-1).getDateInLong());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    private DataPoint[] getGraphValues1(ArrayList<GraphValues> getGraphValues){
        DataPoint[] dp = new DataPoint[getGraphValues.size()];

        for (int i=0; i<getGraphValues.size(); i++){
            dp[i] = new DataPoint(getGraphValues.get(i).getDateInLong(), getGraphValues.get(i).getCurrency());
        }
        return dp;
    }

    private ArrayList<GraphValues> getGraphValues(){
        Cursor res = db.getAllTicketData();
        ArrayList<GraphValues> values = new ArrayList<>();

        while(res.moveToNext()){
            GraphValues graphValues = new GraphValues();
            graphValues.setCurrency(res.getDouble(4));
            graphValues.setDateInLong(res.getLong(7));
            values.add(graphValues);

            Log.e(TAG, " X : "+res.getString(6)+" || Y :"+res.getDouble(4));
        }
        return values;
    }
}
