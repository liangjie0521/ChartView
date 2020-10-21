package com.json.chart.widget.ani;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import com.json.chart.widget.data.CirclePoint;

import java.util.List;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public class DefaultCircleChartAnimation extends BaseChartAnimation {

    public void animation(List<CirclePoint> points) {
        for (CirclePoint point : points) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(point, "screenDegrees", 0, point.screenDegrees);
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
