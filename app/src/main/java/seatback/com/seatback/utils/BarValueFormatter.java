package seatback.com.seatback.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by naji on 10/21/2017.
 */

public class BarValueFormatter implements IValueFormatter {
    private DecimalFormat mFormat;

    public BarValueFormatter() {
        mFormat = new DecimalFormat("##.#"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value)+"%"; // e.g. append a percentage-sign
    }
}
