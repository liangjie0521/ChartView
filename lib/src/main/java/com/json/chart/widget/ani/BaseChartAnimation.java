package com.json.chart.widget.ani;

import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public abstract class BaseChartAnimation {
    protected long duration = 1000L;
    protected Interpolator mInterpolator = new DecelerateInterpolator();

    protected ValueAnimator.AnimatorUpdateListener mListener;

    public BaseChartAnimation() {
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setListener(ValueAnimator.AnimatorUpdateListener listener) {
        mListener = listener;
    }
}
