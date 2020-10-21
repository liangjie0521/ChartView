package com.json.bbchart.ui.main;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    private MutableLiveData<Integer> circleIndex = new MutableLiveData<>();

    private MutableLiveData<ArrayList<String>> labels = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Integer>> colors = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Float>> values = new MutableLiveData<>();

    private MutableLiveData<Integer> lineIndex = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Integer>> lineColors = new MutableLiveData<>();

    private MutableLiveData<ArrayList<ArrayList<Float>>> lines = new MutableLiveData<>();

    private MutableLiveData<ArrayList<String>> lineLabels = new MutableLiveData<>();

    public MutableLiveData<Integer> getCircleIndex() {
        return circleIndex;
    }

    public LiveData<ArrayList<Float>> getValues() {
        return values;
    }

    public LiveData<ArrayList<String>> getLabels() {
        return labels;
    }

    public LiveData<ArrayList<Integer>> getColors() {
        return colors;
    }

    public MutableLiveData<Integer> getIndex() {
        return mIndex;
    }

    public MutableLiveData<Integer> getLineIndex() {
        return lineIndex;
    }

    public MutableLiveData<ArrayList<Integer>> getLineColors() {
        return lineColors;
    }

    public MutableLiveData<ArrayList<ArrayList<Float>>> getLines() {
        return lines;
    }

    public MutableLiveData<ArrayList<String>> getLineLabels() {
        return lineLabels;
    }

    public void initData() {
        Log.d("Model", "==initData====");
        ArrayList<String> lData = new ArrayList<>();
        lData.add("实到人数");
        lData.add("总人数");
        lData.add("未到");
        labels.setValue(lData);


        ArrayList<Integer> cData = new ArrayList<>();
        cData.add(Color.parseColor("#FF03DAE8"));
        cData.add(Color.parseColor("#FFFFDF28"));
        cData.add(Color.parseColor("#FFEEEEEE"));
        colors.setValue(cData);

        ArrayList<Float> vData = new ArrayList<>();
        vData.add(80f);
        vData.add(100f);
        vData.add(20f);
        values.setValue(vData);
        circleIndex.setValue(1);

        //line Data

        generateLineData(false);

        lineIndex.setValue(1);
    }

    public void generateLineData(boolean setValue) {
        ArrayList<String> lineLabel = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            lineLabel.add(8 + "/" + (i + 1));
        }
        lineLabels.setValue(lineLabel);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#03DAE8"));
        colors.add(Color.parseColor("#FFDF28"));
        lineColors.setValue(colors);

        ArrayList<ArrayList<Float>> als = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            ArrayList<Float> l = new ArrayList<>();
            for (int j = 0; j < lineLabel.size(); j++) {
                l.add(0f + new Random().nextInt(500));
            }
            als.add(l);
        }
        lines.setValue(als);
        if (setValue)
            lineIndex.setValue(lineIndex.getValue() + 1);
    }
}