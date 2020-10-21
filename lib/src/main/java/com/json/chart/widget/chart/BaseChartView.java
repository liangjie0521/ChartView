package com.json.chart.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public abstract class BaseChartView extends View {
    protected static final String TAG = BaseChartView.class.getName();
    protected Canvas mCanvas;
    protected List<String> labels = new ArrayList<>();
    protected List<Float> values = new ArrayList<>();
    protected List<Integer> colorList = new ArrayList<>();

    public BaseChartView(Context context) {
        super(context);
    }

    public BaseChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    abstract void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        draw();
    }

    abstract void draw();

    abstract void convertData();

    public abstract void show();

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void setColorList(List<Integer> colorList) {
        this.colorList = colorList;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

}
