package com.example.wangweimin.customerview.view.zoomHeader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by wangweimin on 17/2/15.
 */

public class FlagImage extends View {
    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] orig = new float[COUNT * 2];
    private Bitmap bitmap;
    private int time;
    private int period;
    private int amplitude;
    private long preTime;

    public FlagImage(Context context, Bitmap bitmap) {
        super(context);

        setFocusable(true);

        this.bitmap = bitmap;

        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int index = 0;
            for (int y = 0; y <= HEIGHT; y++) {
                float fy = height * y / HEIGHT;
                for (int x = 0; x <= WIDTH; x++) {
                    float fx = width * x / WIDTH;
                    orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                    //将图像下移
                    orig[index * 2 + 1] = verts[index * 2 + 1] = fy + 100;
                    index++;
                }
            }
        }
        time = 0;
        period = 200;
        amplitude = 50;
        preTime = System.currentTimeMillis();
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long current = System.currentTimeMillis();
        time += (int) (current - preTime);
        preTime = current;
        time %= period;

        for (int j = 0; j <= HEIGHT; j++) {
            for (int i = 0; i <= WIDTH; i++) {
                verts[(j * (WIDTH + 1) + i) * 2 + 0] += 0;
                float offsetY = (float) Math.sin((float) i * 2.0 / WIDTH * 2 * Math.PI + 2 * time * Math.PI / period);
                verts[(j * (WIDTH + 1) + i) * 2 + 1] = orig[(j * WIDTH + i) * 2 + 1] + offsetY * amplitude;
            }
        }
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        invalidate();
    }
}
