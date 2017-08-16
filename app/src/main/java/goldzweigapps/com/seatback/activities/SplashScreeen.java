package goldzweigapps.com.seatback.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.utils.ColorUtils;

public class SplashScreeen extends AppCompatActivity {
       private static final String TAG = SplashScreeen.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screeen);
        Glide.with(this).load(R.drawable.splash_background).into((ImageView) findViewById(R.id.img_logo));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent moveToMain = new Intent(SplashScreeen.this, MainActivity.class);
                startActivity(moveToMain);
            }
        }, 800);
        }
    }
