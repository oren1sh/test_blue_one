package goldzweigapps.com.seatback.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.utils.ColorUtils;

public class InformationActivity extends AppCompatActivity {
    Toolbar mToolBar;
    ImageView mInformationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        mToolBar = (Toolbar) findViewById(R.id.toolbar_information);
        mInformationView = (ImageView) findViewById(R.id.img_information);
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
        Glide.with(this).load(R.drawable.chair).into(mInformationView);
    }
}
