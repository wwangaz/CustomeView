package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangweimin on 16/10/17.
 */

public class Image3DView extends View {
    //源视图
    private View sourceView;

    private Bitmap sourceBitmap;

    private float sourceWidth;

    private float rotateDegree;

    private Matrix matrix = new Matrix();

    private Camera camera = new Camera();


    public Image3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSourceView(View view) {
        sourceView = view;
        sourceWidth = sourceView.getWidth();
    }

    public void clearSourceBitmap() {
        if (sourceBitmap != null)
            sourceBitmap = null;
    }

    public void setDegree(float degree) {
        rotateDegree = degree;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sourceBitmap == null)
            getSourceBitmap();

        float degree = (1 - getWidth() / sourceWidth) * rotateDegree;

        camera.save();
        camera.rotateY(degree);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(0, -getHeight() / 2);
        matrix.postTranslate(0, getHeight() / 2);
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }

    private void getSourceBitmap() {
        if (sourceView != null) {
            sourceView.setDrawingCacheEnabled(true);
            sourceView.layout(0, 0, sourceView.getWidth(), sourceView.getHeight());
            sourceView.buildDrawingCache();
            sourceBitmap = sourceView.getDrawingCache();
        }
    }

}
