package goldzweigapps.com.seatback.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;


import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.utils.ColorUtils;

public class WorkoutActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolBar;
    private ImageView mWorkoutView;

    public static final String DRAWABLE_INTENT_KEY = "My_Drawable_intent_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_information);
        mToolBar = (Toolbar) findViewById(R.id.toolbar_information);
//        mWorkoutView = (ImageView) findViewById(R.id.img_information);
        setSupportActionBar(mToolBar);
        mToolBar.setTitleTextColor(ColorUtils.WHITE);
        getSupportActionBar().setTitle("Workout");
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              WorkoutActivity.super.onBackPressed();
            }
        });

        handleIncomingIntent();
    }

    /**
     * getting what image has been pressed
     */
    private void handleIncomingIntent() {
        Intent incomingIntent = getIntent();
        String videoToPlay = "";
        if (incomingIntent.hasExtra(DRAWABLE_INTENT_KEY)) {
            videoToPlay = incomingIntent.getStringExtra(DRAWABLE_INTENT_KEY);
            incomingIntent.removeExtra(DRAWABLE_INTENT_KEY);
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyDOIEhhqCLzW0jcZ-j047hXFKiaZLWik_A", videoToPlay, 0, true, false);
            startActivity(intent);
            finish();
        }
    }
}
