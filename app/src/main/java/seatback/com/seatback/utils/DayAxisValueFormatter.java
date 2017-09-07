package seatback.com.seatback.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;


/**
 * Created by naji on 9/5/2017.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {
    private BarLineChartBase<?> chart;
    private ArrayList<String> axisValues;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    public void setAxisValues(ArrayList<String> values){
        axisValues = values;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if( axisValues.size() >0 && axisValues.size() > (int)value)
            return axisValues.get((int)value);
        else
            return Float.toString(value);
    }
}
