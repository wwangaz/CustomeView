package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.wangweimin.customerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangweimin on 16/5/24.
 */
public class MyPieChart extends View {
    List<Product> mData = new ArrayList<>();

    int[] mColors = new int[]{0xff1945aa, 0xff3b69d9, 0xff51c970, 0xffe5e71c, 0xffe7801c, 0xfff74c44};

    private Paint mTextPaint;
    private Paint mPiePaint;
    private Paint mDotPaint;

    private RectF mPieBounds = new RectF();

    private int mTextColor;
    private float mTextSize;
    private float mPieRadius;
    private float mDotRadius;
    private float mTextX;
    private float mTextY;
    private float mTabWidth;
    private float mTabHeight;
    private double mTotal;

    public MyPieChart(Context context) {
        super(context);
        init();
    }

    public MyPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyPieChart, 0, 0);

        try {
            mTextColor = a.getColor(R.styleable.MyPieChart_textColor, 0xff00000);
            mTextSize = a.getDimensionPixelSize(R.styleable.MyPieChart_textSize, 14);
            mPieRadius = a.getDimension(R.styleable.MyPieChart_pieRadius, 50);
            mDotRadius = a.getDimension(R.styleable.MyPieChart_dotRadius, 5);
        } finally {
            a.recycle();
        }

        init();
    }

    public int addProduct(String title, double value) {
        int colorIndex = mData.size() % mColors.length;

        Product product = new Product(title, value, mColors[colorIndex]);

        mData.add(product);
        mTotal += value;
        onDataChanged();
        return mData.size() - 1;
    }

    private void onDataChanged() {
        int currentAngle = 0;
        for (Product product : mData) {
            product.mStartAngle = currentAngle;
            product.mEndAngle = (int) ((float) currentAngle + product.mAmount * 360.0f / mTotal);
            currentAngle = product.mEndAngle;
        }
        invalidate();
    }

    private void init() {
        setLayerToSW(this);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextSize == 0)
            mTextSize = mTextPaint.getTextSize();
        else mTextPaint.setTextSize(mTextSize);

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Product product : mData) {
            mPiePaint.setColor(product.mColor);
            canvas.drawArc(mPieBounds, 360 - product.mEndAngle, product.mEndAngle - product.mStartAngle, true, mPiePaint);
        }

        float mTextItemHeight = mTabHeight / 3;

        float mTempTextX = mTextX;
        float mTempTextY = mTextY;

        for (int i = 0; i < mData.size(); i++) {
            Product product = mData.get(i);
            mDotPaint.setColor(product.mColor);

            String mItemText = product.mName + " " + String.format("%.2f%%", product.mAmount / mTotal * 100);

            canvas.drawCircle(mTempTextX + mTextItemHeight / 2, mTempTextY + mTextItemHeight / 2, mDotRadius, mDotPaint);
            canvas.drawText(mItemText, mTempTextX + mTextItemHeight / 2 + 20, mTempTextY + mTextItemHeight / 2 + 10, mTextPaint);

            if (i == mData.size() / 2 - 1) {
                mTempTextY = mTextY;
                mTempTextX += mTabWidth / 2 + 20;
            } else {
                mTempTextY += mTextItemHeight;
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + ((int) convertDp2Px(15) * 2 + (int) mPieRadius * 2) * 2;

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        int minh = getPaddingBottom() + getPaddingTop() + (int) mPieRadius * 2;

        setMeasuredDimension(w, minh);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        float diameter = Math.min(hh, mPieRadius * 2);
        mPieBounds = new RectF(0.0f, 0.0f, diameter, diameter);

        mPieBounds.offset(getPaddingLeft() + 10, 0);

        mTabHeight = hh / 2;
        mTabWidth = ww / 2;

        mTextX = mPieBounds.right + 30;
        mTextY = mPieBounds.bottom - mPieBounds.height() / 2;

        onDataChanged();
    }

    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private float convertDp2Px(float dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics());
    }

    public class Product {
        public String mName;
        public double mAmount;
        public int mColor;

        public int mStartAngle;
        public int mEndAngle;

        public Product(String name, double amount, int color) {
            mName = name;
            mAmount = amount;
            mColor = color;
        }
    }
}
