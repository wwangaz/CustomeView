package com.example.wangweimin.customerview.view.zoomHeader;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by wangweimin on 16/12/28.
 */

public class CtrlLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CtrlLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag){
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
