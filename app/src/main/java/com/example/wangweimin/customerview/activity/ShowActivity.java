package com.example.wangweimin.customerview.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.view.DashboardView;
import com.example.wangweimin.customerview.view.Rotate3DLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangweimin on 16/10/17.
 */

public class ShowActivity extends Activity {

    /**
     * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
     */
    @BindView(R.id.slidingLayout)
    Rotate3DLayout slidingLayout;

    /**
     * menu按钮，点击按钮展示左侧布局，再点击一次隐藏左侧布局。
     */
    @BindView(R.id.menuButton)
    Button menuButton;

    @BindView(R.id.dashboard)
    DashboardView dashboardView;

    @BindView(R.id.content)
    RelativeLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        // 将监听滑动事件绑定在contentLayout上
        slidingLayout.setScrollEvent(dashboardView);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidingLayout.isLeftLayoutVisible()) {
                    slidingLayout.scrollToRightLayout();
                } else {
                    slidingLayout.scrollToLeftLayout();
                }
            }
        });
        dashboardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dashboardView.invalidateProgress();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    dashboardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    dashboardView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
}
