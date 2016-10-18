package com.example.wangweimin.customerview.view.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.adapter.BaseRecyclerListAdapter;
import com.example.wangweimin.customerview.view.DividerItemDecoration;

/**
 * Created by wangweimin on 16/10/17.
 */

public class ListRecyclerView extends RecyclerView {

    /**
     * 无分页信息，即一次性加载时使用
     */
    public static int PAGELIMIT_NONE = -1;

    /**
     * 「更多」加载完成
     */
    private boolean loadBottomDataCompleted = true;

    /**
     * 分别为加载失败、空数据、加载中显示的页面
     */
    private View mFailedView, mEmptyView, mLoadingView;

    /**
     * 滑动到底部时触发的事件
     */
    private OnRecyclerViewScrollBottomListener scrollBottomListener;

    /**
     * divider
     */
    private Drawable mDivider;

    private ItemDecoration itemDecoration;

    /**
     * 定义滑动到底部时的监听器
     */
    public interface OnRecyclerViewScrollBottomListener {
        void requestNextPage();
    }

    /**
     * RecyclerView的Scroll监听器
     */
    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    loadBottomDataCompleted &&
                    getAdapter()
                            .getItemViewType(((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition()) == BaseRecyclerListAdapter.TYPE_FOOTER) {
                if (scrollBottomListener != null) {
                    loadBottomDataCompleted = false;
                    scrollBottomListener.requestNextPage();
                }
            }
        }

    };

    public ListRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public ListRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListRecyclerView, 0, 0);
        mDivider = getResources().getDrawable(a.getResourceId(R.styleable.ListRecyclerView_recycler_divider, R.drawable.deep_divider));
        init(context);
        a.recycle();
    }

    public ListRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListRecyclerView, defStyle, 0);
        mDivider = getResources().getDrawable(a.getResourceId(R.styleable.ListRecyclerView_recycler_divider, R.drawable.deep_divider));
        init(context);
        a.recycle();
    }

    @Override
    public boolean canScrollVertically(int direction) {
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
        }
        return super.canScrollVertically(direction);
    }

    /**
     * 设置RecyclerView滑动监听器
     */
    private void init(Context context) {
        setItemAnimator(new DefaultItemAnimator());
        setLayoutManager(new LinearLayoutManager(context));
        if (mDivider != null) {
            itemDecoration = new DividerItemDecoration(mDivider, DividerItemDecoration.VERTICAL_LIST);
        } else {
            itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST);
        }
        addItemDecoration(itemDecoration);
        addOnScrollListener(onScrollListener);
    }


    /**
     * 底部「更多」加载完成
     */
    public void onLoadNextComplete() {
        this.loadBottomDataCompleted = true;
    }

    /**
     * 无分页值时，列表数据加载完后控制显示的状态
     *
     * @param showFailedView 是否显示失败页面
     */
    public void renderViewByResult(boolean showFailedView) {
        renderViewByResult(showFailedView, PAGELIMIT_NONE, false);
    }

    /**
     * 列表数据加载完之后控制显示的状态
     *
     * @param showFailedView 是否显示失败页面
     * @param pageLimit      分页值
     */
    public void renderViewByResult(boolean showFailedView, int pageLimit, boolean isCurrentListEmpty) {
        onLoadNextComplete();
        if (showFailedView) {
            setVisibility(INVISIBLE);
            setFailedViewVisibility(VISIBLE);
            setEmptyViewVisibility(GONE);
            setLoadingViewVisibility(GONE);
        } else {
            BaseRecyclerListAdapter mAdapter = (BaseRecyclerListAdapter) getAdapter();
            int size = mAdapter.getData().size();
            setLoadingViewVisibility(GONE);
            setFailedViewVisibility(GONE);
            if (size == 0) {
                setVisibility(INVISIBLE);
                setEmptyViewVisibility(VISIBLE);
            } else {
                setVisibility(VISIBLE);
                setEmptyViewVisibility(GONE);
                mAdapter.setHasFooter(pageLimit != PAGELIMIT_NONE && size % pageLimit == 0 && !isCurrentListEmpty);
            }
        }
    }

    /**
     * 设置RecyclerView滑动到底部时的监听器
     *
     * @param onRecyclerViewScrollBottomListener
     */
    public void setOnRecyclerViewScrollBottomListener(OnRecyclerViewScrollBottomListener onRecyclerViewScrollBottomListener) {
        this.scrollBottomListener = onRecyclerViewScrollBottomListener;
    }

    /**
     * 设置首次加载失败时显示的页面
     *
     * @param failedView
     */
    public void setFailedView(View failedView) {
        this.mFailedView = failedView;
    }

    /**
     * 设置数据为空时显示的页面
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 设置列表数据加载中时显示的页面
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }

    /**
     * 设置失败页面是否显示
     *
     * @param visibility
     */
    private void setFailedViewVisibility(int visibility) {
        if (mFailedView != null) {
            mFailedView.setVisibility(visibility);
        }
    }

    /**
     * 设置空白页面是否显示
     *
     * @param visibility
     */
    private void setEmptyViewVisibility(int visibility) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visibility);
        }
    }

    /**
     * 设置加载中页面是否显示
     *
     * @param visibility
     */
    private void setLoadingViewVisibility(int visibility) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(visibility);
        }
    }

    public void removeItemDecoration() {
        removeItemDecoration(itemDecoration);
    }

}
