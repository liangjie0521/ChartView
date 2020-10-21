package com.json.chart.widget.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.json.chart.R;
import com.json.chart.util.LineUtil;
import com.json.chart.util.UIUtils;
import com.json.chart.widget.ani.DefaultLineChartAnimation;
import com.json.chart.widget.data.ChartPoint;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created on 2020/10/21.
 *
 * @author json
 * 折线、曲线图
 */
public class LineChartView extends BaseChartView {
    private AxisType mAxisType = AxisType.NONE;
    private float labelTextSize;
    private int labelTextColor = Color.parseColor("#FF999999");
    private int xAxisNum = 3;//x轴标签个数
    private int yAxisNum = 6;//y轴标签个数
    private int max, min;//最小、最大的值

    private boolean isSmooth;//是否曲线

    private float lineWidth;//线条宽度
    private boolean drawShader;//是否绘制渐变
    private boolean drawShadow;//是否绘制线条阴影
    private float shadowRadius;
    private float shadowDx;
    private float shadowDy;
    private int dashColor = Color.parseColor("#FFEEEEEE");//虚线颜色

    private List<String> yLabels = new ArrayList<>();//y轴标签
    private List<ChartPoint> xLabels = new ArrayList<>();//x轴标签
    private List<ArrayList<ChartPoint>> linesData = new ArrayList<>();//所有曲线

    private List<ArrayList<Float>> lines = new ArrayList<>();//所有数据
    private List<RectF> clickRegion = new ArrayList<>();
    private int selectIndex = -1;
    private int startX, startY;//起始 x,y
    private Paint mPaint, dashPaint, shaderPaint;//标准、虚线、渐变
    private RectF mRectF;
    private DefaultLineChartAnimation mAnimation;

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineChartView, defStyleAttr, defStyleRes);
        mAxisType = AxisType.get(ta.getInt(R.styleable.LineChartView_line_chart_axis, 0));
        labelTextSize = ta.getDimensionPixelSize(R.styleable.LineChartView_line_chart_label_size, UIUtils.dp2px(12));
        labelTextColor = ta.getColor(R.styleable.LineChartView_line_chart_label_color, labelTextColor);
        isSmooth = ta.getBoolean(R.styleable.LineChartView_line_chart_smooth, false);
        xAxisNum = ta.getInt(R.styleable.LineChartView_line_chart_axis_x_num, 3);
        yAxisNum = ta.getInt(R.styleable.LineChartView_line_chart_axis_y_num, 6);
        lineWidth = ta.getDimensionPixelSize(R.styleable.LineChartView_line_chart_line_width, UIUtils.dp2px(1));
        drawShader = ta.getBoolean(R.styleable.LineChartView_line_chart_line_shader, false);
        drawShadow = ta.getBoolean(R.styleable.LineChartView_line_chart_shadow, true);
        shadowRadius = ta.getDimensionPixelSize(R.styleable.LineChartView_line_chart_shadow_radius, UIUtils.dp2px(6));
        shadowDx = ta.getDimensionPixelSize(R.styleable.LineChartView_line_chart_shadow_dx, 0);
        shadowDy = ta.getDimensionPixelSize(R.styleable.LineChartView_line_chart_shadow_dy, UIUtils.dp2px(2));
        dashColor = ta.getColor(R.styleable.LineChartView_line_chart_dash_color, dashColor);

        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(labelTextSize);

        dashPaint = new Paint();
        dashPaint.setColor(dashColor);
        dashPaint.setStrokeWidth(UIUtils.dp2px(1));
        dashPaint.setPathEffect(new DashPathEffect(new float[]{4f, 4f}, 0));

        mAnimation = new DefaultLineChartAnimation();
        mAnimation.setListener(animation -> {
            postInvalidate();
        });
    }

    private void initData() {
        linesData.clear();
        xLabels.clear();
        yLabels.clear();
        clickRegion.clear();
        selectIndex = -1;
        min = 0;
        max = yAxisNum - 1;
    }

    @Override
    void draw() {
        if (mRectF == null)
            return;
        if (shouldDrawAxisX())
            drawXAxis();
        if (shouldDrawAxisY())
            drawYAxis();
        drawLine();

    }

    @Override
    void convertData() {
        initData();
        getYLabel();
        getChartRectF();

        int labelSize = labels.size();
        float chartWidth = mRectF.right - mRectF.left;
        float chartHeight = mRectF.bottom - mRectF.top;
        float dif = labelSize > 1 ? chartWidth / (labelSize - 1) : chartWidth / 2;
        int labelDif = labelSize > xAxisNum ? (labelSize / (xAxisNum - 1)) : 1;
        for (int i = 0; i < lines.size(); i++) {
            ArrayList<Float> l = lines.get(i);
            ArrayList<ChartPoint> cpL = new ArrayList<>();
            for (int j = 0; j < l.size(); j++) {
                if (j <= labelSize) {
                    float y = startY - ((l.get(j) - min) * chartHeight / (max - min));
                    float x = l.size() == 1 ? startX + dif : startX + j * dif;
                    ChartPoint point = new ChartPoint(x, y, labels.get(j), l.get(j));
                    cpL.add(point);
                    if (i == 0) {
                        clickRegion.add(new RectF(x - dif / 2, mRectF.top, x + dif / 2, mRectF.bottom));
                    }
                    //获取x轴标签坐标
                    if (i == 0 && j % labelDif == 0) {
                        xLabels.add(point);
                    }
                    if (xLabels.size() < xAxisNum && j == labelSize - 1) //x轴最后一个
                    {
                        xLabels.add(point);
                    }
                }
            }
            linesData.add(cpL);
        }
    }

    @Override
    public void show() {
        post(() -> {
            convertData();
            mAnimation.animation(linesData, startY);
        });
    }

    /**
     * 获取Y轴文字
     */
    private void getYLabel() {
        for (int i = 0; i < lines.size(); i++) {
            ArrayList<Float> l = lines.get(i);
            for (int j = 0; j < l.size(); j++) {
                float temp = l.get(j);
                if (temp < min)
                    min = (int) Math.floor(temp);
                if (temp > max)
                    max = (int) Math.ceil(temp);
            }
        }
        if (max == min)
            max = yAxisNum - 1;
        if ((max - min) % (yAxisNum - 1) != 0) {
            max = ((max - min) / (yAxisNum - 1) + 1) * (yAxisNum - 1);
        }
        int dif = (max - min) / (yAxisNum - 1);
        for (int i = 0; i < yAxisNum; i++) {
            yLabels.add(String.valueOf(min + dif * i));
        }
    }

    /**
     * 计算 图表区域
     */
    private void getChartRectF() {
        startX = getPaddingLeft() + UIUtils.dp2px(5);
        if (shouldDrawAxisY()) {
            startX = getPaddingLeft() + (int) (mPaint.measureText(yLabels.get(yAxisNum - 1), 0,
                    yLabels.get(yAxisNum - 1).length()) + UIUtils.dp2px(10));
        }
        float top = getPaddingTop() + UIUtils.dp2px(15);
        float right = getMeasuredWidth() - getPaddingRight() - UIUtils.dp2px(12);
        startY = getMeasuredHeight() - getPaddingBottom() - UIUtils.dp2px(10);
        if (shouldDrawAxisX()) {
            startY = (int) (startY - labelTextSize);
        }
        mRectF = new RectF(startX, top, right, startY);
    }

    private boolean shouldDrawAxisX() {
        return mAxisType == AxisType.X || AxisType.XY == mAxisType;
    }

    private boolean shouldDrawAxisY() {
        return mAxisType == AxisType.Y || mAxisType == AxisType.XY;
    }

    /**
     * 绘制x轴
     */
    private void drawXAxis() {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(labelTextColor);
        int labelY = getMeasuredHeight() - UIUtils.dp2px(5);
        for (int i = 0; i < xLabels.size(); i++) {
            ChartPoint point = xLabels.get(i);
            if (i == xLabels.size() - 1)//x轴 标签最后一个显示不全，向左偏移
            {
                mCanvas.drawText(point.label, point.pointX - UIUtils.dp2px(3), labelY, mPaint);
            } else {
                mCanvas.drawText(point.label, point.pointX, labelY, mPaint);
            }
        }
    }

    //绘制Y轴
    private void drawYAxis() {
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(labelTextColor);
        int hegit = (int) (mRectF.bottom - mRectF.top);
        int dif = hegit / (yAxisNum - 1);
        float labelLeft = startX - UIUtils.dp2px(10);
        for (int i = 0; i < yAxisNum; i++) {
            mCanvas.drawText(yLabels.get(i), labelLeft, startY - dif * i + mPaint.getFontMetricsInt().bottom, mPaint);
        }

        for (int i = 0; i < yLabels.size(); i++) {
            dashPaint.setColor(dashColor);
            mCanvas.drawLine(labelLeft + UIUtils.dp2px(5), startY - dif * i,
                    mRectF.right + UIUtils.dp2px(5), startY - dif * i, dashPaint);
        }
    }

    /**
     * 绘制曲线/折线
     */
    private void drawLine() {
        for (int i = 0; i < linesData.size(); i++) {
            List<ChartPoint> line = linesData.get(i);
            if (line.size() == 1) {
                drawPoint(line.get(0), colorList.get(i));
                return;
            }
            Path path;
            if (isSmooth)
                path = LineUtil.covertToSmoothPath(line, 0.1f);
            else
                path = LineUtil.covertToLine(line);
            mPaint.setColor(colorList.get(i));
            mPaint.setStrokeWidth(lineWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            if (drawShadow)
                mPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, getShadowColor(0.4f, colorList.get(i)));//带阴影的线
            mCanvas.drawPath(path, mPaint);
            mPaint.clearShadowLayer();
            if (drawShader)
                drawLineBackground(path, line, colorList.get(i));
        }
    }

    private int getShadowColor(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    /**
     * 绘制圆点
     *
     * @param point 点的坐标
     * @param color 点的颜色
     */
    private void drawPoint(ChartPoint point, int color) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(lineWidth * 2);
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawCircle(point.pointX, point.pointY, lineWidth, mPaint);
    }

    /**
     * 绘制线条下渐变背景
     *
     * @param path  线条路径
     * @param line  所有点
     * @param color 线条颜色
     */
    private void drawLineBackground(Path path, List<ChartPoint> line, int color) {
        path.lineTo(line.get(line.size() - 1).pointX, startY);
        path.lineTo(line.get(0).pointX, startY);
        if (shaderPaint == null) {
            shaderPaint = new Paint();
            shaderPaint.setStyle(Paint.Style.FILL);
        }
        shaderPaint.setShader(getGradient(color));
        mCanvas.drawPath(path, shaderPaint);
    }

    /**
     * 获取竖直方向渐变效果
     *
     * @param color 色值
     */
    private LinearGradient getGradient(int color) {
        return new LinearGradient(mRectF.left, mRectF.top, mRectF.left, mRectF.bottom, color, Color.TRANSPARENT, Shader.TileMode.MIRROR);
    }

    public void cleanData(boolean refresh) {
        initData();
        lines.clear();
        mRectF = null;
        if (refresh)
            postInvalidate();
    }

    public void setLines(List<ArrayList<Float>> lines) {
        this.lines = lines;
    }

    public void addLine(ArrayList<Float> line) {
        this.lines.add(line);
    }
}
