package com.example.wangweimin.customerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wangweimin.customerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangweimin on 16/10/17.
 */

public abstract class BaseRecyclerListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    /**
     * Item的type为header
     */
    public static final int TYPE_HEADER = 1;

    /**
     * Item的type为正常数据项
     */
    public static final int TYPE_ITEM = 2;

    /**
     * Item的type为footer
     */
    public static final int TYPE_FOOTER = 3;

    /**
     * 默认加载的footer
     */
    private static final int DEFAULT_FOOTER_LAYOUT = R.layout.loading_cell;

    /**
     * 数据列表
     */
    protected List<T> mDataList;

    /**
     * 用于记录是否有header，默认无
     */
    private boolean hasHeader = false;

    /**
     * 用于记录是否有footer，默认无
     */
    private boolean hasFooter = false;

    /**
     * List Item的点击事件
     */
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    /**
     * 定义List Item点击监听器
     */
    public interface OnRecyclerViewItemClickListener {
        void onItemClickListener(View view, int position);
    }

    /**
     * List Item的长按点击事件
     */
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;

    /**
     * 定义List Item长按点击监听器
     */
    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClickListener(View view, int position);
    }

    /**
     * 数据Item对应的ViewHolder，子类必须Override
     *
     * @param viewGroup
     * @return
     */
    protected abstract VH onCreateItemViewHolder(ViewGroup viewGroup);

    /**
     * 数据Item View的渲染
     *
     * @param viewHolder
     * @param position
     */
    protected abstract void onBindItemViewHolder(VH viewHolder, int position);

    /**
     * 初始化数据
     */
    public BaseRecyclerListAdapter() {
        this.mDataList = new ArrayList<>();
    }

    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        throw new IllegalAccessError("Please Override this Method if you want to add a 「Header」 to RecyclerView, or " +
                "you should not call setHasHeader(true)");
    }

    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(DEFAULT_FOOTER_LAYOUT, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) {
            return TYPE_HEADER;
        } else if (hasFooter && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(viewGroup);
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(viewGroup);
        } else {
            return onCreateItemViewHolder(viewGroup);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final int realPosition = position - getHeaderCount();
            onBindItemViewHolder((VH) viewHolder, realPosition);
            if (onRecyclerViewItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRecyclerViewItemClickListener.onItemClickListener(viewHolder.itemView, realPosition);
                    }
                });
            }
            if (onRecyclerViewItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onRecyclerViewItemLongClickListener.onItemLongClickListener(viewHolder.itemView, realPosition);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return getCount() + getHeaderCount() + getFooterCount();
    }

    /**
     * Add数据，然后刷新
     *
     * @param list
     */
    public void refreshViewByAddData(List<T> list) {
        if (list == null) {
            return;
        }
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Replace所有数据，然后刷新
     *
     * @param list
     */
    public void refreshViewByReplaceData(List<T> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    /**
     * 设置是否有header
     *
     * @param hasHeader
     */
    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    /**
     * 设置是否有footer
     *
     * @param hasFooter
     */
    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
        notifyItemRangeChanged(getCount() + getHeaderCount(), 1);
    }

    /**
     * 设置Item View点击监听器
     *
     * @param clickListener
     */
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener clickListener) {
        this.onRecyclerViewItemClickListener = clickListener;
    }

    /**
     * 设置Item View长按点击监听器
     *
     * @param clickListener
     */
    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener clickListener) {
        this.onRecyclerViewItemLongClickListener = clickListener;
    }

    /**
     * 获取header数量
     *
     * @return
     */
    private int getHeaderCount() {
        return hasHeader ? 1 : 0;
    }

    /**
     * 获取footer数量
     *
     * @return
     */
    private int getFooterCount() {
        return hasFooter ? 1 : 0;
    }

    /**
     * 获取数据列表
     *
     * @return
     */
    public List<T> getData() {
        return mDataList;
    }

    /**
     * 获取数据长度
     *
     * @return
     */
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}

