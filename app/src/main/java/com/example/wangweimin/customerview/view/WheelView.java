package com.example.wangweimin.customerview.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
    private final static int DEFAULT_ITEM_HEIGHT = 20;

    private Context context;
    private LinearLayout views;
    private Runnable scrollTask;
    private Paint paint;
    private int viewWidth;
    private int itemHeight;
    private int initialY;
    private int selectedIndex = 1;
    private int offset = 1;
    private int displayItemCount;
    private List<String> items;
    private OnWheelViewListener listener;

    private long newCheck = 1000;

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

        initialY = getScrollY();

        init(context);
    }

    public interface OnWheelViewListener {
        void onSelected(int selectedIndex, String item);
    }

    private void init(Context context) {
        items = new ArrayList<>();
        this.context = context;

        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        //处理滑动
        scrollTask = new Runnable() {
            @Override
            public void run() {
                if (itemHeight == 0)
                    return;

                int newY = getScrollY();
                if (initialY == newY) {
                    final int reminder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;

                    if (reminder == 0) {
                        selectedIndex = divided + offset;
                        onSelectedCallBack();
                    } else {
                        if (reminder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - reminder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSelectedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - reminder);
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
    }

    public void setOnSelectedListener(OnWheelViewListener listener) {
        this.listener = listener;
    }

    private void initData(List<String> data) {
        if (data != null && data.size() > 0)
            items = data;

        displayItemCount = offset * 2 + 1;

        views.removeAllViews();

        for (String item : items) {
            views.addView(createView(item));
        }

        refreshItemView(0);
    }

    //根据子项生成TextView
    private TextView createView(String item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(15);
        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
            itemHeight = getViewMeasureHeight(tv);
            views.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(params.width, itemHeight * displayItemCount));
        }
        return tv;
    }

    //选择项回调函数
    private void onSelectedCallBack() {
        if (listener != null)
            listener.onSelected(selectedIndex, items.get(selectedIndex)); //此处注意items的position要与selectedIndex保持同步
    }

    //根据选择项更新View
    private void refreshItemView(int newSelectedIndex) {
        int dIndex = newSelectedIndex - selectedIndex;
        this.selectedIndex = newSelectedIndex;
        //选中项变为蓝色
        if (views != null) {
            ((TextView) views.getChildAt(selectedIndex)).setTextColor(Color.parseColor("#83cde6"));
        }
        //滑动至选中项
        this.smoothScrollBy(0, dIndex * itemHeight);
        invalidate();
    }

    //dp转换为px
    private int dip2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    //获取view测量高度
    private int getViewMeasureHeight(View v) {
        return v.getHeight();
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

        if (viewWidth == 0)
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#83cde6"));
            paint.setStrokeWidth(dip2px(1f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(viewWidth / 6f, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6f, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth / 6f, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6f, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int i) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        super.setBackgroundDrawable(background);
    }
}
