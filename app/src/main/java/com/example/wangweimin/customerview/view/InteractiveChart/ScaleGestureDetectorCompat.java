package com.example.wangweimin.customerview.view.InteractiveChart;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ScaleGestureDetector;

/**
 * Created by wangweimin on 16/8/17.
 */

public class ScaleGestureDetectorCompat {

    private ScaleGestureDetectorCompat(){

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCurrentSpanX(ScaleGestureDetector scaleGestureDetector){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return scaleGestureDetector.getCurrentSpanX();
        }else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCurrentSpanY(ScaleGestureDetector scaleGestureDetector){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return scaleGestureDetector.getCurrentSpanY();
        }else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }
}
