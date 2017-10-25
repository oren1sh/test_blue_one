package seatback.com.seatback.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import seatback.com.seatback.R;
import seatback.com.seatback.views.PressureMap;


public class TipsFragment extends Fragment {
    private Context context;
    private View rootView;
    public PressureMap topPressureMap, bottomPressureMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tips, container, false);
        context = rootView.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * no goto in java than just open it
         * goto MainActivity.class
         * main activity handle all of the bluetooth communications
         * topPressureMap is the top 6*6 circles view
         * bottomPressureMap is the bottom 6*6 circles view
         */
        topPressureMap = (PressureMap) view.findViewById(R.id.pressure_map_top);
        bottomPressureMap = (PressureMap) view.findViewById(R.id.pressure_map_bottom);
        topPressureMap.setColors(null);
        bottomPressureMap.setColors(null);
        Answers.getInstance().logCustom(new CustomEvent("Tips"));
    }

}