package com.json.bbchart.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.json.bbchart.R;
import com.json.chart.widget.chart.CircleChartView;
import com.json.chart.widget.chart.LineChartView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private CircleChartView mCircleChartView;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        pageViewModel.initData();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        pageViewModel.getCircleIndex().observe(getViewLifecycleOwner(), (s) -> {
            mCircleChartView.setColorList(pageViewModel.getColors().getValue());
            mCircleChartView.setLabels(pageViewModel.getLabels().getValue());
            mCircleChartView.setValues(pageViewModel.getValues().getValue());
            mCircleChartView.show();
        });
        mCircleChartView = root.findViewById(R.id.mine_chart);
        LineChartView lineChartView = root.findViewById(R.id.line_chart);
        pageViewModel.getLineIndex().observe(getViewLifecycleOwner(), (s) -> {
            lineChartView.setColorList(pageViewModel.getLineColors().getValue());
            lineChartView.setLabels(pageViewModel.getLineLabels().getValue());
            lineChartView.setLines(pageViewModel.getLines().getValue());
            lineChartView.show();
        });
        root.findViewById(R.id.clean).setOnClickListener(v -> lineChartView.cleanData(true));
        root.findViewById(R.id.generate_data).setOnClickListener(v -> {
            lineChartView.cleanData(false);
            pageViewModel.generateLineData(true);
        });
        return root;
    }
}