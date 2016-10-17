package com.example.wangweimin.customerview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by wangweimin on 16/10/14.
 */

public class ScreenUtils {
    public static int convertDp2Px(float dp) {
        Resources r = App.mContext.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics());
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    //将View以Y轴旋转degree度
    public static void rotateView(View view, float degree){
        
    }
}
