package com.example.mycareshoe.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;

import org.jetbrains.annotations.NotNull;

public class StatisticsFragment extends Fragment {
    Button pressurePlotStats;
    Button pressureIllustratedStats;
    private PressurePlotStatisticsFragment pressurePlotStatisticsFragment = new PressurePlotStatisticsFragment();


    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_statistics, container, false);


    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.statistics_en));

        pressurePlotStats = (Button) view.findViewById(R.id.plotted_pressure_stats_btn);
        pressureIllustratedStats = (Button) view.findViewById(R.id.illustrated_pressure_stats_btn);

        pressurePlotStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.contentFragment);

                frameLayout.removeAllViews();
                getParentFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), pressurePlotStatisticsFragment).addToBackStack("pressurePlotStats").commit();

            }
        });

        pressureIllustratedStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.contentFragment);

                frameLayout.removeAllViews();
                getParentFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), new PressureStatisticsFragment()).addToBackStack("pressureIllustratedStats").commit();

            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);


    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


}