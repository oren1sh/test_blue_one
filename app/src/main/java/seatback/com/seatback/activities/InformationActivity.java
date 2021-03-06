package seatback.com.seatback.activities;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import seatback.com.seatback.BuildConfig;
import seatback.com.seatback.R;
import seatback.com.seatback.utils.ColorUtils;

public class InformationActivity extends AppCompatActivity {
    Toolbar mToolBar;
    ImageView mInformationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ViewCompat.setLayoutDirection(getWindow().getDecorView(), ViewCompat.LAYOUT_DIRECTION_LOCALE);

        mToolBar = (Toolbar) findViewById(R.id.toolbar_information);
        mInformationView = (ImageView) findViewById(R.id.exceriseVideoView);
        setSupportActionBar(mToolBar);
        mToolBar.setTitleTextColor(ColorUtils.WHITE);
        mToolBar.setTitle("Information");
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              InformationActivity.super.onBackPressed();
            }
        });
//        if(!BuildConfig.DEBUG){
//            Glide.with(this).load(R.drawable.chair).into(mInformationView);
//            findViewById(R.id.about_id).setVisibility(View.INVISIBLE);
//        }
//        else
        {
            mInformationView.setVisibility(View.INVISIBLE);
            findViewById(R.id.about_id).setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.seat_back_logo).into((ImageView) findViewById(R.id.img_logo));
            TextView version = (TextView) findViewById(R.id.version);
            String versionNumber = BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE;
            version.setText(versionNumber);
        }
    }
}
