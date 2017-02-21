package com.example.wangweimin.customerview.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by wangweimin on 17/1/24.
 */

public class SurfaceViewTestActivity extends AppCompatActivity {
    private final static String TAG = "SurfaceViewTestActivity";
    private Paint mPaint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    class MyView extends SurfaceView implements SurfaceHolder.Callback {
        private final static String TAG = "SurfaceView";
        private SurfaceHolder holder;
        private MyThread myThread;
        private Path mPath;

        public MyView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.addCallback(this);
            mPath = new Path();
            myThread = new MyThread(holder, mPath);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated()");
            myThread.isRun = true;
            myThread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "surfaceDestroyed()");
            myThread.isRun = false;
            try{
                myThread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    }

    class MyThread extends Thread {
        private final static String TAG = "surfaceThread";
        private final SurfaceHolder holder;
        private boolean isRun;
        private Path mPath;

        public MyThread(SurfaceHolder holder, Path path) {
            this.holder = holder;
            isRun = true;
            mPath = path;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            while (isRun) {
                Log.i(TAG, "thread running");
                draw();
            }
            long end = System.currentTimeMillis();
            if(end -start < 100){
                try{
                    Thread.sleep(100 - (end - start));
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

        private void draw(){
            Canvas c = null;
            try {
                synchronized (holder) {
                    c = holder.lockCanvas();
                    if(c!= null) {
                        c.drawColor(Color.WHITE);
                        c.drawPath(mPath, mPaint);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
        }
    }
}
