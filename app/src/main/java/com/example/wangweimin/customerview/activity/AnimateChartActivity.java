package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.view.AnimateChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangweimin on 16/12/28.
 */

public class AnimateChartActivity extends AppCompatActivity {
    @BindView(R.id.column1)
    AnimateChart chart1;

    @BindView(R.id.column2)
    AnimateChart chart2;

    @BindView(R.id.column3)
    AnimateChart chart3;

    private final static int MAX = 100;

    @OnClick(R.id.set_value)
    void setValue(View v){
        chart1.setData(20, MAX);
        chart2.setData(50, MAX);
        chart3.setData(80, MAX);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ButterKnife.bind(this);


    }
}
