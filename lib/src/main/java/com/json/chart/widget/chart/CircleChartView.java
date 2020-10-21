package com.json.chart.widget.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.json.chart.R;
import com.json.chart.util.UIUtils;
import com.json.chart.widget.ani.DefaultCircleChartAnimation;
import com.json.chart.widget.data.CirclePoint;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public class CircleChartView extends BaseChartView implements ValueAnimator.AnimatorUpdateListener {

    private DefaultCircleChartAnimation mAnimation;

    private boolean isPie = false;//是否是饼状图

    private float barWidth;//圆环宽度
    private int barBackgroundColor = Color.TRANSPARENT;//圆环背景色
    private boolean isBarCorners;//环是否有圆角
    private boolean isAnimation;//是否有动画
    private float total;//数值总和

    private Paint mPaint;
    private List<CirclePoint> mPointList = new ArrayList<>();

    public CircleChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleChartView, defStyleAttr, defStyleRes);
        isPie = ta.getInt(R.styleable.CircleChartView_chart_mode, 1) == 0;
        barWidth = ta.getDimensionPixelSize(R.styleable.CircleChartView_chart_bar_width, UIUtils.dp2px(10));
        barBackgroundColor = ta.getColor(R.styleable.CircleChartView_chart_bar_backgroundColor, barBackgroundColor);
        isBarCorners = ta.getBoolean(R.styleable.CircleChartView_chart_bar_corners, true);
        isAnimation = ta.getBoolean(R.styleable.CircleChartView_chart_bar_animation, true);
        total = ta.getFloat(R.styleable.CircleChartView_chart_total_value, 100f);
        ta.recycle();

        mAnimation = new DefaultCircleChartAnimation();
        mAnimation.setListener(this);
        //初始化画笔
        mPaint = new Paint();
        if (isPie) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(barWidth);
            if (isBarCorners) {
                mPaint.setStrokeCap(Paint.Cap.ROUND);
            }
        }
        mPaint.setAntiAlias(true);
    }

    @Override
    void draw() {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        RectF mRectF;
        if (isPie) {
            mRectF = new RectF(paddingLeft, paddingTop,
                    width - paddingLeft - paddingRight,
                    height - paddingBottom - paddingTop);
        } else {
            mRectF = new RectF(paddingLeft + barWidth / 2, paddingTop + barWidth / 2,
                    width - paddingLeft - paddingRight - barWidth / 2,
                    height - paddingBottom - paddingTop - barWidth / 2);

            mPaint.setColor(barBackgroundColor);
            mCanvas.drawCircle(mRectF.left + (mRectF.right - mRectF.left) / 2, mRectF.top +
                    (mRectF.bottom - mRectF.top) / 2, (mRectF.bottom - mRectF.top) / 2, mPaint);
        }

        for (int i = mPointList.size() - 1; i >= 0; i--) {
            CirclePoint point = mPointList.get(i);
            mPaint.setColor(point.color);
            mCanvas.drawArc(mRectF, -90, point.screenDegrees, isPie, mPaint);
        }
    }

    @Override
    void convertData() {
        if (colorList.size() == 0) {
            Log.d(TAG, "please set colors's data");
            return;
        }
        if (values.size() == 0) {
            Log.d(TAG, "data is empty,please call function setValues()");
            return;
        }
        if (labels.size() == 0) {
            Log.d(TAG, "labels is empty,please call function setLabels()");
            return;
        }
        mPointList.clear();
        if (values.size() == colorList.size() && values.size() == labels.size()) {
            getTotalByValues();
            for (int i = 0; i < colorList.size(); i++) {
                float degrees = values.get(i) * 360 / total;
                if (i > 0)
                    degrees += mPointList.get(i - 1).screenDegrees;
                mPointList.add(new CirclePoint(values.get(i), colorList.get(i), degrees, labels.get(i)));
            }
        }
    }

    @Override
    public void show() {
        mPointList.clear();
        convertData();
        if (isAnimation) {
            mAnimation.animation(mPointList);
        } else {
            postInvalidate();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        postInvalidate();
    }

    private void getTotalByValues() {
        total = 0;
        for (float v : values) {
            total += v;
        }
    }
}
