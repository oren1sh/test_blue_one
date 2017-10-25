package seatback.com.seatback.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by naji on 10/21/2017.
 */

public class BarPercentageFormatter implements IAxisValueFormatter {
    private DecimalFormat mFormat;

    public BarPercentageFormatter() {
        mFormat = new DecimalFormat("##.#"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value)+"%"; // e.g. append a percentage-sign
    }
}
