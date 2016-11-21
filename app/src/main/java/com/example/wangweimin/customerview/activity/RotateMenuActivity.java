package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.ScreenUtils;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangweimin on 16/11/21.
 */

public class RotateMenuActivity extends AppCompatActivity {

    @BindView(R.id.menu_btn)
    ImageView menuBtn;

    @BindView(R.id.btn_top)
    TextView topBtn;

    @BindView(R.id.btn_middle)
    TextView middleBtn;

    @BindView(R.id.btn_bottom)
    TextView bottomBtn;

    @BindView(R.id.text)
    TextView textView;

    @OnClick({R.id.menu_btn, R.id.btn_top, R.id.btn_middle, R.id.btn_bottom})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_btn:
                if (isOpen) {
                    closeMenu();
                    isOpen = false;
                } else {
                    openMenu();
                    isOpen = true;
                }
                break;
            case R.id.btn_top:
                textView.setText("top");
                break;
            case R.id.btn_middle:
                textView.setText("middle");
                break;
            case R.id.btn_bottom:
                textView.setText("bottom");
                break;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_menu);
        ButterKnife.bind(this);
    }

    private boolean isOpen;

    private void openMenu() {

        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", 0, -ScreenUtils.convertDp2Px(200));
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("translationX", 0, -ScreenUtils.convertDp2Px(200));
        PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat("alpha", 0, 1.0f);

        ObjectAnimator topAnim = ObjectAnimator.ofPropertyValuesHolder(topBtn, holderY, holderAlpha);
        ObjectAnimator middleAnim = ObjectAnimator.ofPropertyValuesHolder(middleBtn, holderX, holderY, holderAlpha);
        ObjectAnimator bottomAnim = ObjectAnimator.ofPropertyValuesHolder(bottomBtn, holderX, holderAlpha);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(topAnim, middleAnim, bottomAnim);
        set.start();
    }

    private void closeMenu() {
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", -ScreenUtils.convertDp2Px(200), 0);
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("translationX", -ScreenUtils.convertDp2Px(200), 0);
        PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0);

        ObjectAnimator topAnim = ObjectAnimator.ofPropertyValuesHolder(topBtn, holderY, holderAlpha);
        ObjectAnimator middleAnim = ObjectAnimator.ofPropertyValuesHolder(middleBtn, holderX, holderY, holderAlpha);
        ObjectAnimator bottomAnim = ObjectAnimator.ofPropertyValuesHolder(bottomBtn, holderX, holderAlpha);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(topAnim, middleAnim, bottomAnim);
        set.start();
    }
}
