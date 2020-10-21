package com.json.chart.widget.data;

/**
 * Created on 2020/10/21.
 *
 * @author json
 */
public class ChartPoint {
    public float pointX;
    public float pointY;
    public String label;
    public float value;

    public ChartPoint(float pointX, float pointY, String label, float value) {
        this.pointX = pointX;
        this.pointY = pointY;
        this.label = label;
        this.value = value;
    }

    public float getPointX() {
        return pointX;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public float getPointY() {
        return pointY;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
