package goldzweigapps.com.seatback.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.utils.Utils;


public class StatisticFragment extends Fragment {
    private Context context;
    private View rootView;
    private Button dayButton;
    private Button weekButton;
    private Button monthButton;
    private Button allButton;
    private ImageView statisticImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_statistic, container, false);
        context = rootView.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayButton = (Button) view.findViewById(R.id.button_day);
        weekButton = (Button) view.findViewById(R.id.button_week);
        monthButton = (Button) view.findViewById(R.id.button_month);
        allButton = (Button) view.findViewById(R.id.button_all);
        statisticImage = (ImageView) view.findViewById(R.id.image_statistics);
        Utils.singleSelectionButtons(dayButton, dayButton, weekButton, monthButton, allButton);
        Glide.with(context).load(R.drawable.stat_day).into(statisticImage);
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(dayButton, dayButton, weekButton, monthButton, allButton);
                Glide.with(context).load(R.drawable.stat_day).into(statisticImage);
            }
        });
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(weekButton, dayButton, weekButton, monthButton, allButton);
            }
        });
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(monthButton, dayButton, weekButton, monthButton, allButton);
                Glide.with(context).load(R.drawable.stat_month).into(statisticImage);
            }
        });
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.singleSelectionButtons(allButton, dayButton, weekButton, monthButton, allButton);
            }
        });
    }

}
