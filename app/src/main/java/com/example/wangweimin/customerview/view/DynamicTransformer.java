package com.example.wangweimin.customerview.view;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.wangweimin.customerview.ScreenUtils;
import com.example.wangweimin.customerview.entity.ImageInfo;

import java.util.List;

/**
 * Author: wayne
 * Date: 2018/1/17
 * Description: describe the class here
 */

public class DynamicTransformer implements ViewPager.PageTransformer {

    private List<ImageInfo> imageInfos;

    private ViewPager viewPager;

    private int screenWidth;

    public DynamicTransformer(List<ImageInfo> imageInfos, ViewPager pager) {
        this.imageInfos = imageInfos;
        this.viewPager = pager;
        screenWidth = ScreenUtils.getScreenWidth(pager.getContext());
    }

    @Override
    public void transformPage(View view, float position) {
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 1) {

            int currentIndex = (int) (view.getX() / screenWidth);
            ImageInfo currentImageInfo = imageInfos.get(currentIndex);
            ImageInfo nextImageInfo = imageInfos.get(currentIndex + (position >= 0 ? 1 : -1));

            int nextCalHeight = nextImageInfo.imageHeight * screenWidth / nextImageInfo.imageWidth;

            int curCalHeight = currentImageInfo.imageHeight * screenWidth / currentImageInfo.imageWidth;

            float height = curCalHeight + (nextCalHeight - curCalHeight) * Math.abs(position);

            ViewPager.LayoutParams params = (ViewPager.LayoutParams) viewPager.getLayoutParams();
            params.height = (int) height;
            viewPager.setLayoutParams(params);

        } else {
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}

