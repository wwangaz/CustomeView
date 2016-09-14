package com.example.wangweimin.customerview.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.wangweimin.customerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangweimin on 16/4/27.
 */
public class WheelView extends ScrollView {
    private final static int DEFAULT_ITEM_HEIGHT = 30;
    private final static int PADDING = 15;

    private LinearLayout views;
    private Runnable scrollTask;
    private Paint linePaint;
    private int viewWidth;
    private int initialY;
    private int itemHeight;
    private int selectedIndex = 1;
    private int offset = 1;
    private int displayItemCount;
    private List<String> items;
    private OnWheelViewListener listener;

    private long newCheck = 200;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.WheelView,
                0, 0
        );

        try {
            itemHeight = a.getDimensionPixelSize(R.styleable.WheelView_itemHeight, DEFAULT_ITEM_HEIGHT);
        } finally {
            a.recycle();
        }

        initialY = -1;

        init(context);
    }

    public interface OnWheelViewListener {
        void onSelected(int selectedIndex, String item);
    }

    private void init(Context context) {
        items = new ArrayList<>();

        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        //这个只是用来调整滑动的位置，然后得到选中项
        scrollTask = new Runnable() {
            @Override
            public void run() {
                if (itemHeight == 0)
                    return;

                final int displayItemHeight = itemHeight + PADDING * 2;

                final int newY = getScrollY();
                final int reminder = newY % displayItemHeight;
                final int divided = newY / displayItemHeight;

                //当newY == initialY的时候,滑动停止了,再对滑动位置进行微调
                if (newY == initialY) {
                    if (reminder == 0) {
                        selectedIndex = divided + offset;
                        onSelectedCallBack();
                    } else {
                        if (reminder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, newY - reminder + displayItemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSelectedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, newY - reminder);
                                    selectedIndex = divided + offset;
                                    onSelectedCallBack();
                                }
                            });
                        }
                    }
                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollTask, newCheck);
                }
            }
        };

        this.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                post(scrollTask);
            }
        });

    }

    public void setOnSelectedListener(OnWheelViewListener listener) {
        this.listener = listener;
    }

    public void initData(List<String> data) {
        if (data != null && data.size() > 0)
            items = data;

        displayItemCount = offset * 2 + 1;

        views.removeAllViews();


        for (String item : items) {
            views.addView(createView(item));
        }

        refreshItemView(0);
        this.smoothScrollTo(0, -itemHeight - 2 * PADDING);
    }

    //根据子项生成TextView
    private TextView createView(String item) {
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(PADDING);
        tv.setPadding(padding, padding, padding, padding);

        return tv;
    }

    //选择项回调函数
    private void onSelectedCallBack() {
        if (items != null) {
            selectedIndex = selectedIndex % items.size();

            if (listener != null)
                listener.onSelected(selectedIndex, items.get(selectedIndex)); //此处注意items的position要与selectedIndex保持同步
        }
        refreshItemView(selectedIndex);
    }

    //根据选择项更新View
    private void refreshItemView(int newSelectedIndex) {
        this.selectedIndex = newSelectedIndex;
        //选中项变为蓝色
        for (int i = 0; i < views.getChildCount(); i++) {
            if (i == selectedIndex)
                ((TextView) views.getChildAt(i)).setTextColor(Color.parseColor("#ff83cde6"));
            else ((TextView) views.getChildAt(i)).setTextColor(Color.GRAY);
        }
        invalidate();
    }

    //dp转换为px
    private int dip2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    //获取view测量高度
    private int getViewMeasureHeight(View v) {
        return v.getMeasuredHeight();
    }

    //获取选择子项的上下边界
    private float[] obtainSelectedAreaBorder() {
        float upperBorder = getY() - getHeight() / 3;
        float lowerBorder = upperBorder - getHeight() / 3;
        return new float[]{upperBorder, lowerBorder};
    }

    //为背景直绘两条线
    @Override
    public void setBackgroundDrawable(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth();
        }
        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setColor(Color.parseColor("#ff83cde6"));
            linePaint.setStrokeWidth(dip2px(1f));
        }

        Canvas canvas = new Canvas();
        canvas.drawLine(viewWidth / 6f, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6f, obtainSelectedAreaBorder()[0], linePaint);
        canvas.drawLine(viewWidth / 6f, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6f, obtainSelectedAreaBorder()[1], linePaint);
        background.draw(canvas);

        super.setBackgroundDrawable(background);
    }
}
