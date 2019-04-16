package com.example.fragmenttest.Fragments;

import android.content.Context;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class DiagramCircleFragment extends Fragment {
    private String TAG = DiagramCircleFragment.class.getSimpleName();

    private View v;
    private Context mContext;

    private PieChart pieChart;
    private float[] yData = {1.6f, 10.4f, 60.0f, 10.26f, 26.10f, 75.0f, 99.9f};
    private String[] xData = {"test1", "test2", "test3", "test4", "test5", "test6", "test7",};

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
        Log.e("Testing :", this.getClass().getSimpleName()+" is enable !!!!!!!!!");
        v = inflater.inflate(R.layout.fragment_diagram_circle, container,false);

        pieChart = (PieChart) v.findViewById(R.id.fragmentDiagram_pieChart);

        //"Global Debit Pie Chart by Categories"
        pieChart.getDescription().setText("Global Pie Chart by Categories");
        pieChart.getDescription().setTextSize(16f);

        pieChart.setNoDataText("No Data Yet");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Debit Chart");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(16);
        pieChart.setHoleColor(Color.TRANSPARENT);

        //pieChart.setUsePercentValues(true);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addDatatToChart();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.e(TAG, " onValueSelected: Value selected from chart");
                Log.e(TAG, " onValueSelected: Y value => "+e.toString());
                Log.e(TAG, " onValueSelected: X value => "+h.toString());            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void addDatatToChart(){
        Log.e(TAG, " addDatatToChart !");
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for (int i =0; i<yData.length; i++){
            yEntry.add(new PieEntry(yData[i], i));
        }

        for (int i =0; i<xData.length; i++){
            xEntry.add(xData[i]);
        }

        //create data list
        PieDataSet pieDataSet = new PieDataSet(yEntry, "Categories");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);

        //add colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(18f);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
