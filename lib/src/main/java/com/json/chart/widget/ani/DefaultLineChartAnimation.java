package com.json.chart.widget.ani;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import com.json.chart.widget.data.ChartPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public class DefaultLineChartAnimation extends BaseChartAnimation {

    public void animation(List<ArrayList<ChartPoint>> lines, float startPosition) {
        for (ArrayList<ChartPoint> line : lines) {
            for (ChartPoint point : line) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(point, "pointY", startPosition, point.pointY);
                animator.setDuration(duration);
                animator.setInterpolator(mInterpolator);
                animator.start();
            }
        }

        ValueAnimator global = ValueAnimator.ofInt(0, 1);
        global.setDuration(duration);
        global.setInterpolator(mInterpolator);
        if (mListener != null)
            global.addUpdateListener(mListener);
        global.start();
    }

    public void animation(ArrayList<ChartPoint> line, float startPosition) {
        for (ChartPoint point : line) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(point, "pointY", startPosition, point.pointY);
            animator.setDuration(duration);
            animator.setInterpolator(mInterpolator);
            animator.start();
        }

        ValueAnimator global = ValueAnimator.ofInt(0, 1);
        global.setDuration(duration);
        global.setInterpolator(mInterpolator);
        if (mListener != null)
            global.addUpdateListener(mListener);
        global.start();
    }
}
