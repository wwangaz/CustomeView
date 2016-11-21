package com.example.wangweimin.customerview.activity;

import android.content.Context;
import android.content.Intent;
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
import butterknife.ButterKnife;

/**
 * Created by wangweimin on 16/10/18.
 * 用于展示自定义View的主页
 */

public class ViewListActivity extends AppCompatActivity {

    private final static String SLIDING_MENU_LAYOUT = "SlidingMenuLayout";
    private final static String SLID_FLOP_LAYOUT = "SlidFlopLayout";
    private final static String ROTATE_3D_LAYOUT = "Rotate3DLayout";
    private final static String AUTO_COMPLETE_VIEW = "AutoCompleteView";
    private final static String PIE_CHART_VIEW = "PieChartView";
    private final static String WAVE_PROGRESS_VIEW = "WaveProgressView";
    private final static String DASHBOARD_VIEW = "DashboardView";
    private final static String ROTATE_MENU_VIEW = "RotateMenuView";

    private Context mContext;

    @BindView(R.id.view_grids)
    ViewGridLayout gridLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        ButterKnife.bind(this);
        mContext = this;

        List<CustomView> views = new ArrayList<>();

        views.add(new CustomView(SLIDING_MENU_LAYOUT));
        views.add(new CustomView(SLID_FLOP_LAYOUT));
        views.add(new CustomView(ROTATE_3D_LAYOUT));
        views.add(new CustomView(AUTO_COMPLETE_VIEW));
        views.add(new CustomView(PIE_CHART_VIEW));
        views.add(new CustomView(WAVE_PROGRESS_VIEW));
        views.add(new CustomView(DASHBOARD_VIEW));
        views.add(new CustomView(ROTATE_MENU_VIEW));

        ItemChangedListener<CustomView> listener = new ItemChangedListener<CustomView>() {
            @Override
            public void itemChanged(int position, CustomView item) {
                switch (item.name) {
                    case SLIDING_MENU_LAYOUT:
                        startActivity(new Intent(mContext, ShowViewActivity.class));
                        break;
                    case SLID_FLOP_LAYOUT:
                        startActivity(new Intent(mContext, FlopActivity.class));
                        break;
                    case ROTATE_3D_LAYOUT:
                        startActivity(new Intent(mContext, ShowActivity.class));
                        break;
                    case AUTO_COMPLETE_VIEW:
                        startActivity(new Intent(mContext, MainActivity.class));
                        break;
                    case PIE_CHART_VIEW:
                        startActivity(new Intent(mContext, PieChartActivity.class));
                        break;
                    case ROTATE_MENU_VIEW:
                        startActivity(new Intent(mContext, RotateMenuActivity.class));
                        break;
                    case DASHBOARD_VIEW:
                        break;
                    case WAVE_PROGRESS_VIEW:
                        break;
                }
            }
        };
        gridLayout.setDataList(this, views, listener);
    }
}
