package com.example.wangweimin.customerview.view.pathAnimation;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 介绍：一种填充loading的动画View
 * 继承View的好处是 xml可以预览 ，也可以代码动态设置
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/11/3.
 */

public class LoadingPathAnimView extends PathAnimView {
    public LoadingPathAnimView(Context context) {
        this(context, null);
    }

    public LoadingPathAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPathAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("WANGWEIMIN")));
    }
}