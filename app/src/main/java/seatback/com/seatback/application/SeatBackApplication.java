package seatback.com.seatback.application;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

// https://code.tutsplus.com/tutorials/creating-a-weather-application-for-mars-using-volley--cms-23812
public class SeatBackApplication extends Application {

    private RequestQueue mRequestQueue;
    private static SeatBackApplication mInstance;
    public static final String TAG = SeatBackApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        //setup crashlytics for remote debugging
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized SeatBackApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }}
