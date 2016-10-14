package com.example.wangweimin.customerview;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by wangweimin on 16/10/14.
 */

public class ViewUtils {
    public static int convertDp2Px(float dp) {
        Resources r = App.mContext.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics());
    }
}
