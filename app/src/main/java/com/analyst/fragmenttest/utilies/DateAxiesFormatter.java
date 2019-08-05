package com.analyst.fragmenttest.utilies;

import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateAxiesFormatter extends ValueFormatter implements IAxisValueFormatter {

    private Chart chart;

    public DateAxiesFormatter(Chart chart) {

        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String result = "";
        Date date = new Date((long) value);
        Log.e("Date ", " date => "+date+" at "+value);
        SimpleDateFormat prettyFormat = new SimpleDateFormat("dd.MM H:mm");
        Log.e("Date Format ", " Format => "+prettyFormat+" at "+date);
        prettyFormat.setTimeZone(TimeZone.getDefault());
        Log.e("Date Format TZ ", " TZ => "+prettyFormat+" at "+date);
        result = prettyFormat.format(date);
        Log.e("Date Result ", " Results => "+result+" at "+prettyFormat);
        return result;

        //return sdf.format(new Date((long) value));
    }
}
