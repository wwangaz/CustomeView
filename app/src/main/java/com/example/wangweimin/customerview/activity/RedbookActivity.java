package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.entity.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: wayne
 * Date: 2018/1/17
 * Description: describe the class here
 */

public class RedbookActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager viewPager;

    private List<ImageInfo> imageInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_redbook);
        imageInfos = new ArrayList<>();
    }

    public class pagerAdaper extends PagerAdapter{

        private List<ImageInfo> imageInfos;

        public pagerAdaper(List<ImageInfo> imageInfos) {
            this.imageInfos = imageInfos;
        }

        @Override
        public int getCount() {
            return imageInfos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            return super.instantiateItem(container, position);
        }
    }
}
