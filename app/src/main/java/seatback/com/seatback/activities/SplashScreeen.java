package seatback.com.seatback.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import seatback.com.seatback.R;

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
