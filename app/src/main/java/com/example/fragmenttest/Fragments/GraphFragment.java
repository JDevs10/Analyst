package com.example.fragmenttest.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragmenttest.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;

public class GraphFragment extends Fragment {

    private View v;
    private Context mContext;
    private String TAG = ListFragment.class.getSimpleName();

    private LineChart graph;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container,false);;

        graph = (LineChart) v.findViewById(R.id.lineChart);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        graph.setDragEnabled(true);
        graph.setScaleEnabled(false);

        //add chart point values
        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0, 45f));
        yValues.add(new Entry(1, 20f));
        yValues.add(new Entry(2, 90f));
        yValues.add(new Entry(3, 10f));
        yValues.add(new Entry(4, 20f));
        yValues.add(new Entry(5, 5f));


        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
        set1.setFillAlpha(110);

        //Customize Chart
        set1.setColor(Color.BLUE);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.WHITE);

        //Add the line data into an arrayList of line Chart
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        //Display the Chart
        LineData data = new LineData(dataSets);
        graph.setData(data);
    }
}
