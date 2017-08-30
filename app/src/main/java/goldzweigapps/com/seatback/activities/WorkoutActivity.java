package goldzweigapps.com.seatback.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

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
        @IntegerRes
        Integer drawableToLoad = null;
        if (incomingIntent.hasExtra(DRAWABLE_INTENT_KEY)) {
            drawableToLoad = incomingIntent.getIntExtra(DRAWABLE_INTENT_KEY, R.drawable.chair);
            String videoToPlay = "3c7QRP8mIt8";//https://www.youtube.com/watch?v=3c7QRP8mIt8

            switch (drawableToLoad){
                case R.drawable.extra:
                    videoToPlay = "3c7QRP8mIt8";
                    break;
                case R.drawable.extra2:
                    videoToPlay = "uiVPUW_gJm8"; // https://www.youtube.com/watch?v=uiVPUW_gJm8
                    break;
                default:
                    videoToPlay = "5NgRcaKdrAU"; // https://www.youtube.com/watch?v=5NgRcaKdrAU
                    break;
            }
            incomingIntent.removeExtra(DRAWABLE_INTENT_KEY);
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyDOIEhhqCLzW0jcZ-j047hXFKiaZLWik_A", videoToPlay, 0, true, false);
            startActivity(intent);
            finish();
        }
    }
}
