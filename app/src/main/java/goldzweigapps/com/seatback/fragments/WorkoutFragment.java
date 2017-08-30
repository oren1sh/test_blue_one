package goldzweigapps.com.seatback.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.activities.MainActivity;
import goldzweigapps.com.seatback.activities.WorkoutActivity;


public class WorkoutFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private View rootView;
    private ImageView mExerciseTopRight, mExerciseMiddleMiddle, mExerciseMiddleLeft,
                mExerciseMiddleRightMost, mExerciseTopMiddle, mExerciseTopLeft,
            mExerciseBottomLeft, mExerciseBottomMiddle, mExerciseBottomRight;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        context = rootView.getContext();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExerciseTopLeft  = (ImageView) view.findViewById(R.id.img_exercise_top_left);
        mExerciseTopLeft.setOnClickListener(this);
        mExerciseTopMiddle = (ImageView) view.findViewById(R.id.img_exercise_top_middle);
        mExerciseTopMiddle.setOnClickListener(this);
        mExerciseTopRight = (ImageView) view.findViewById(R.id.img_exercise_top_right);
        mExerciseTopRight.setOnClickListener(this);

        mExerciseMiddleMiddle = (ImageView) view.findViewById(R.id.img_exercise_middle_right);
        mExerciseMiddleMiddle.setOnClickListener(this);
        mExerciseMiddleRightMost = (ImageView) view.findViewById(R.id.img_exercise_middle_right_most);
        mExerciseMiddleRightMost.setOnClickListener(this);
        mExerciseMiddleLeft = (ImageView) view.findViewById(R.id.img_exercise_middle_left);
        mExerciseMiddleLeft.setOnClickListener(this);

        mExerciseBottomLeft = (ImageView) view.findViewById(R.id.img_exercise_bottom_left);
        mExerciseBottomLeft.setOnClickListener(this);
        mExerciseBottomMiddle = (ImageView) view.findViewById(R.id.img_exercise_bottom_middle);
        mExerciseBottomMiddle.setOnClickListener(this);
        mExerciseBottomRight = (ImageView) view.findViewById(R.id.img_exercise_bottom_right);
        mExerciseBottomRight.setOnClickListener(this);

        Glide.with(context).load(R.drawable.twistbody).into(mExerciseTopMiddle);
        Glide.with(context).load(R.drawable.carpal).into(mExerciseTopRight);
        Glide.with(context).load(R.drawable.twisthand).into(mExerciseTopLeft);

        Glide.with(context).load(R.drawable.lowback).into(mExerciseMiddleMiddle);
        Glide.with(context).load(R.drawable.restup).into(mExerciseMiddleRightMost);
        Glide.with(context).load(R.drawable.twisthand).into(mExerciseMiddleLeft);

        Glide.with(context).load(R.drawable.twistneck).into(mExerciseBottomLeft);
        Glide.with(context).load(R.drawable.shoulderup).into(mExerciseBottomMiddle);
        Glide.with(context).load(R.drawable.restdown).into(mExerciseBottomRight);

    }

    @Override
    public void onClick(View v) {
        //Could have done 4 on click listeners but this is cleaner
        if (v.equals(mExerciseTopLeft)) {
           showExercise(R.drawable.extra);
        } else if (v.equals(mExerciseTopMiddle)) {
            showExercise(R.drawable.extra2);
       } else if (v.equals(mExerciseTopRight)) {
           showExercise(R.drawable.extra2);
        } else if (v.equals(mExerciseMiddleLeft)) {
            showExercise(R.drawable.extra2);
        } else if (v.equals(mExerciseMiddleMiddle)) {
            showExercise(R.drawable.extra2);
        } else if (v.equals(mExerciseTopRight)) {
            showExercise(R.drawable.extra2);
       } else if (v.equals(mExerciseBottomLeft)) {
           showExercise(R.drawable.extra3);
        } else if (v.equals(mExerciseBottomMiddle)) {
            showExercise(R.drawable.extra3);
       } else if (v.equals(mExerciseBottomRight)) {
           showExercise(R.drawable.extra4);
       }
    }
    private void showExercise(@DrawableRes Integer drawableResID) {
        Intent moveToExerciseIntent = new Intent(context, WorkoutActivity.class);
        moveToExerciseIntent.putExtra(WorkoutActivity.DRAWABLE_INTENT_KEY, drawableResID);
        startActivity(moveToExerciseIntent);
    }
}