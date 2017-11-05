package seatback.com.seatback.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import seatback.com.seatback.R;
import seatback.com.seatback.utils.DayAxisValueFormatter;
import seatback.com.seatback.views.CircleTimerView;
import seatback.com.seatback.utils.PercentageFormatter;


public class HomeFragment extends Fragment {
    private Context context;
    private View rootView;
    public CircleTimerView circleTimerView;
    public ImageView positionImageView;
    private TextView postureDescription;
    private PieChart pieChart;
    private float lastShownValue = 0f;
    private long lastConnectedTimeValue = 0;
    private String[] imageText = new String[6];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = rootView.getContext();
        pieChart = (PieChart) rootView.findViewById(R.id.homeChart);
        pieChart.getDescription().setEnabled(false);

        pieChart.setHoleRadius(50f);
        pieChart.setUsePercentValues(true);
        pieChart.setNoDataText(getString(R.string.no_data_to_show));
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.getLegend().setEnabled(false);
//        pieChart.setEntryLabelTextSize(10);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        imageText[0] = getString(R.string.standing);
        imageText[1] = getString(R.string.good_posture);
        imageText[2] = getString(R.string.bending);
        imageText[3] = getString(R.string.slouching);
        imageText[4] = getString(R.string.tilt_left);
        imageText[5] = getString(R.string.tilt_right);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        circleTimerView = (CircleTimerView) view.findViewById(R.id.circle_timer_view);
        positionImageView = (ImageView) view.findViewById(R.id.img_sitting_position);
        postureDescription = (TextView) view.findViewById(R.id.text_view_posture_description);
        drawPieChart(lastShownValue, lastConnectedTimeValue);
    }

    public void drawPieChart(float percentage, long connectedTime){
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // this allows us to show the last piechart drawing even in the case the device disconnected/app restarted without
        // a connection. In the case that a day had passed since the last time the device connected, then the connection
        // time will be zero again and we should remove the last piechart drawing.
        if( (connectedTime > 0 && percentage != 0) || (connectedTime == 0 && percentage == 0f))
            lastShownValue = percentage;
        lastConnectedTimeValue = connectedTime;
        entries.add(new PieEntry(1-lastShownValue, getString(R.string.good_posture)));
        colors.add(Color.GREEN);
        entries.add(new PieEntry(lastShownValue, getString(R.string.bad_posture)));
        colors.add(Color.RED);
        Date currentTime = new Date(connectedTime);
        PeriodFormatter durationFormatter;
        Period period;
        period = new Period(connectedTime).toPeriod();
        durationFormatter = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();
        String periodFormatted = durationFormatter.print(period.toPeriod());
        if( connectedTime > 0 && percentage != 0)
            pieChart.setCenterText(getResources().getString(R.string.circle_Sitting_time)+"\n" + periodFormatted);
        else
            pieChart.setCenterText(getResources().getString(R.string.not_connected)+"\n");

        if( entries.size()>0) {
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);
            IValueFormatter valueFormatter = new PercentageFormatter();
            dataSet.setValueFormatter(valueFormatter);

            PieData data = new PieData(dataSet);
            dataSet.setColors(colors);
            data.setValueTextSize(12f);

            pieChart.setData(data);
        }
        pieChart.invalidate();
    }
    private float calculatePercentage(int cnt, int total){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float percentage = ((float)cnt/(float)total)*100f;
        return Float.valueOf(decimalFormat.format(percentage));
    }
    public void setImageText(int postureIndex){
        postureDescription.setText(imageText[postureIndex]);
    }
}