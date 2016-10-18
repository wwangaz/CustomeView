package com.example.wangweimin.customerview.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.view.DashboardView;
import com.example.wangweimin.customerview.view.SlidFlopLayout;
import com.example.wangweimin.customerview.view.WaveProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangweimin on 16/10/18.
 */

public class FlopActivity extends AppCompatActivity {
    @BindView(R.id.wave_progress)
    public WaveProgressView waveProgressView;

    @BindView(R.id.dashboard)
    public DashboardView dashboardView;

    @BindView(R.id.sliding_menu)
    SlidFlopLayout slidFlopLayout;

    @BindView(R.id.reset)
    public Button reset;

    @OnClick(R.id.reset)
    public void reset() {
        waveProgressView.setPercent(0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flop);
        ButterKnife.bind(this);
        dashboardView.setSweepPercentage(1.0f);
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
        if (waveProgressView != null)
            waveProgressView.setPercent(0);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveProgressView.setPercent(0);
            }
        });
    }
}
