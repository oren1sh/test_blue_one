package goldzweigapps.com.seatback.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.activities.WorkoutActivity;
import goldzweigapps.com.seatback.application.SeatBackApplication;
import goldzweigapps.com.seatback.utils.Utils;


public class WorkoutFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private View rootView;
    private ImageView mExerciseTopRight, mExerciseMiddleMiddle, mExerciseMiddleLeft,
                mExerciseMiddleRightMost, mExerciseTopMiddle, mExerciseTopLeft,
            mExerciseBottomLeft, mExerciseBottomMiddle, mExerciseBottomRight;
    private SeatBackApplication helper = SeatBackApplication.getInstance();
    private static String[] videoFiles = new String[] {
            "YEZlElFTp4k",
            "vUktU_pxUGY",
            "RPiGQ6MFqAM",
            "gjNB-rvc9PQ",
            "D3GdDccp6Gs",
            "cnBsBbqk0so",
            "AsQ8e45eb_c",
            "Ao7HEEmAe_I",
            "0hL5CD5H_kc"
    };

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

        Glide.with(context).load(R.drawable.restdown).into(mExerciseTopRight);
        Glide.with(context).load(R.drawable.restup).into(mExerciseTopMiddle);
        Glide.with(context).load(R.drawable.lowback).into(mExerciseTopLeft);

        Glide.with(context).load(R.drawable.twistneck).into(mExerciseMiddleRightMost);
        Glide.with(context).load(R.drawable.handsup).into(mExerciseMiddleMiddle);
        Glide.with(context).load(R.drawable.shoulderup).into(mExerciseMiddleLeft);

        Glide.with(context).load(R.drawable.carpal).into(mExerciseBottomRight);
        Glide.with(context).load(R.drawable.twistbody).into(mExerciseBottomMiddle);
        Glide.with(context).load(R.drawable.twisthand).into(mExerciseBottomLeft);

        sendRequest();
    }

    @Override
    public void onClick(View v) {
        //Could have done 4 on click listeners but this is cleaner
        if (v.equals(mExerciseTopLeft)) {
           showExercise(videoFiles[0]);
        } else if (v.equals(mExerciseTopMiddle)) {
            showExercise(videoFiles[1]);
       } else if (v.equals(mExerciseTopRight)) {
           showExercise(videoFiles[2]);
        } else if (v.equals(mExerciseMiddleLeft)) {
            showExercise(videoFiles[3]);
        } else if (v.equals(mExerciseMiddleMiddle)) {
            showExercise(videoFiles[4]);
        } else if (v.equals(mExerciseMiddleRightMost)) {
            showExercise(videoFiles[5]);
       } else if (v.equals(mExerciseBottomLeft)) {
           showExercise(videoFiles[6]);
        } else if (v.equals(mExerciseBottomMiddle)) {
            showExercise(videoFiles[7]);
       } else if (v.equals(mExerciseBottomRight)) {
           showExercise(videoFiles[8]);
       }
    }
    private void showExercise(String videoID) {
        Intent moveToExerciseIntent = new Intent(context, WorkoutActivity.class);
        moveToExerciseIntent.putExtra(WorkoutActivity.DRAWABLE_INTENT_KEY, videoID);
        startActivity(moveToExerciseIntent);
    }
    private void sendRequest(){
        JSONObject jsonObject = new JSONObject();

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Utils.getServerURL() + "/getMovies",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int index = 0; index <response.length(); index++){
                            try {
                                JSONObject obj = response.getJSONObject(index);
                                videoFiles[index] = obj.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        helper.add(jsonArrayRequest);
    }
}