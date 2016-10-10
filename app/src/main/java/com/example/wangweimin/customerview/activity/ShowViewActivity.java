package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.view.WaveProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangweimin on 16/10/9.
 */

public class ShowViewActivity extends AppCompatActivity {
    @BindView(R.id.wave_progress)
    public WaveProgressView waveProgressView;

    @BindView(R.id.reset)
    public Button reset;

    @OnClick(R.id.reset)
    public void reset() {
        waveProgressView.setPercent(0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_view);
        ButterKnife.bind(this);
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
