package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.ScreenUtils;

import java.util.List;

/**
 * Created by wangweimin on 16/10/17.
 * 自定义的GridLayout的基类
 */
public abstract class BaseGridLayout<T> extends GridLayout {


    protected LayoutInflater mLayoutInflater;

    /**
     * 列数
     */
    private int columnCount;

    /**
     * 水平间距
     */
    private int horizontalSpace;

    /**
     * 垂直间距
     */
    private int verticalSpace;

    /**
     * 屏幕宽度
     */
    private int screenWidth;

    /**
     * 每个Grid item组装的数据
     */
    protected List<T> dataList;

    /**
     * 缺省的GridLayout列数
     */
    private static final int DEFAULT_COLUMN_COUNT = 2;

    /**
     * 缺省的水平间距
     */
    private static final int DEFAULT_HORIZONTAL_SPACE = 0;

    /**
     * 缺省的垂直间距
     */
    private static final int DEFAULT_VERTICAL_SPACE = 0;

    /**
     * 子类需要实现的方法，需要渲染的Item
     *
     * @param mActivity
     * @param position
     * @param itemChangedListener
     * @return
     */
    protected abstract View getItemView(AppCompatActivity mActivity, int position, ItemChangedListener<T> itemChangedListener);

    /**
     * GridLayout的item选择发生变化时可以触发这个监听器
     *
     * @param <T>
     */
    public interface ItemChangedListener<T> {
        void itemChanged(int position, T item);
    }

    public BaseGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public BaseGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseGridLayout(Context context) {
        this(context, null);
    }

    /**
     * 初始化传入的参数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.mLayoutInflater = LayoutInflater.from(context);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseGridLayout, defStyle, 0);
        columnCount = a.getInteger(R.styleable.BaseGridLayout_column_count, DEFAULT_COLUMN_COUNT);
        horizontalSpace = a.getDimensionPixelSize(R.styleable.BaseGridLayout_horizontal_space, DEFAULT_HORIZONTAL_SPACE);
        verticalSpace = a.getDimensionPixelSize(R.styleable.BaseGridLayout_vertical_space, DEFAULT_VERTICAL_SPACE);
        setColumnCount(columnCount);
        setOrientation(HORIZONTAL);
        screenWidth = ScreenUtils.getScreenWidth(context);
    }

    /**
     * 传入值及对应的监听器
     *
     * @param activity
     * @param dataList
     * @param listener
     */
    public void setDataList(AppCompatActivity activity, List<T> dataList, ItemChangedListener<T> listener) {
        this.dataList = dataList;
        refreshView(activity, listener);
    }

    /**
     * 水平方向space
     *
     * @param horizontalSpace
     */
    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    /**
     * 垂直方向spacee
     *
     * @param verticalSpace
     */
    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    /**
     * Grid列数
     *
     * @param columnCount
     */
    public void setColumnCount(int columnCount) {
        super.setColumnCount(columnCount);
        this.columnCount = columnCount;
    }

    /**
     * Layout总宽度
     *
     * @param screenWidth
     */
    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * 刷新整个GridLayout
     *
     * @param activity
     * @param itemChangedListener
     */
    private void refreshView(AppCompatActivity activity, ItemChangedListener<T> itemChangedListener) {
        if (dataList == null) {
            return;
        }
        this.removeAllViews();
        int itemWidth = (screenWidth - (columnCount + 1) * horizontalSpace) / columnCount;
        int length = dataList.size();
        for (int i = 0; i < length; i++) {
            View itemView = getItemView(activity, i, itemChangedListener);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = itemWidth;
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.setGravity(Gravity.FILL);
            layoutParams.leftMargin = horizontalSpace;
            if (i < columnCount) {
                layoutParams.topMargin = verticalSpace;
            }
            layoutParams.bottomMargin = verticalSpace;
            if ((i + 1) % columnCount == 0) {
                layoutParams.rightMargin = horizontalSpace;
            } else {
                layoutParams.rightMargin = 0;
            }
            layoutParams.columnSpec = spec(UNDEFINED, 1);
            itemView.setLayoutParams(layoutParams);
            this.addView(itemView);
        }
        invalidate();
    }
}
