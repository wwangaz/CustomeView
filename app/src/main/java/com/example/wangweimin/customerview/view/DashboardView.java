package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.example.wangweimin.customerview.ScreenUtils;

/**
 * Created by wangweimin on 16/10/11.
 */

public class DashboardView extends View {

    private static final int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.BLACK;

    private static final int[] DEFAULT_PROGRESS_COLORS = {Color.YELLOW, Color.BLUE, Color.RED, Color.TRANSPARENT};

    private int DEFAULT_PROGRESS_ITEM_WIDTH = (int) ScreenUtils.convertDp2Px(6);

    private static final int DEFAULT_START_ANGLE = -240;

    private static final int DEFAULT_SWEEP_ANGLE = 300;

    private int mBackgroundProgressColor;

    private int mStartAngle;

    private int mSweepAngle;

    private int[] mProgressColors;

    private int mProgressItemWidth;

    private RectF mProgressRectangle;

    private Paint mBackgroundProgressPaint;

    private Paint mProgressPaint;

    private float mSweepPercentage;

    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBackgroundProgressColor = DEFAULT_BACKGROUND_PROGRESS_COLOR;
        mProgressColors = DEFAULT_PROGRESS_COLORS;
        mProgressItemWidth = DEFAULT_PROGRESS_ITEM_WIDTH;
        mStartAngle = DEFAULT_START_ANGLE;
        mSweepAngle = DEFAULT_SWEEP_ANGLE;

        mProgressRectangle = new RectF();

        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{ScreenUtils.convertDp2Px(3), ScreenUtils.convertDp2Px(6)}, 0);

        mBackgroundProgressPaint = new Paint();
        mBackgroundProgressPaint.setAntiAlias(true);
        mBackgroundProgressPaint.setStyle(Paint.Style.STROKE);
        mBackgroundProgressPaint.setPathEffect(dashPathEffect);
        mBackgroundProgressPaint.setColor(mBackgroundProgressColor);
        mBackgroundProgressPaint.setStrokeWidth(mProgressItemWidth);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setPathEffect(dashPathEffect);
        mProgressPaint.setStrokeWidth(mProgressItemWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mProgressRectangle.set(mProgressItemWidth / 2, mProgressItemWidth / 2, size - mProgressItemWidth / 2, size - mProgressItemWidth / 2);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundProgress(canvas);
        drawProgress(canvas);
    }

    private void drawBackgroundProgress(Canvas canvas) {
        canvas.drawArc(mProgressRectangle, mStartAngle, mSweepAngle, false, mBackgroundProgressPaint);
    }

    private void drawProgress(Canvas canvas) {
        canvas.drawArc(mProgressRectangle, mStartAngle, mSweepPercentage * mSweepAngle, false, mProgressPaint);
    }

    public void setStartAngle(int startAngle) {
        this.mStartAngle = startAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        this.mSweepAngle = sweepAngle;
    }

    public void setProgressItemWidth(int width) {
        this.mProgressItemWidth = width;
    }

    public void setBackgroundProgressColor(int color) {
        this.mBackgroundProgressColor = color;
    }

    public void setProgressColors(int[] colors) {
        this.mProgressColors = colors;
    }

    public void setSweepPercentage(float percentage){
        this.mSweepPercentage = percentage;
    }

    public void invalidateProgress() {
        // 最后一个参数设置position，不设的话每个颜色渲染的角度不会根据percentage做变化
        SweepGradient sweepGradient = new SweepGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mProgressColors, null);
        Matrix gradientMatrix = new Matrix();
        // SweepGradient从degree为0开始渲染(三点钟方向)，需要转置Matrix以旋转角度
        gradientMatrix.preRotate(mStartAngle, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        sweepGradient.setLocalMatrix(gradientMatrix);
        mProgressPaint.setShader(sweepGradient);
        invalidate();
    }
}
