package com.json.chart.widget.data;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public class CirclePoint {
    public float value;//值
    public int color;//颜色
    public float screenDegrees;//弧度
    public String label;//文本

    public CirclePoint(float value, int color, float screenDegrees, String label) {
        this.value = value;
        this.color = color;
        this.screenDegrees = screenDegrees;
        this.label = label;
    }

    public float getScreenDegrees() {
        return screenDegrees;
    }

    public void setScreenDegrees(float screenDegrees) {
        this.screenDegrees = screenDegrees;
    }
}
