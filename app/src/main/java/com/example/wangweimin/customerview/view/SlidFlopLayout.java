package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.ScreenUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by wangweimin on 16/10/14.
 * 自定义的视图翻转菜单View
 */

public class SlidFlopLayout extends HorizontalScrollView {

    private int mScreenWidth;

    private int mMenuRightPadding;

    private int mMenuWidth;
    private int mHalfMenuWidth;

    private ViewGroup mMenu;
    private ViewGroup mContent;

    private boolean isOpen;

    public SlidFlopLayout(Context context) {
        this(context, null, 0);
    }

    public SlidFlopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidFlopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = ScreenUtils.getScreenWidth(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenuLayout, defStyleAttr, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenuLayout_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelOffset(attr, ScreenUtils.convertDp2Px(50f));
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) wrapper.getChildAt(0);
            mContent = (ViewGroup) wrapper.getChildAt(1);

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth = mMenuWidth / 2;
            mMenu.getLayoutParams().width = mMenuWidth;
            mContent.getLayoutParams().width = mScreenWidth;


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollx = getScrollX();
                if (scrollx > mHalfMenuWidth) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;
        float leftScale = 1 - 0.3f * scale; //确定menu的大小变化为1.0-0.7
        float rightScale = 0.6f + 0.4f * scale;//确定content的大小变化为0.8-1.0 也可以后期自己制定

        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        float rightDegree = (1 - scale) * 30;

        //这里为什么不设置pivot呢
        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, leftAlpha);
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.6f); //差速效果

        ViewHelper.setPivotX(mContent, 300);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleY(mContent, rightScale);
        ViewHelper.setScaleY(mContent, rightScale);
        ViewHelper.setRotationY(mContent, -rightDegree);
    }

    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    public void closeMenu() {
        if (!isOpen)
            return;
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }

    public void toggle() {
        if (isOpen)
            closeMenu();
        else
            openMenu();
    }
}

