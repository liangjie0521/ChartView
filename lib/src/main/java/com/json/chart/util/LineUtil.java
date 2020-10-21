package com.json.chart.util;

import android.graphics.Path;

import com.json.chart.widget.data.ChartPoint;

import java.util.List;

/**
 * Created on 2020/10/21.
 *
 * @author json
 *
 */
public class LineUtil {
    public static Path covertToLine(List<ChartPoint> line) {
        Path path = new Path();
        for (int i = 0; i < line.size(); i++) {
            ChartPoint point = line.get(i);
            if (i == 0) {
                path.moveTo(point.pointX, point.pointY);
            } else {
                path.lineTo(point.pointX, point.pointY);
            }
        }
        return path;
    }

    public static Path covertToSmoothPath(List<ChartPoint> line, float smoothFactor) {
        Path path = new Path();
        path.moveTo(line.get(0).pointX, line.get(1).pointY);
        float thisPointX, thisPointY, nextPointX, nextPointY, startDiffX, startDiffY, endDiffX,
                endDiffY, firstControlX, firstControlY, secondControlX, secondControlY;
        int lineSize = line.size();
        for (int i = 0; i < lineSize - 1; i++) {
            thisPointX = line.get(i).pointX;
            thisPointY = line.get(i).pointY;
            nextPointX = line.get(i + 1).pointX;
            nextPointY = line.get(i + 1).pointY;

            startDiffX = nextPointX - line.get(si(i - 1, lineSize)).pointX;
            startDiffY = nextPointY - line.get(si(i - 1, lineSize)).pointY;
            endDiffX = line.get(si(i + 2, lineSize)).pointX - thisPointX;
            endDiffY = line.get(si(i + 2, lineSize)).pointY - thisPointY;

            firstControlX = thisPointX + smoothFactor * startDiffX;
            firstControlY = thisPointY + smoothFactor * startDiffY;

            secondControlX = nextPointX - smoothFactor * endDiffX;
            secondControlY = nextPointY - smoothFactor * endDiffY;

            path.cubicTo(firstControlX, firstControlY, secondControlX, secondControlY, nextPointX, nextPointY);
        }

        return path;
    }

    private static int si(int i, int size) {
        if (i > size - 1)
            return size - 1;
        else return Math.max(i, 0);
    }
}
