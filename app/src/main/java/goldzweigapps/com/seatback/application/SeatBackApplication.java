package goldzweigapps.com.seatback.application;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SeatBackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //setup crashlytics for remote debugging
        Fabric.with(this, new Crashlytics());

    }
}
