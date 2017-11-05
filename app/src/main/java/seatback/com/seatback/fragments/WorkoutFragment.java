package seatback.com.seatback.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import seatback.com.seatback.R;
import seatback.com.seatback.activities.WorkoutActivity;
import seatback.com.seatback.application.SeatBackApplication;
import seatback.com.seatback.utils.Utils;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class WorkoutFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private View rootView;
    private int workoutNumber = -1;
    private ImageView[] workoutViews = new ImageView[9];
    private Object[] workoutObjs = new Object[9];
    private String[] workoutText = new String[9];

//    private ImageView mExerciseTopRight, mExerciseMiddleMiddle, mExerciseMiddleLeft,
//                mExerciseMiddleRightMost, mExerciseTopMiddle, mExerciseTopLeft,
//            mExerciseBottomLeft, mExerciseBottomMiddle, mExerciseBottomRight;
    private SeatBackApplication helper = SeatBackApplication.getInstance();
    private static String[] videoFiles = new String[] {
            "EXo2QP27VQE",
            "AsQ8e45eb_c",
            "cnBsBbqk0so",
            "YEZlElFTp4k",
            "2RUi85l46cU",
            "rfWTmFP0DiA",
            "D3GdDccp6Gs",
            "PcG_dtN3MXA",
            "0hL5CD5H_kc"
    };
    OnWorkoutFragmentCreated mListener;
    // Container Activity must implement this interface
    public interface OnWorkoutFragmentCreated {
        public void onWorkoutFragmentCreated();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if( workoutNumber == -1 && isVisibleToUser == true && getView() != null){
            getView().findViewById(R.id.single_workout_image_layout).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.full_workout_images).setVisibility(View.VISIBLE);
            loadImages(getView());
            workoutNumber = -1;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getView().findViewById(R.id.single_workout_image_layout).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.full_workout_images).setVisibility(View.INVISIBLE);

        ImageView imageToShow  = (ImageView) getView().findViewById(R.id.single_workout_image);
        if( imageToShow != null) {
            imageToShow.setOnClickListener(this);
            Glide.with(context).load(workoutObjs[resultCode]).into(imageToShow);
            ((TextView) getView().findViewById(R.id.single_workout_text)).setText(workoutText[resultCode]);
        }
        workoutNumber = resultCode;
    }

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
        loadImages(view);
        mListener = (OnWorkoutFragmentCreated)getActivity();
        mListener.onWorkoutFragmentCreated();
    }

    private void loadImages(View view){
        workoutViews[0] = (ImageView) view.findViewById(R.id.img_exercise_top_right);
        workoutViews[0].setOnClickListener(this);
        workoutViews[1]  = (ImageView) view.findViewById(R.id.img_exercise_top_middle);
        workoutViews[1].setOnClickListener(this);
        workoutViews[2] = (ImageView) view.findViewById(R.id.img_exercise_top_left);
        workoutViews[2].setOnClickListener(this);

        workoutViews[3] = (ImageView) view.findViewById(R.id.img_exercise_middle_right_most);
        workoutViews[3].setOnClickListener(this);
        workoutViews[4] = (ImageView) view.findViewById(R.id.img_exercise_middle_right);
        workoutViews[4].setOnClickListener(this);
        workoutViews[5] = (ImageView) view.findViewById(R.id.img_exercise_middle_left);
        workoutViews[5].setOnClickListener(this);

        workoutViews[6] = (ImageView) view.findViewById(R.id.img_exercise_bottom_right);
        workoutViews[6].setOnClickListener(this);
        workoutViews[7] = (ImageView) view.findViewById(R.id.img_exercise_bottom_middle);
        workoutViews[7].setOnClickListener(this);
        workoutViews[8] = (ImageView) view.findViewById(R.id.img_exercise_bottom_left);
        workoutViews[8].setOnClickListener(this);

        workoutObjs[0] = R.drawable.restdown;
        workoutObjs[1] = R.drawable.restup;
        workoutObjs[2] = R.drawable.lowback;

        workoutObjs[3] = R.drawable.twistneck;
        workoutObjs[4] = R.drawable.handsup;
        workoutObjs[5] = R.drawable.shoulderup;

        workoutObjs[6] = R.drawable.carpal;
        workoutObjs[7] = R.drawable.twistbody;
        workoutObjs[8] = R.drawable.twisthand;

        workoutText[0] = getString(R.string.workout_top_right);
        workoutText[1] = getString(R.string.workout_top_middle);
        workoutText[2] = getString(R.string.workout_top_left);

        workoutText[3] = getString(R.string.workout_middle_right);
        workoutText[4] = getString(R.string.workout_middle_middle);
        workoutText[5] = getString(R.string.workout_middle_left);

        workoutText[6] = getString(R.string.workout_bottom_right);
        workoutText[7] = getString(R.string.workout_bottom_middle);
        workoutText[8] = getString(R.string.workout_bottom_left);

        Glide.with(context).load(workoutObjs[0]).into(workoutViews[0]);
        Glide.with(context).load(workoutObjs[1]).into(workoutViews[1]);
        Glide.with(context).load(workoutObjs[2]).into(workoutViews[2]);

        Glide.with(context).load(workoutObjs[3]).into(workoutViews[3]);
        Glide.with(context).load(workoutObjs[4]).into(workoutViews[4]);
        Glide.with(context).load(workoutObjs[5]).into(workoutViews[5]);

        Glide.with(context).load(workoutObjs[6]).into(workoutViews[6]);
        Glide.with(context).load(workoutObjs[7]).into(workoutViews[7]);
        Glide.with(context).load(workoutObjs[8]).into(workoutViews[8]);

        sendRequest();
    }

    @Override
    public void onClick(View v) {
        //Could have done 4 on click listeners but this is cleaner
        if( workoutNumber == -1) {
            if (v.equals(workoutViews[0])) {
                showExercise(videoFiles[0]);
            } else if (v.equals(workoutViews[1])) {
                showExercise(videoFiles[1]);
            } else if (v.equals(workoutViews[2])) {
                showExercise(videoFiles[2]);
            } else if (v.equals(workoutViews[3])) {
                showExercise(videoFiles[3]);
            } else if (v.equals(workoutViews[4])) {
                showExercise(videoFiles[4]);
            } else if (v.equals(workoutViews[5])) {
                showExercise(videoFiles[5]);
            } else if (v.equals(workoutViews[6])) {
                showExercise(videoFiles[6]);
            } else if (v.equals(workoutViews[7])) {
                showExercise(videoFiles[7]);
            } else if (v.equals(workoutViews[8])) {
                showExercise(videoFiles[8]);
            }
        }
        else {
            showExercise(videoFiles[workoutNumber]);
        }
    }
    private void showExercise(String videoID) {
        Intent moveToExerciseIntent = new Intent(context, WorkoutActivity.class);
        moveToExerciseIntent.putExtra(WorkoutActivity.DRAWABLE_INTENT_KEY, videoID);
        Answers.getInstance().logCustom(new CustomEvent("Workout").putCustomAttribute("VideoID", videoID));
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key",Utils.getAPITokenId());
                //..add other headers
                return params;
            }
        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        helper.add(jsonArrayRequest);
    }
}