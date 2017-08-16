package goldzweigapps.com.seatback.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.activities.WorkoutActivity;


public class WorkoutFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private View rootView;
    private ImageView mExerciseTopRight, mExerciseTopLeft, mExerciseBottomLeft, mExerciseBottomRight;
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
        mExerciseTopRight = (ImageView) view.findViewById(R.id.img_exercise_top_right);
        mExerciseTopRight.setOnClickListener(this);
        mExerciseTopLeft  = (ImageView) view.findViewById(R.id.img_exercise_top_left);
        mExerciseTopLeft.setOnClickListener(this);
        mExerciseBottomRight = (ImageView) view.findViewById(R.id.img_exercise_bottom_right);
        mExerciseBottomRight.setOnClickListener(this);
        mExerciseBottomLeft = (ImageView) view.findViewById(R.id.img_exercise_bottom_left);
        mExerciseBottomLeft.setOnClickListener(this);

        Glide.with(context).load(R.drawable.back_stretch).into(mExerciseTopRight);
        Glide.with(context).load(R.drawable.wrist_stretch).into(mExerciseTopLeft);
        Glide.with(context).load(R.drawable.shoulders_stretch).into(mExerciseBottomRight);
        Glide.with(context).load(R.drawable.neck_stretch).into(mExerciseBottomLeft);

    }

    @Override
    public void onClick(View v) {
        //Could have done 4 on click listeners but this is cleaner
       if (v.equals(mExerciseTopRight)) {
           showExercise(R.drawable.extra);
       } else if (v.equals(mExerciseTopLeft)) {
           showExercise(R.drawable.extra2);
       } else if (v.equals(mExerciseBottomRight)) {
           showExercise(R.drawable.extra3);
       } else if (v.equals(mExerciseBottomLeft)) {
           showExercise(R.drawable.extra4);
       }
    }
    private void showExercise(@DrawableRes Integer drawableResID) {
        Intent moveToExerciseIntent = new Intent(context, WorkoutActivity.class);
        moveToExerciseIntent.putExtra(WorkoutActivity.DRAWABLE_INTENT_KEY, drawableResID);
        startActivity(moveToExerciseIntent);
    }
}