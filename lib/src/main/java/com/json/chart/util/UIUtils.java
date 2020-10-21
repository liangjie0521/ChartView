package com.json.chart.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public class UIUtils {

    public static int dp2px(int dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * displayMetrics.density + 0.5);
    }

    public static int sp2px(int sp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return (int) (sp * displayMetrics.scaledDensity + 0.5);
    }

}
