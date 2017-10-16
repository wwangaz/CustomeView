package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.wangweimin.customerview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: wayne
 * Date: 2017/10/16
 * Description: describe the class here
 */

public class ConstraintCircleActivity extends AppCompatActivity {

    @BindView(R.id.center_img)
    ImageView centerImg;

    @BindView(R.id.constraint_layout)
    ConstraintLayout mConstraintLayout;

    ConstraintSet bigImage = new ConstraintSet();
    ConstraintSet smallImage = new ConstraintSet();
    boolean mOld = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smallImage.clone(this, R.layout.activity_constraint_small);
        setContentView(R.layout.activity_constraint_circle);
        ButterKnife.bind(this);
        bigImage.clone(mConstraintLayout);

        centerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(mConstraintLayout);
                if(mOld = !mOld){
                    smallImage.applyTo(mConstraintLayout);
                }else {
                    bigImage.applyTo(mConstraintLayout);
                }
            }
        });
    }
}
