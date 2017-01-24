package com.example.wangweimin.customerview.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by wangweimin on 17/1/24.
 */

public class SurfaceViewTestActivity extends AppCompatActivity {
    private final static String TAG = "SurfaceViewTestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends SurfaceView implements SurfaceHolder.Callback {
        private final static String TAG = "SurfaceView";
        private SurfaceHolder holder;
        private MyThread myThread;

        public MyView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.addCallback(this);
            myThread = new MyThread(holder);
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
    }

    class MyThread extends Thread {
        private final static String TAG = "surfaceThread";
        private final SurfaceHolder holder;
        private boolean isRun;

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
            isRun = true;
        }

        @Override
        public void run() {
            int count = 0;

            while (isRun) {
                Log.i(TAG, "thread running");
                Canvas c = null;
                try {
                    synchronized (holder) {
                        c = holder.lockCanvas();
                        if(c!= null) {
                            c.drawColor(Color.BLACK);
                            Paint p = new Paint();
                            p.setColor(Color.WHITE);
                            Rect r = new Rect(100, 50, 300, 250);
                            c.drawRect(r, p);
                            c.drawText("这是第" + (count++) + "秒", 100, 310, p);
                            Thread.sleep(1000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (c != null)
                        holder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }
}
