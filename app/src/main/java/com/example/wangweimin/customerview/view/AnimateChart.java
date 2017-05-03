package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.ScreenUtils;

/**
 * Created by wayne on 2017/5/3.
 */

public class AnimateChart extends View {
        int MAX = 100;//最大
        int corner = 40;
        int data = 0;//显示的数
        int tempData = 0;
        int textPadding = 20;
        Paint mPaint;
        int mColor;

        Context mContext;

        public AnimateChart(Context context) {
            super(context);
            mContext = context;
        }

        public AnimateChart(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            mContext = context;
            initPaint();
        }

        public AnimateChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mContext = context;
            initPaint();
        }

        private void initPaint() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mColor = mContext.getResources().getColor(R.color.colorPrimary);
            mPaint.setColor(mColor);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);

            if(data == 0){
                mPaint.setTextSize(getWidth() / 2);
                RectF oval3 = new RectF(0, getHeight() -  ScreenUtils.convertPx2Dp(20), getWidth(), getHeight());// 设置个新的长方形
                canvas.drawRoundRect(oval3,  ScreenUtils.convertPx2Dp(corner),  ScreenUtils.convertPx2Dp(corner), mPaint);

                canvas.drawText("0",
                        getWidth() * 0.5f - mPaint.measureText("0") * 0.5f ,
                        getHeight() -  ScreenUtils.convertPx2Dp(20) - 2 * ScreenUtils.convertPx2Dp(textPadding),
                        mPaint);
                return;
            }

            //防止数值很大的的时候，动画时间过长
            int step = data /100 + 1;

            if(tempData < data - step){
                tempData = tempData + step;
            }else{
                tempData = data;
            }
            //画圆角矩形
            String S = tempData + "";

            //一个字和两,三个字的字号相同
            if(S.length()<4){
                mPaint.setTextSize(getWidth() / 2);
            }else{
                mPaint.setTextSize(getWidth() / (S.length()-1));
            }

            float textH = mPaint.ascent() + mPaint.descent();
            float MaxH = getHeight() - textH - 2 * ScreenUtils.convertPx2Dp(textPadding);
            //圆角矩形的实际高度
            float realH = MaxH / MAX * tempData;
            RectF oval3 = new RectF(0, getHeight() - realH, getWidth(), getHeight());// 设置个新的长方形
            canvas.drawRoundRect(oval3,  ScreenUtils.convertPx2Dp(corner),  ScreenUtils.convertPx2Dp(corner), mPaint);

            //写数字
            canvas.drawText(S,
                    getWidth() * 0.5f - mPaint.measureText(S) * 0.5f ,
                    getHeight() - realH - 2 * ScreenUtils.convertPx2Dp(textPadding),
                    mPaint);

            if(tempData != data){
                postInvalidate();
            }
        }

        public void setData(int data, int MAX){
            this.data = data;
            tempData = 0;
            this.MAX = MAX;
            postInvalidate();
        }


}
