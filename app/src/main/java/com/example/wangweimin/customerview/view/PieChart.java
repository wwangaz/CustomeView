package com.example.wangweimin.customerview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.example.wangweimin.customerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangweimin on 15/12/9.
 */
public class PieChart extends ViewGroup {
    List<Item> mData = new ArrayList<>();

    private float mTotal = 0.0f;

    private RectF mPieBounds = new RectF();

    private Paint mPiePaint;
    private Paint mTextPaint;
    private Paint mShadowPaint;

    private boolean mShowText = false;

    private float mTextX = 0.0f;
    private float mTextY = 0.0f;
    private float mTextWidth = 0.0f;
    private float mTextHeight = 0.0f;
    private int mTextPos = TEXTPOS_LEFT;

    private float mHighlightStrength = 1.15f;

    private float mPointerRadius = 2.0f;
    private float mPointerX;
    private float mPointerY;

    private int mPieRotation;

    private OnCurrentItemChangedListener mCurrentItemChangedListener = null;

    private int mTextColor;
    private PieView mPieView;
    private Scroller mScroller;
    private ValueAnimator mScrollAnimator;
    private GestureDetector mDetector;
    private PointerView mPointerView;

    private int mCurrentItemAngle;

    private int mCurrentItem = 0;
    private boolean mAutoCenterInSlice;
    private ObjectAnimator mAutoCenterAnimator;
    private RectF mShadowBounds = new RectF();


    public static final int TEXTPOS_LEFT = 0;

    public static final int TEXTPOS_RIGHT = 1;

    public static final int FLING_VELOCITY_DOWNSCALE = 4;

    public static final int AUTOCENTER_ANIM_DURATION = 250;

    public interface OnCurrentItemChangedListener {
        void OnCurrentItemChanged(PieChart source, int currentItem);
    }

    public PieChart(Context context) {
        super(context);
        init();
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieChart,
                0, 0
        );

        try {
            mShowText = a.getBoolean(R.styleable.PieChart_pieShowText, false);
            mTextY = a.getDimension(R.styleable.PieChart_labelY, 0.0f);
            mTextWidth = a.getDimension(R.styleable.PieChart_labelWidth, 0.0f);
            mTextHeight = a.getDimension(R.styleable.PieChart_labelHeight, 0.0f);
            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
            mTextColor = a.getColor(R.styleable.PieChart_labelColor, 0xff000000);
            mHighlightStrength = a.getFloat(R.styleable.PieChart_highlightStrength, 1.0f);
            mPieRotation = a.getInteger(R.styleable.PieChart_pieRotation, 0);
            mPointerRadius = a.getDimension(R.styleable.PieChart_pointerRadius, 2.0f);
            mAutoCenterInSlice = a.getBoolean(R.styleable.PieChart_autoCenterPointerInSlice, false);
        } finally {
            a.recycle();
        }

        init();
    }

    public boolean getShowText() {
        return mShowText;
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
        invalidate();
    }

    public float getTextY() {
        return mTextY;
    }

    public void setTextY(float textY) {
        mTextY = textY;
        invalidate();
    }

    public float getTextWidth() {
        return mTextWidth;
    }

    public void setTextWidth(float textWidth) {
        mTextWidth = textWidth;
        invalidate();
    }

    public float getTextHeight() {
        return mTextHeight;
    }

    public void setTextHeight(float textHeight) {
        mTextHeight = textHeight;
        invalidate();
    }

    public int getTextPos() {
        return mTextPos;
    }

    public void setTextPos(int textPos) {
        if (textPos != TEXTPOS_LEFT && textPos != TEXTPOS_RIGHT) {
            throw new IllegalArgumentException(
                    "Text Pos must be ether TEXTPOS_LEFT or TEXTPOS_RIGHT"
            );
        }
        mTextPos = textPos;
        invalidate();
    }

    public float getHighlightStrength() {
        return mHighlightStrength;
    }

    public void setHighlightStrength(float highlightStrength) {
        if (highlightStrength < 0.0f) {
            throw new IllegalArgumentException(
                    "highlight strength cannot be negative"
            );
        }
        mHighlightStrength = highlightStrength;
        invalidate();
    }

    public float getPointerRadius() {
        return mPointerRadius;
    }

    public void setPointerRadius(float pointerRadius) {
        mPointerRadius = pointerRadius;
        invalidate();
    }

    public int getPieRotation() {
        return mPieRotation;
    }

    public void setPieRotation(int pieRotation) {
        pieRotation = (pieRotation % 360 + 360) % 360;
        mPieRotation = pieRotation;
        mPieView.rotateTo(pieRotation);

        calcCurrentItem();
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(int currentItem) {
        setCurrentItem(currentItem, true);
    }

    private void setCurrentItem(int currentItem, boolean scrollIntoView) {
        mCurrentItem = currentItem;
        if (mCurrentItemChangedListener != null) {
            mCurrentItemChangedListener.OnCurrentItemChanged(this, currentItem);
        }
        if (scrollIntoView) {
            centerOnCurrentItem();
        }
        invalidate();
    }

    public void setOnCurrentItemChangedListener(OnCurrentItemChangedListener listener) {
        mCurrentItemChangedListener = listener;
    }

    public int addItem(String label, float value, int color) {
        Item it = new Item();
        it.mLabel = label;
        it.mValue = value;
        it.mColor = color;

        it.mHighlight = Color.argb(
                0xff,
                Math.min((int) (mHighlightStrength * (float) Color.red(color)), 0xff),
                Math.min((int) (mHighlightStrength * (float) Color.green(color)), 0xff),
                Math.min((int) (mHighlightStrength * (float) Color.blue(color)), 0xff)
        );

        mTotal += value;

        mData.add(it);

        onDataChanged();

        return mData.size() - 1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mDetector.onTouchEvent(event);

        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                stopScrolling();
                result = true;
            }
        }

        return result;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(mShadowBounds, mShadowPaint);

        if (getShowText()) {
            canvas.drawText(mData.get(mCurrentItem).mLabel, mTextX, mTextY, mTextPaint);
        }

        if (Build.VERSION.SDK_INT < 11) {
            tickScrollAnimation();
            if (!mScroller.isFinished()) {
                postInvalidate();
            }
        }
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextWidth * 2;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) mTextWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        int minh = (w - (int) mTextWidth) + getPaddingBottom() + getPaddingTop();

        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        if (mShowText) xpad += mTextWidth;

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        float diameter = Math.min(ww, hh);
        mPieBounds = new RectF(
                0.0f,
                0.0f,
                diameter,
                diameter
        );
        mPieBounds.offsetTo(getPaddingLeft(), getPaddingTop());

        mPointerY = mTextY - (mTextHeight / 2.0f);

        float pointerOffset = mPieBounds.centerY() - mPointerY;

        if (mTextPos == TEXTPOS_LEFT) {
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            if (mShowText) mPieBounds.offsetTo(mTextWidth, 0.0f);
            mTextX = mPieBounds.left;

            if (pointerOffset < 0) {
                pointerOffset = -pointerOffset;
                mCurrentItemAngle = 225;
            } else {
                mCurrentItemAngle = 135;
            }

            mPointerX = mPieBounds.centerX() - pointerOffset;
        } else {
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            mTextX = mPieBounds.right;

            if (pointerOffset < 0) {
                pointerOffset = -pointerOffset;
                mCurrentItemAngle = 315;
            } else {
                mCurrentItemAngle = 45;
            }
            mPointerX = mPieBounds.centerX() + pointerOffset;
        }

        mShadowBounds = new RectF(
                mPieBounds.left + 10,
                mPieBounds.bottom + 10,
                mPieBounds.right - 10,
                mPieBounds.bottom + 20
        );

        mPieView.layout((int) mPieBounds.left,
                (int) mPieBounds.top,
                (int) mPieBounds.right,
                (int) mPieBounds.bottom);

        mPieView.setPivot(mPieBounds.width() / 2, mPieBounds.height() / 2);

        mPointerView.layout(0, 0, w, h);
        onDataChanged();
    }

    private void calcCurrentItem() {
        int pointerAngle = (mCurrentItemAngle + 360 + mPieRotation) % 360;
        for (int i = 0; i < mData.size(); ++i) {
            Item it = mData.get(i);
            if (it.mStartAngle <= pointerAngle && pointerAngle <= it.mEndAngle) {
                if (i != mCurrentItem) {
                    setCurrentItem(i, false);
                }
                break;
            }
        }
    }

    private void onDataChanged() {
        int currentAngle = 0;
        for (Item it : mData) {
            it.mStartAngle = currentAngle;
            it.mEndAngle = (int) ((float) currentAngle + it.mValue * 360.0f / mTotal);
            currentAngle = it.mEndAngle;

            it.mShader = new SweepGradient(
                    mPieBounds.width() / 2.0f,
                    mPieBounds.height() / 2.0f,
                    new int[]{
                            it.mHighlight,
                            it.mHighlight,
                            it.mColor,
                            it.mColor
                    },
                    new float[]{
                            0,
                            (float) (360 - it.mEndAngle) / 360.0f,
                            (float) (360 - it.mStartAngle) / 360.0f,
                            1.0f
                    }
            );
        }
        calcCurrentItem();
        onScrollFinished();
    }

    private void init() {
        setLayerToSW(this);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setTextSize(mTextHeight);

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff101010);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        mPieView = new PieView(getContext());
        addView(mPieView);
        mPieView.rotateTo(mPieRotation);

        mPointerView = new PointerView(getContext());
        addView(mPointerView);

        if (Build.VERSION.SDK_INT >= 11) {
            mAutoCenterAnimator = ObjectAnimator.ofInt(PieChart.this, "PieRotation", 0);

            mAutoCenterAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mPieView.decelerate();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

        if (Build.VERSION.SDK_INT < 11) {
            mScroller = new Scroller(getContext());
        } else {
            mScroller = new Scroller(getContext(), null, true);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            mScrollAnimator = ValueAnimator.ofFloat(0, 1);
            mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    tickScrollAnimation();
                }
            });
        }

        mDetector = new GestureDetector(PieChart.this.getContext(), new GestureListener());

        mDetector.setIsLongpressEnabled(false);

        if (this.isInEditMode()) {
            Resources res = getResources();
//            addItem("Annabelle", 3, res.getColor(R.color.bluegrass));
//            addItem("Brunhilde", 4, res.getColor(R.color.chartreuse));
//            addItem("Carolina", 2, res.getColor(R.color.seafoam));
//            addItem("Dahlia", 3, res.getColor(R.color.seafoam));
//            addItem("Ekaterina", 1, res.getColor(R.color.slate));
        }
    }

    private void tickScrollAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.computeScrollOffset();
            setPieRotation(mScroller.getCurrY());
        } else {
            if (Build.VERSION.SDK_INT >= 11) {
                mScrollAnimator.cancel();
            }
            onScrollFinished();
        }
    }

    private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void stopScrolling() {
        mScroller.forceFinished(true);
        if (Build.VERSION.SDK_INT >= 11) {
            mAutoCenterAnimator.cancel();
        }

        onScrollFinished();
    }

    private void onScrollFinished() {
        if (mAutoCenterInSlice) {
            centerOnCurrentItem();
        } else {
            mPieView.decelerate();
        }
    }

    private void centerOnCurrentItem() {
        Item current = mData.get(getCurrentItem());
        int targetAngle = current.mStartAngle + (current.mEndAngle - current.mStartAngle) / 2;
        targetAngle -= mCurrentItemAngle;
        if (targetAngle < 90 && mPieRotation > 180) targetAngle += 360;

        if (Build.VERSION.SDK_INT >= 11) {
            mAutoCenterAnimator.setIntValues(targetAngle);
            mAutoCenterAnimator.setDuration(AUTOCENTER_ANIM_DURATION).start();
        }
    }

    private class Item {
        public String mLabel;
        public float mValue;
        public int mColor;

        public int mStartAngle;
        public int mEndAngle;

        public int mHighlight;
        public Shader mShader;
    }

    private class PieView extends View {
        private float mRotation = 0;
        private Matrix mTransform = new Matrix();
        private PointF mPivot = new PointF();

        public PieView(Context context) {
            super(context);
        }

        public void accelerate() {
            setLayerToHW(this);
        }

        public void decelerate() {
            setLayerToSW(this);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (Build.VERSION.SDK_INT < 11) {
                mTransform.set(canvas.getMatrix());
                mTransform.preRotate(mRotation, mPivot.x, mPivot.y);
                canvas.setMatrix(mTransform);
            }

            for (Item it : mData) {
                mPiePaint.setShader(it.mShader);
                canvas.drawArc(mBounds, 360 - it.mEndAngle, it.mEndAngle - it.mStartAngle, true, mPiePaint);
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mBounds = new RectF(0, 0, w, h);
        }

        RectF mBounds;

        public void rotateTo(float pieRotation) {
            mRotation = pieRotation;
            if (Build.VERSION.SDK_INT >= 11) {
                setRotation(pieRotation);
            } else {
                invalidate();
            }
        }

        public void setPivot(float x, float y) {
            mPivot.x = x;
            mPivot.y = y;
            if (Build.VERSION.SDK_INT >= 11) {
                setPivotX(x);
                setPivotY(y);
            } else {
                invalidate();
            }
        }
    }

    private class PointerView extends View {

        public PointerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawLine(mTextX, mPointerY, mPointerX, mPointerY, mTextPaint);
            canvas.drawCircle(mPointerX, mPointerY, mPointerRadius, mTextPaint);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float scrollTheta = vectorToScalarScroll(
                    distanceX,
                    distanceY,
                    e2.getX() - mPieBounds.centerX(),
                    e2.getY() - mPieBounds.centerY());
            setPieRotation(getPieRotation() - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float scrollTheta = vectorToScalarScroll(
                    velocityX,
                    velocityY,
                    e2.getX() - mPieBounds.centerX(),
                    e2.getY() - mPieBounds.centerY()
            );
            mScroller.fling(
                    0,
                    (int) getPieRotation(),
                    0,
                    (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
                    0,
                    0,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE
            );

            if (Build.VERSION.SDK_INT >= 11) {
                mScrollAnimator.setDuration(mScroller.getDuration());
                mScrollAnimator.start();
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mPieView.accelerate();
            if (isAnimationRunning()) {
                stopScrolling();
            }
            return true;
        }
    }

    private boolean isAnimationRunning() {
        return !mScroller.isFinished() || (Build.VERSION.SDK_INT >= 11 && mAutoCenterAnimator.isRunning());
    }

    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
        float l = (float) Math.sqrt(dx * dx + dy * dy);

        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }
}
