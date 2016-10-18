package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.entity.CustomView;
import com.example.wangweimin.customerview.view.ViewGridLayout;
import com.example.wangweimin.customerview.view.base.BaseGridLayout.ItemChangedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wangweimin on 16/10/18.
 */

public class ViewListActivity extends AppCompatActivity {

    private final static String SLIDING_MENU = "SlidingMenu";
    private final static String SLID_FLOP_VIEW = "SlidFlopView";
    private final static String ROTATE_3D_Layout = "Rotate3DLayout";
    private final static String PIE_CHART = "PieChart";
    private final static String WAVE_PROGRESS_VIEW = "WaveProgressView";
    private final static String DASHBOARD_VIEW = "DashboardView";

    @BindView(R.id.view_grids)
    ViewGridLayout gridLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<CustomView> views = new ArrayList<>();

        views.add(new CustomView("SlidingMenu"));
        views.add(new CustomView("SlidFlopView"));
        views.add(new CustomView("Rotate3DLayout"));
        views.add(new CustomView("PieChart"));
        views.add(new CustomView("WaveProgressView"));
        views.add(new CustomView("DashboardView"));

        ItemChangedListener<CustomView> listener = new ItemChangedListener<CustomView>() {
            @Override
            public void itemChanged(int position, CustomView item) {
                switch (item.name){

                }
            }
        };

        gridLayout.setDataList(this, views, listener);

    }
}
