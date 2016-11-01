package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.view.PieChartView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangweimin on 16/10/18.
 * 饼状图
 */

public class PieChartActivity extends AppCompatActivity {

    @BindView(R.id.pie_chart)
    PieChartView pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        ButterKnife.bind(this);

        pieChart = (PieChartView) findViewById(R.id.pie_chart);

        for (int i = 0; i < 6; i++) {
            pieChart.addProduct("产品" + i, 1);
        }
    }
}
