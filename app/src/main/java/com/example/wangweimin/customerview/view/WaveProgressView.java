package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangweimin on 16/10/9.
 */

public class WaveProgressView extends View {
    private final static String TAG = "WaveProgressView";
    //绘制波纹
    private Paint mWavePaint;
    //设置mode为XOR
    private PorterDuffXfermode mMod = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    //绘制圆
    private Paint mCirclePaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Path mPath;
    private int mWidth;
    private int mHeight;
    private int mPercent;
    private boolean isLeft;

    private int x;
    private int y;

    public WaveProgressView(Context context) {
        this(context, null);
    }

    public WaveProgressView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WaveProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPath = new Path();

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setColor(Color.parseColor("#33b5e5"));

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.parseColor("#99cc00"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
            mWidth = widthSize;

        if (heightMode == MeasureSpec.EXACTLY)
            mHeight = heightSize;

        y = mHeight;

        mBitmap = Bitmap.createBitmap(1200, 1200, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //x 为波动范围
        if (x > 300) {
            isLeft = true;
        } else if (x < 0) {
            isLeft = false;
        }

        //-50保证最后铺满,不断上升
        if (y > -50) y--;

        if (isLeft) {
            x = x - 2;
        } else x = x + 2;

        mPath.reset();
        mPath.moveTo(0, y);
        //绘制波浪线
        mPath.cubicTo(100 + x * 2, 200 + y, 100 + x * 2, y - 200, mWidth, y);
        //绘制右边界
        mPath.lineTo(mWidth, mHeight);
        //绘制左边界
        mPath.lineTo(0, mHeight);
        mPath.close();

        mBitmap.eraseColor(Color.parseColor("#00000000"));

        mCanvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mCirclePaint);
        mWavePaint.setXfermode(mMod);
        mCanvas.drawPath(mPath, mWavePaint);

        canvas.drawBitmap(mBitmap, 0, 0, null);

        postInvalidateDelayed(10);
    }

    public void setPercent(int percent) {
        mPercent = percent;
        y = (int) ((1 - mPercent / 100f) * mHeight);
        invalidate();
    }
}
