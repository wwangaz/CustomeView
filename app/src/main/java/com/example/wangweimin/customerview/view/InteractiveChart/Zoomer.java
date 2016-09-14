package com.example.wangweimin.customerview.view.InteractiveChart;

import android.content.Context;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by wangweimin on 16/8/17.
 */

public class Zoomer {

    private Interpolator mInterpolator;

    private int mAnimationDurationMills;

    private boolean mFinished = true;

    private float mCurrentZoom;

    /**
     * The time the zoom started
     */
    private long mStartRTC;

    private float mEndZoom;

    public Zoomer(Context context){
        mInterpolator = new DecelerateInterpolator();
        mAnimationDurationMills = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public void forceFinished(boolean finished){
        mFinished = finished;
    }

    public void abortAnimation(){
        mFinished = true;
        mCurrentZoom = mEndZoom;
    }

    public void startZoom(float endZoom){
        mStartRTC = SystemClock.elapsedRealtime();
        mEndZoom = endZoom;

        mFinished = false;
        mCurrentZoom = 1f;
    }

    public boolean computeZoom(){
        if(mFinished)
            return false;

        long tRTC = SystemClock.elapsedRealtime() - mStartRTC;
        if(tRTC >= mAnimationDurationMills){
            mFinished = true;
            mCurrentZoom = mEndZoom;
            return false;
        }

        float t = tRTC * 1f / mAnimationDurationMills;
        mCurrentZoom = mEndZoom * mInterpolator.getInterpolation(t);
        return true;
    }

    public float getCurrZoom(){
        return mCurrentZoom;
    }

}
