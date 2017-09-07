package seatback.com.seatback.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by gilgoldzweig on 24/03/2017.
 */

public class TimeService extends Service {
    private static final String TAG = TimeService.class.getSimpleName();
    public static final String COUNTDOWN_PACKAGE = "goldzweigapps.com.seatback.services.timer";
    private long _12HOURS = 43200000; //in milliseconds
    private static long time;
    private SharedPreferences lastTime; // saves current run time before app been killed
    Intent timerIntent = new Intent(COUNTDOWN_PACKAGE);
    TimeBinder binder = new TimeBinder();

    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();

        lastTime = getSharedPreferences("timer", MODE_PRIVATE);
        //count down timer for 12 hours with tick every second
        countDownTimer = new CountDownTimer(_12HOURS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = _12HOURS - millisUntilFinished + lastTime.getLong("countDown", 0);
                timerIntent.putExtra("countdown", time);
                if (time == _12HOURS) {
                    countDownTimer.onFinish();
                }
                sendBroadcast(timerIntent);
            }

            @Override
            public void onFinish() {
                lastTime.edit().putLong("countDown", time).apply();
            }
        };

        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + time);
        lastTime.edit().putLong("countDown", time).apply();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: " + time);
        lastTime.edit().putLong("countDown", time).apply();
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    public class TimeBinder extends Binder {
        public TimeService getService() {
            return TimeService.this;
        }
    }
}