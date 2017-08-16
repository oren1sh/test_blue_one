package goldzweigapps.com.seatback.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.views.CircleTimerView;


public class HomeFragment extends Fragment {
    private Context context;
    private View rootView;
    public CircleTimerView circleTimerView;
    public ImageView positionImageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = rootView.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circleTimerView = (CircleTimerView) view.findViewById(R.id.circle_timer_view);
        positionImageView = (ImageView) view.findViewById(R.id.img_sitting_position);
    }

}