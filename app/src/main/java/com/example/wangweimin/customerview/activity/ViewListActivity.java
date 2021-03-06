package com.example.wangweimin.customerview.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    private final static String TAG = ViewListActivity.class.getSimpleName();

    private final static String SLIDING_MENU_LAYOUT = "SlidingMenuLayout";
    private final static String SLID_FLOP_LAYOUT = "SlidFlopLayout";
    private final static String ROTATE_3D_LAYOUT = "Rotate3DLayout";
    private final static String AUTO_COMPLETE_VIEW = "AutoCompleteView";
    private final static String PIE_CHART_VIEW = "PieChartView";
    private final static String WAVE_PROGRESS_VIEW = "WaveProgressView";
    private final static String GRADUALLY_VIEW = "GraduallyView";
    private final static String ROTATE_MENU_VIEW = "RotateMenuView";
    private final static String HACK_DIALOG = "HackDialog";
    private final static String CHANGE_ICON = "ChangeIcon";
    private final static String PATH_ANIMATION = "PathAnimation";
    private final static String SURFACE_VIEW = "SurfaceView";
    private final static String COLOR_MATRIX = "ColorMatrix";
    private final static String ANIMATE_CHART = "AnimateChart";
    private final static String CIRCLE_LOADING_VIEW = "CircleLoadingView";
    private final static String CIRCLE_CONSTRAINT = "CircleConstraint";
    private final static String STICK_HEAD_LIST = "StickHeadList";

    private Context mContext;

    private ComponentName mDefault;
    private ComponentName mTest;
    private PackageManager mPm;

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
        views.add(new CustomView(GRADUALLY_VIEW));
        views.add(new CustomView(ROTATE_MENU_VIEW));
        views.add(new CustomView(HACK_DIALOG));
        views.add(new CustomView(CHANGE_ICON));
        views.add(new CustomView(PATH_ANIMATION));
        views.add(new CustomView(SURFACE_VIEW));
        views.add(new CustomView(COLOR_MATRIX));
        views.add(new CustomView(ANIMATE_CHART));
        views.add(new CustomView(CIRCLE_LOADING_VIEW));
        views.add(new CustomView(CIRCLE_CONSTRAINT));
        views.add(new CustomView(STICK_HEAD_LIST));

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
                        startActivity(new Intent(mContext, AutoCompleteActivity.class));
                        break;
                    case PIE_CHART_VIEW:
                        startActivity(new Intent(mContext, PieChartActivity.class));
                        break;
                    case ROTATE_MENU_VIEW:
                        startActivity(new Intent(mContext, RotateMenuActivity.class));
                        break;
                    case GRADUALLY_VIEW:
                        startActivity(new Intent(mContext, GraduallyViewActivity.class));
                        break;
                    case WAVE_PROGRESS_VIEW:
                        break;
                    case HACK_DIALOG:
                        startActivity(new Intent(mContext, PullToLoadMoreActivity.class));
                        break;
                    case CHANGE_ICON:
                        changeIconTest();
                        break;
                    case PATH_ANIMATION:
                        startActivity(new Intent(mContext, PathAnimActivity.class));
                        break;
                    case SURFACE_VIEW:
                        startActivity(new Intent(mContext, SurfaceViewTestActivity.class));
                        break;
                    case COLOR_MATRIX:
                        startActivity(new Intent(mContext, ColorMatrixActivity.class));
                        break;
                    case ANIMATE_CHART:
                        startActivity(new Intent(mContext, AnimateChartActivity.class));
                        break;
                    case CIRCLE_LOADING_VIEW:
                        startActivity(new Intent(mContext, CircleLoadingViewActivity.class));
                        break;
                    case CIRCLE_CONSTRAINT:
                        startActivity(new Intent(mContext, ConstraintCircleActivity.class));
                        break;
                    case STICK_HEAD_LIST:
                        startActivity(new Intent(mContext, StickHeadActivity.class));
                        break;
                    default:
                        break;
                }
            }
        };
        gridLayout.setDataList(this, views, listener);

        mDefault = getComponentName();
        mTest = new ComponentName(getBaseContext(), "com.example.wangweimin.customerview.Test");
        mPm = getApplication().getPackageManager();

    }

    private void changeIconTest(){
        enableComponent(mTest);
        disableComponent(mDefault);
    }

    private void enableComponent(ComponentName componentName){
        mPm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName componentName){
        mPm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
