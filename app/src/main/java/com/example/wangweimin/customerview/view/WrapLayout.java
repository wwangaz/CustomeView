package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.wangweimin.customerview.ScreenUtils;

/**
 * Created by wangweimin on 16/10/14.
 */

public class WrapLayout extends ViewGroup {
    private final static String TAG = "WrapLayout";

    private final static int VIEW_MARGIN = ScreenUtils.convertDp2Px(5);

    public WrapLayout(Context context) {
        super(context);
    }

    public WrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int row = 0;
        int lengthX = l;
        int lengthY = t;

        for (int i = 0; i < count; i++) {
            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            lengthX += width + VIEW_MARGIN;
            lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height + t;

            if (lengthX + VIEW_MARGIN > r) {
                lengthX = width + VIEW_MARGIN + l;
                row++;
                lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height + t;
            }

            child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
        }

    }
}
