package com.json.chart.widget.chart;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public enum AxisType {
    NONE,
    X,
    Y,
    XY;

    static AxisType get(int value) {
        if (value == 0)
            return NONE;
        else if (value == 1)
            return X;
        else if (value == 2)
            return Y;
        else if (value == 3)
            return XY;
        return NONE;
    }
}
