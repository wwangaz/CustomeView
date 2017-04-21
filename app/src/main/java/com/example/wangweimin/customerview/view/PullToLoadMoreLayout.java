package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by wangweimin on 17/4/20.
 */

public class PullToLoadMoreLayout extends ViewGroup {

    public final static String TAG = PullToLoadMoreLayout.class.getName();

    CustomScrollView topScrollView, bottomScrollView;

    VelocityTracker velocityTracker = VelocityTracker.obtain();

    Scroller scroller = new Scroller(getContext());

    private int scaledTouchSlop;

    private boolean topScrollViewIsBottom;

    private boolean bottomScrollViewIsTop;

    private int currentPos;

    private int speed = 200;

    private int topViewBottom;

    private int lastY;

    private boolean once;

    public PullToLoadMoreLayout(Context context) {
        super(context);
        init();
    }

    public PullToLoadMoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToLoadMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                topScrollView = (CustomScrollView) getChildAt(0);
                bottomScrollView = (CustomScrollView) getChildAt(1);
                topScrollView.setScrollListener(new CustomScrollView.ScrollListener() {
                    @Override
                    public void onScrollToBottom() {
                        topScrollViewIsBottom = true;
                    }

                    @Override
                    public void onScrollToTop() {

                    }

                    @Override
                    public void onScroll(int scrollY) {

                    }

                    @Override
                    public void notBottom() {
                        topScrollViewIsBottom = false;
                    }
                });

                bottomScrollView.setScrollListener(new CustomScrollView.ScrollListener() {
                    @Override
                    public void onScrollToBottom() {

                    }

                    @Override
                    public void onScrollToTop() {
                        bottomScrollViewIsTop = true;
                    }

                    @Override
                    public void onScroll(int scrollY) {
                        bottomScrollViewIsTop = scrollY == 0;
                    }

                    @Override
                    public void notBottom() {

                    }
                });
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = lastY - y;

                if (topScrollViewIsBottom) {
                    if (dy >= scaledTouchSlop && currentPos == 0) {
                        return true;
                    }
                }

                if (bottomScrollViewIsTop) {
                    if (dy < 0 && currentPos == 1) {
                        if (Math.abs(dy) > scaledTouchSlop) {
                            return true;
                        }
                    }
                }
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        velocityTracker.addMovement(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = lastY - y;
                scrollBy(0, dy);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                float yVelocity = velocityTracker.getYVelocity();
                if(currentPos == 0){
                    if(yVelocity <0 && yVelocity < -speed){
                        smoothScroll(topViewBottom);
                        currentPos = 1;
                    }else {
                        smoothScroll(0);
                    }
                }else {
                    if(yVelocity > 0 && yVelocity > speed){
                        smoothScroll(0);
                        currentPos = 0;
                    }else {
                        smoothScroll(topViewBottom);
                    }
                }
                break;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childTop = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(l, childTop, r, childTop + child.getMeasuredHeight());
            childTop += child.getMeasuredHeight();
        }
        topViewBottom = getChildAt(0).getBottom();
    }

    private void smoothScroll(int targetY){
        int dy = targetY - getScrollY();
        scroller.startScroll(getScrollX(), getScrollY(), 0, dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
