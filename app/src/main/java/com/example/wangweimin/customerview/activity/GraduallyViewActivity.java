package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.wangweimin.customerview.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangweimin on 17/2/10.
 */

public class GraduallyViewActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.mask)
    ImageView mask;

    @BindView(R.id.start_btn)
    Button startBtn;

    private ValueAnimator animator;

    private float maskWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradually);
        ButterKnife.bind(this);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.start();
            }
        });


        animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setTarget(mask);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                maskWidth = mask.getWidth();
                ViewHelper.setTranslationX(mask, maskWidth * animation.getAnimatedFraction());
            }
        });
        animator.setDuration(5000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ViewHelper.setTranslationX(mask, -maskWidth * 0.5f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                ViewHelper.setTranslationX(mask, -maskWidth * 0.5f);
            }
        });
    }
}
