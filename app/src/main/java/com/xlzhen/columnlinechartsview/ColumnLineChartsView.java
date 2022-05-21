package com.xlzhen.columnlinechartsview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

public class ColumnLineChartsView extends View {
    private int width, height;
    private int calibrationLineColor;//刻度线的颜色
    private Paint calibrationLinePaint;//刻度线的颜色Paint

    private int describeColor;//描述文字颜色
    private Paint textPaint;//描述文字Paint

    private int[] columnColor;//柱状图颜色
    private String columnDescribe;//柱状图描述
    private Paint columnPaint, columnDescribePaint;
    private Rect columnDescribeColorRect;//柱状图描述下的颜色方块

    private int lineColor;//线型图颜色
    private String lineDescribe;//线型图描述
    private Paint linePaint, lineDescribePaint;
    private Rect lineDescribeColorRect;//柱状图描述下的颜色方块

    private float[] columnCalibration;//柱状刻度
    private float columnScale;//柱状刻度和View的相对比例
    private float[] lineCalibration;//线状刻度
    private float lineScale;//线状刻度和View的相对比例
    private float[] timeCalibration;//时间刻度
    private float timeScale;//时间刻度和View的相对比例

    private float[] columns;
    private float[] lines;

    private float[] animColumns;
    private float[] animLines;

    private boolean animator;//是否在执行动画

    private int dp2, dp3, dp5, dp7, dp10, dp12, dp14, dp15, dp17, dp18, dp19, dp20, dp23, dp25, dp30, dp35, dp40, dp45, dp50, dp60, dp70;

    public ColumnLineChartsView(Context context) throws Exception {
        super(context);
        throw new Exception("Please not create from Code!");
    }

    public ColumnLineChartsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        columnCalibration = new float[]{0, 2, 4, 6, 8, 10};
        lineCalibration = new float[]{0, 50, 100, 150, 200, 250};
        timeCalibration = new float[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        dp2 = DensityUtils.dp2px(2);
        dp3 = DensityUtils.dp2px(3);
        dp5 = DensityUtils.dp2px(5);
        dp7 = DensityUtils.dp2px(7);
        dp10 = DensityUtils.dp2px(10);
        dp12 = DensityUtils.dp2px(12);
        dp14 = DensityUtils.dp2px(14);
        dp17 = DensityUtils.dp2px(17);
        dp15 = DensityUtils.dp2px(15);
        dp18 = DensityUtils.dp2px(18);
        dp19 = DensityUtils.dp2px(19);
        dp20 = DensityUtils.dp2px(20);
        dp23 = DensityUtils.dp2px(23);
        dp25 = DensityUtils.dp2px(25);
        dp30 = DensityUtils.dp2px(30);
        dp35 = DensityUtils.dp2px(35);
        dp40 = DensityUtils.dp2px(40);
        dp45 = DensityUtils.dp2px(45);
        dp50 = DensityUtils.dp2px(50);
        dp60 = DensityUtils.dp2px(60);
        dp70 = DensityUtils.dp2px(70);
        if (attrs == null)
            return;
        @SuppressLint("Recycle") TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColumnLineChartsView);
        calibrationLineColor = array.getColor(R.styleable.ColumnLineChartsView_calibrationLineColor, getResources().getColor(R.color.color_c6));
        describeColor = array.getColor(R.styleable.ColumnLineChartsView_describeColor, getResources().getColor(R.color.color_999));
        int startColor = array.getColor(R.styleable.ColumnLineChartsView_columnStartColor, getResources().getColor(R.color.blue_column_start_color));
        int endColor = array.getColor(R.styleable.ColumnLineChartsView_columnEndColor, getResources().getColor(R.color.blue_column_end_color));
        lineColor = array.getColor(R.styleable.ColumnLineChartsView_lineColor, getResources().getColor(R.color.purple_200));
        columnColor = new int[]{startColor, endColor};
        columnDescribe = array.getString(R.styleable.ColumnLineChartsView_columnDescribe);
        if (columnDescribe == null) {
            columnDescribe = getResources().getString(R.string.column_describe);
        }
        lineDescribe = array.getString(R.styleable.ColumnLineChartsView_lineDescribe);
        if (lineDescribe == null) {
            lineDescribe = getResources().getString(R.string.line_describe);
        }

        columnPaint = new Paint();
        columnPaint.setAntiAlias(true);

        columnDescribePaint = new Paint();
        columnDescribePaint.setAntiAlias(true);
        columnDescribePaint.setColor(columnColor[0]);


        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(DensityUtils.dp2px(1));

        lineDescribePaint = new Paint();
        lineDescribePaint.setColor(lineColor);
        lineDescribePaint.setAntiAlias(true);

        calibrationLinePaint = new Paint();
        calibrationLinePaint.setColor(calibrationLineColor);
        calibrationLinePaint.setAntiAlias(true);

        textPaint = new TextPaint();
        textPaint.setTextSize(DensityUtils.sp2px(8));
        textPaint.setColor(describeColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        setChartsData(new float[]{2f, 1.2f, 2.3f, 3f, 4f, 5.6f, 6.7f, 7.8f, 1.8f
                        , 7f, 6f, 4f, 3f, 2f, 1f, 2.3f, 4.1f, 2.1f, 3.3f, 0.1f, 2.2f}
                , new float[]{10f, 20f, 30f, 80f, 50f, 60f, 70f, 80f, 70f, 60f, 65f, 22f, 32f, 86f, 120f
                        , 220f, 200f, 180f, 160f, 100f});
    }

    public void setChartsData(float[] columns, float[] lines) {
        this.columns = columns;
        this.lines = lines;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        //以下是绘制左右两边的介绍
        canvas.rotate(270, width / 2f, height / 2f);

        canvas.translate(0f, -width / 2f);
        canvas.drawText(columnDescribe, width / 2f + dp10, height / 2f + dp10, textPaint);
        canvas.drawRect(columnDescribeColorRect, columnDescribePaint);

        canvas.translate(0f, width);
        canvas.drawText(lineDescribe, width / 2f + dp10, height / 2f - dp10, textPaint);
        canvas.drawRect(lineDescribeColorRect, lineDescribePaint);
        canvas.restore();

        //接下来绘制刻度尺
        for (float v : timeCalibration) {
            canvas.drawText((int) v + ""
                    , v * timeScale + dp30, height - dp5, textPaint);
        }

        for (int i = 0; i < lineCalibration.length; i++) {
            float y = (lineCalibration[lineCalibration.length - i - 1]) * lineScale + dp20;
            canvas.drawText((int) lineCalibration[i] + "", width - dp30
                    , y, textPaint);
        }

        for (int i = 0; i < columnCalibration.length; i++) {
            float y = (columnCalibration[columnCalibration.length - i - 1]) * columnScale + dp20;
            canvas.drawText((int) columnCalibration[i] + "", dp20, y, textPaint);
        }
        //绘制一列一列横着的线条
        for (int i = 0; i < columnCalibration.length; i++) {
            float startY = (columnCalibration[columnCalibration.length - i - 1]) * columnScale + dp17;
            float endY = (lineCalibration[lineCalibration.length - i - 1]) * lineScale + dp17;
            canvas.drawLine(dp30, startY, width - dp45, endY, calibrationLinePaint);
        }

        //绘制柱状图
        if (animator) {
            drawCharts(canvas, animColumns, animLines);
        } else {
            drawCharts(canvas, columns, lines);
        }
    }

    private void drawCharts(Canvas canvas, float[] columns, float[] lines) {
        if (lines != null && columns != null) {
            for (int i = 0; i < columns.length; i++) {
                if (i > timeCalibration.length - 2) {
                    break;//超出时间轴了
                }
                float y = (columnCalibration[columnCalibration.length - 1] - columns[i])
                        * columnScale + dp17;
                columnPaint.setShader(new LinearGradient(timeScale * (i + 1) + dp20, y
                        , timeScale * (i + 1) + dp20, height - dp23, columnColor[0], columnColor[1]
                        , Shader.TileMode.MIRROR));
                canvas.drawLine(timeScale * (i + 1) + dp20, y, timeScale * (i + 1) + dp20, height - dp23, columnPaint);
            }
            Path path = new Path();
            for (int i = 0; i < lines.length; i++) {
                if (i > timeCalibration.length - 2) {
                    break;//超出时间轴了
                }
                float startY = (lineCalibration[lineCalibration.length - 1] - lines[i])
                        * lineScale + dp17;
                float startX = timeScale * (i + 1) + dp20;
                if (i == 0) {
                    path.moveTo(startX, startY);
                } else {
                    path.lineTo(startX, startY);
                }
            }
            canvas.drawPath(path, linePaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        timeScale = (width - dp70) / timeCalibration[timeCalibration.length - 1];
        columnPaint.setStrokeWidth(timeScale);
        lineScale = (height - dp40) / lineCalibration[lineCalibration.length - 1];
        columnScale = (height - dp40) / columnCalibration[columnCalibration.length - 1];

        columnDescribeColorRect = new Rect(width / 2 - dp30, height / 2 + dp5, width / 2 - dp25
                , height / 2 + dp10);

        lineDescribeColorRect = new Rect(width / 2 - dp30, height / 2 - dp10, width / 2 - dp25
                , height / 2 - dp15);
    }

    public void setTimeCalibration(float[] timeCalibration) {
        this.timeCalibration = timeCalibration;
        timeScale = (width - dp70) / timeCalibration[timeCalibration.length - 1];
        columnPaint.setStrokeWidth(timeScale);
    }

    /**
     * 开始动画
     */
    public void startAnimator() {
        animColumns = new float[columns.length];
        animLines = new float[lines.length];
        ValueAnimator columnAnimator = ValueAnimator.ofFloat(0f, columnCalibration[columnCalibration.length - 1]);
        columnAnimator.setDuration(3000);
        columnAnimator.setInterpolator(new DecelerateInterpolator());
        columnAnimator.addUpdateListener(animation -> {
            for (int i = 0; i < animColumns.length; i++) {
                if (animColumns[i] < columns[i]) {
                    animColumns[i] = (float) animation.getAnimatedValue();
                }
            }
        });
        columnAnimator.start();

        ValueAnimator lineAnimator = ValueAnimator.ofFloat(0f, lineCalibration[lineCalibration.length - 1]);
        lineAnimator.setDuration(3000);
        lineAnimator.setInterpolator(new DecelerateInterpolator());
        lineAnimator.addUpdateListener(animation -> {
            for (int i = 0; i < animLines.length; i++) {
                if (animLines[i] < lines[i]) {
                    animLines[i] = (float) animation.getAnimatedValue();
                }
            }
            invalidate();
        });
        lineAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator = false;
            }
        });
        lineAnimator.start();
        animator = true;
    }
}
