package com.example.wangweimin.customerview.view.zoomHeader;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by wangweimin on 16/12/28.
 */

public class CtrlLinearLayout extends LinearLayout {
    private boolean isScrollEnabled = true;

    public CtrlLinearLayout(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag){
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return isScrollEnabled && super.canScrollVertically(direction);
    }
}
