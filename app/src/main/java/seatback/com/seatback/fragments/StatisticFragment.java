package seatback.com.seatback.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.util.Util;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import seatback.com.seatback.R;
import seatback.com.seatback.activities.MainActivity;
import seatback.com.seatback.application.SeatBackApplication;
import seatback.com.seatback.utils.BarPercentageFormatter;
import seatback.com.seatback.utils.BarValueFormatter;
import seatback.com.seatback.utils.DayAxisValueFormatter;
import seatback.com.seatback.utils.PercentageFormatter;
import seatback.com.seatback.utils.Utils;


public class StatisticFragment extends Fragment implements OnChartGestureListener {
    private Context context;
    private View rootView;
    private Button dayButton;
    private Button weekButton;
    private Button monthButton;
    private Button allButton;
    private ImageView statisticImage;
    private SeatBackApplication helper = SeatBackApplication.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();
    private PieChart pieChart;
    private BarChart barChar;

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    private enum REPORT_PRIOD { DAY, WEEK, MONTH, ALL} ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_statistic, container, false);
        context = rootView.getContext();

        // get a layout defined in xml
        pieChart = (PieChart) rootView.findViewById(R.id.chart1);
        pieChart.getDescription().setEnabled(false);
//        rl.addView(pieChart); // add the programmatically created pieChart

        pieChart.setHoleRadius(45f);
        pieChart.setUsePercentValues(true);
        pieChart.setNoDataText(getString(R.string.no_data_to_show));
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(50f);
//        pieChart.setEntryLabelTextSize(10);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date()); // compute start of the day for the timestamp
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long seconds = cal.getTimeInMillis()/1000;

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long to_seconds = cal.getTimeInMillis()/1000;

        sendRequest(Long.toString(seconds),Long.toString(to_seconds), REPORT_PRIOD.DAY);

        barChar = (BarChart) rootView.findViewById(R.id.chart2);
        barChar.getDescription().setEnabled(false);

        barChar.setOnChartGestureListener(this);
        barChar.setNoDataText(getString(R.string.no_data_to_show));

        XAxis xAxis = barChar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        DayAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChar);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis yAxis = barChar.getAxisLeft();
        BarPercentageFormatter yAxisFormatter = new BarPercentageFormatter();
        yAxis.setValueFormatter(yAxisFormatter);
        yAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        barChar.setDrawGridBackground(false);
        barChar.setDrawBarShadow(false);

        barChar.getAxisRight().setEnabled(false);
        barChar.getAxisLeft().setDrawGridLines(false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayButton = (Button) view.findViewById(R.id.button_day);
        weekButton = (Button) view.findViewById(R.id.button_week);
        monthButton = (Button) view.findViewById(R.id.button_month);
        allButton = (Button) view.findViewById(R.id.button_all);
        // programmatically create a LineChart
        Utils.singleSelectionButtons(dayButton, dayButton, weekButton, monthButton, allButton);

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(dayButton, dayButton, weekButton, monthButton, allButton);
                Answers.getInstance().logCustom(new CustomEvent("Statistics")
                        .putCustomAttribute("Report Type", "Daily"));


                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTime(new Date()); // compute start of the day for the timestamp
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long seconds = cal.getTimeInMillis()/1000;

                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                long to_seconds = cal.getTimeInMillis()/1000;

                sendRequest(Long.toString(seconds),Long.toString(to_seconds), REPORT_PRIOD.DAY);
            }
        });
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(weekButton, dayButton, weekButton, monthButton, allButton);
                Answers.getInstance().logCustom(new CustomEvent("Statistics")
                        .putCustomAttribute("Report Type", "Weekly"));

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTime(new Date()); // compute start of the day for the timestamp
                cal.add(Calendar.DAY_OF_YEAR, -7);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long seconds = cal.getTimeInMillis()/1000;

                cal.add(Calendar.DAY_OF_YEAR, 7);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                long to_seconds = cal.getTimeInMillis()/1000;

                sendRequest(Long.toString(seconds),Long.toString(to_seconds), REPORT_PRIOD.WEEK);
            }
        });
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(monthButton, dayButton, weekButton, monthButton, allButton);
                Answers.getInstance().logCustom(new CustomEvent("Statistics")
                        .putCustomAttribute("Report Type", "Monthly"));
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTime(new Date()); // compute start of the day for the timestamp
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long seconds = cal.getTimeInMillis()/1000;

                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                long to_seconds = cal.getTimeInMillis()/1000;

                sendRequest(Long.toString(seconds),Long.toString(to_seconds), REPORT_PRIOD.MONTH);
            }
        });
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(allButton, dayButton, weekButton, monthButton, allButton);
                Answers.getInstance().logCustom(new CustomEvent("Statistics")
                        .putCustomAttribute("Report Type", "All"));
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTime(new Date()); // compute start of the day for the timestamp
                cal.add(Calendar.MONTH, -3);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long seconds = cal.getTimeInMillis()/1000;

                cal.add(Calendar.MONTH, 3);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                long to_seconds = cal.getTimeInMillis()/1000;

                sendRequest(Long.toString(seconds),Long.toString(to_seconds), REPORT_PRIOD.ALL);
            }
        });
    }

    private void sendRequest(String from_date, String to_date, final REPORT_PRIOD period){
        if( ((MainActivity)getContext()).isLoggedIn == false) return;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("seatback_id", Utils.getConnectecMAC());
            jsonObject.put("user_id", Utils.getUserID(this.context));
            jsonObject.put("from_time", from_date);
            jsonObject.put("to_time", to_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue.

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Utils.getServerURL() + "/getData", jsonObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if( period == REPORT_PRIOD.DAY)
                            drawPieChart(response);
                        else
                            drawStackedBarChar(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key",Utils.getAPITokenId());
                //..add other headers
                return params;
            }
        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        helper.add(jsonArrayRequest);
    }

    private void drawPieChart(JSONArray response){
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        barChar.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.VISIBLE);

        for(int i= 0; i< response.length(); i++){
            try {
                JSONObject obj = response.getJSONObject(i);
//                Log.d(TAG, "Get data:" + obj.getString("cnt"));
                int total = 100;
                int cnt = 0;
                int year = 0, month =0, day = 0;

                try {
                    total = obj.getInt("total");
                    year = obj.getInt("year");
                    month = obj.getInt("month");
                    day = obj.getInt("day");
                    float percentage = 0;
                    try {
                        cnt = obj.getInt("standing");
                        if (cnt != 0) {
                            percentage = calculatePercentage(cnt, total);
                            entries.add(new PieEntry(percentage, getString(R.string.standing)));
                            colors.add(Color.GREEN);
                        }
                    } catch (JSONException e){
                    }

                    try {
                    cnt = obj.getInt("good");
                    if( cnt != 0 && total > 0){
                        percentage = calculatePercentage(cnt, total);
                        entries.add(new PieEntry(percentage, getString(R.string.good_posture)));
                        colors.add(Color.GREEN);
                    }
                    } catch (JSONException e){
                    }
                    try{
                    cnt = obj.getInt("bending_forward");
                    if( cnt != 0 && total > 0){
                        percentage = calculatePercentage(cnt, total);
                        entries.add(new PieEntry(percentage, getString(R.string.bending)));
                        colors.add(Color.YELLOW);
                    }
                    } catch (JSONException e){
                    }
                    try{
                    cnt = obj.getInt("slump");
                    if( cnt != 0 && total > 0){
                        percentage = calculatePercentage(cnt, total);
                        entries.add(new PieEntry(percentage, getString(R.string.slouching)));
                        colors.add(Color.GRAY);
                    }
                    } catch (JSONException e){
                    }
                    try{
                    cnt = obj.getInt("tilt_left");
                    if( cnt != 0 && total > 0){
                        percentage = calculatePercentage(cnt, total);
                        entries.add(new PieEntry(percentage, getString(R.string.tilt_left)));
                        colors.add(Color.CYAN);
                    }
                    } catch (JSONException e){
                    }
                    try{
                    cnt = obj.getInt("tilt_right");
                    if( cnt != 0 && total > 0){
                        percentage = calculatePercentage(cnt, total);
                        entries.add(new PieEntry(percentage, getString(R.string.tilt_right)));
                        colors.add(Color.CYAN);
                    }
                    } catch (JSONException e){
                    }
                }
                catch (JSONException e){
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if( entries.size()>0) {
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);
            IValueFormatter valueFormatter = new PercentageFormatter();
            dataSet.setValueFormatter(valueFormatter);

            PieData data = new PieData(dataSet);
            data.setValueTextSize(12f);
            dataSet.setColors(colors);

            pieChart.setData(data);
        }
        pieChart.invalidate();
    }
    private float calculatePercentage(int cnt, int total){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float percentage = ((float)cnt/(float)total)*100f;
        return Float.valueOf(decimalFormat.format(percentage));
    }

    private void drawStackedBarChar(JSONArray response){
        class BarDataPoint {
            public BarDataPoint(float val, String pos, int col){
                value = val;
                posture = pos;
                color = col;

            };
            public float value;
            public String posture;
            public int color;
        }
//        List<PieEntry> entries = new ArrayList<>();
        int[] colors = {
                Color.GREEN,
                Color.GREEN,
                Color.YELLOW,
                Color.CYAN,
                Color.GRAY,
                Color.BLUE
        };
        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        String[] labels = {
                getString(R.string.good_posture),
                getString(R.string.standing),
                getString(R.string.bending),
                getString(R.string.leg),
                getString(R.string.slouching),
                getString(R.string.sensors_unknown)
        };
        BarDataPoint[] values = new BarDataPoint[8];

        pieChart.setVisibility(View.INVISIBLE);
        barChar.setVisibility(View.VISIBLE);
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        ArrayList<String> axisValues = new ArrayList<String>();

        for(int i= 0; i< response.length(); i++){
            try {
                java.util.Arrays.fill(values, new BarDataPoint(0f, "", 0));
                JSONObject obj = response.getJSONObject(i);
//                Log.d(TAG, "Get data:" + obj.getString("cnt"));
                int total = 100;
                int cnt = 0;
                int year = 0, month =0, day = 0;

                try {
                    total = obj.getInt("total");
                    year = obj.getInt("year");
                    month = obj.getInt("month");
                    day = obj.getInt("day");
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                    String strDate = String.format("%d-%d-%d", year, month, day);

                    try {
                        axisValues.add(new SimpleDateFormat("dd/MM").format(ft.parse(strDate)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    float percentage = 0;
                    try{
                    cnt = obj.getInt("good");
                    if( cnt != 0){
                        percentage = calculatePercentage(cnt, total);
                        values[0] = new BarDataPoint(percentage, getString(R.string.good_posture), Color.GREEN);
                        entries.add(new BarEntry(i, percentage));
                    }
                    } catch (JSONException e){
                    }
                }
                catch (JSONException e){
                    continue;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if( entries.size() > 0) {
            BarDataSet ds = new BarDataSet(entries, "");
            ds.setColor(Color.GREEN);
            ds.setStackLabels(labels);
            sets.add(ds);

            XAxis xAxis = barChar.getXAxis();
            DayAxisValueFormatter xAxisFormatter = (DayAxisValueFormatter)xAxis.getValueFormatter();
            xAxisFormatter.setAxisValues(axisValues);

            BarData d = new BarData(sets);
            BarValueFormatter valueFormatter = new BarValueFormatter();
            d.setValueFormatter(valueFormatter);
            d.setValueTextSize(12f);
            barChar.setData(d);
        }

        barChar.invalidate();
    }
    private String[] mLabels = new String[] { "Company A", "Company B", "Company C", "Company D", "Company E", "Company F" };
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

    private String getLabel(int i) {
        return mLabels[i];
    }

    protected BarData generateBarData(int dataSets, float range, int count) {

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for(int i = 0; i < dataSets; i++) {

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

            for(int j = 0; j < count; j++) {
                entries.add(new BarEntry(j, new float[]{(float) (Math.random() * range) + range / 4,Float.MIN_VALUE, (float) (Math.random() * range) + range / 4, (float) (Math.random() * range) + range / 4}, "data1"));
            }

            MyBarDataSet ds = new MyBarDataSet(entries, "Postures");
            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(Color.GREEN);
            colors.add(Color.RED);
            ds.setColors(colors);
            ds.setStackLabels(new String[]{"Standing", "Bending"});
            sets.add(ds);
        }

        BarData d = new BarData(sets);

        return d;
    }
    public class MyBarDataSet extends BarDataSet {


        public MyBarDataSet(List<BarEntry> yVals, String label) {
            super(yVals, label);
        }

        @Override
        public int getColor(int index) {
//            if(getEntryForIndex(index).getData()5) // less than 95 green
//                return mColors.get(0);
//            else if(getEntryForIndex(index).getVal() < 100) // less than 100 orange
//                return mColors.get(1);
//            else // greater or equal than 100 red
//                return mColors.get(2);
//            BarEntry data = getEntryForIndex(index);
            return mColors.get(0);
        }
    }
}

