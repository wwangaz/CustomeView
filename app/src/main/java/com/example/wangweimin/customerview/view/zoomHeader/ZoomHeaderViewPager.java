package com.example.wangweimin.customerview.view.zoomHeader;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.wangweimin.customerview.view.zoomHeader.support.ZoomOutPageTransformer;

/**
 * Created by wangweimin on 16/12/29.
 */

public class ZoomHeaderViewPager extends ViewPager {
    public boolean canScroll = true;

    public ZoomHeaderViewPager(Context context) {
        super(context);
    }

    public ZoomHeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScroll && super.onTouchEvent(ev);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int position = getCurrentItem();
        if (position < 0) {
            return i;
        }else {
            if(i == childCount - 1){
                if(position > i)
                    position = i;

                return position;
            }

            if (i == position)
                return childCount - 1;
        }
        return i;
    }
}
