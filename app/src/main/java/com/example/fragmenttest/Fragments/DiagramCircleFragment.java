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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.DiagramValues;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DiagramCircleFragment extends Fragment {
    private String TAG = DiagramCircleFragment.class.getSimpleName();

    private View v;
    private Context mContext;

    private Spinner pieChartFilter_sp;
    private String filterSelected = "";
    private PieChart pieChart;
    private ArrayList<DiagramValues> diagramValues;

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

        pieChartFilter_sp = (Spinner) v.findViewById(R.id.fragmentDiagram_pieChart_filter);
        pieChart = (PieChart) v.findViewById(R.id.fragmentDiagram_pieChart);

        //"Global Debit Pie Chart by Categories"
        pieChart.getDescription().setText("Global Pie Chart of all Categories");
        pieChart.getDescription().setTextSize(14f);

        pieChart.setNoDataText("No Data Yet");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Global Chart");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(16);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setUsePercentValues(true);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.animateY(1000, Easing.EaseInOutCubic);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //graph time filter
        String[] timeFilter = {"Credit Chart", "Debit Chart"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, timeFilter);
        pieChartFilter_sp.setAdapter(adapter);
        pieChartFilter_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterSelected = adapterView.getSelectedItem().toString();

                getDataFromDb();
                addDatatToChart();

                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        //Log.e(TAG, " onValueSelected: Value selected from chart");
                        //Log.e(TAG, " onValueSelected: X value => "+h.getX()+" Y value => "+h.getY());
                        if (filterSelected.contains("Credit")){
                            Toast.makeText(mContext, diagramValues.get((int) h.getX()).getCategoryName()+
                                    " category has "+h.getY()+"% of credit from the total gain", Toast.LENGTH_LONG).show();
                        }
                        if (filterSelected.contains("Debit")){
                            Toast.makeText(mContext, diagramValues.get((int) h.getX()).getCategoryName()+
                                    " category has "+h.getY()+"% of spending from the total withdrawal", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getDataFromDb(){
        Log.e(TAG, " getDataFromDb");

        //1 get all the categories in the table
        Cursor res1 = db.getAllCategoriesData();
        ArrayList<String> categoryNames = new ArrayList<>();
        while (res1.moveToNext()){
            categoryNames.add(res1.getString(1));
            Log.e(TAG, " categoryNames : "+res1.getString(1));
        }


        //2 get all tickets of each category and calculate the sum of all values in the category
        ArrayList<Double> totalValueOfEachCategory = new ArrayList<>();

        //in each category (found in the db table) get all the ticket currency and calculate the total value
        for (int i=0; i<categoryNames.size(); i++){
            double categoryTotalValue = 0.0;

            Cursor res2 = db.getAllTicketData();
            while (res2.moveToNext()){
                //Log.e(TAG, " condition if : "+categoryNames.get(i)+" == "+res2.getString(2));
                if (categoryNames.get(i).equals(res2.getString(2))){

                    //if the ticket type is credit or debit type
                    if (res2.getString(3).equals("+") && filterSelected.contains("Credit")){
                        pieChart.setCenterText("Credit Chart");
                        categoryTotalValue = categoryTotalValue + res2.getDouble(4);
                    }
                    //if the ticket type is credit or debit type
                    if (res2.getString(3).equals("-") && filterSelected.contains("Debit")){
                        pieChart.setCenterText("Debit Chart");
                        categoryTotalValue = categoryTotalValue - res2.getDouble(4);
                    }
                    //Log.e(TAG, " categoryTotalValue: "+categoryTotalValue);
                }
            }
            Log.e(TAG, " categoryTotalValue done: "+categoryTotalValue);
            totalValueOfEachCategory.add(categoryTotalValue);
        }

        //3 calculate the global value of all category
        double globalValueOfAllCategories = 0.0;
        for (int i=0; i<totalValueOfEachCategory.size(); i++){
            globalValueOfAllCategories += totalValueOfEachCategory.get(i);
        }
        Log.e(TAG, " globalValueOfAllCategories : "+globalValueOfAllCategories);

        //4 calculate the percentage of the category from the global value
        ArrayList<Float> categoryValues = new ArrayList<>();
        for (int i=0; i<totalValueOfEachCategory.size(); i++){
           categoryValues.add( (float) ((totalValueOfEachCategory.get(i) / globalValueOfAllCategories) * 100) );
           Log.e(TAG, " percentage : "+ (float) ((totalValueOfEachCategory.get(i) / globalValueOfAllCategories) * 100));
        }

        //populate the diagram values
        diagramValues = new ArrayList<>();
        for (int i=0; i<categoryNames.size(); i++){
            if (categoryValues.get(i) != 0.0 || categoryValues.get(i) != -0.0){
                diagramValues.add(new DiagramValues(categoryNames.get(i), categoryValues.get(i)));
            }else {
                categoryNames.remove(i);
                categoryValues.remove(i);
            }
        }

    }

    private void addDatatToChart(){
        Log.e(TAG, " addDatatToChart !");
        ArrayList<PieEntry> yEntry = new ArrayList<>();

        for (int i =0; i<diagramValues.size(); i++){
            yEntry.add(new PieEntry(diagramValues.get(i).getTotalCategoryValue(), diagramValues.get(i).getCategoryName()));
        }

        //create data list
        PieDataSet pieDataSet = new PieDataSet(yEntry, "Categories");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(18f);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.BLACK);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
