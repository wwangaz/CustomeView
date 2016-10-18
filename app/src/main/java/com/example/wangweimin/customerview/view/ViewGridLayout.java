package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.entity.CustomView;
import com.example.wangweimin.customerview.view.base.BaseGridLayout;

/**
 * Created by wangweimin on 16/10/18.
 */

public class ViewGridLayout extends BaseGridLayout<CustomView> {

    public ViewGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGridLayout(Context context) {
        super(context);
    }

    @Override
    protected View getItemView(AppCompatActivity mActivity, final int position, final ItemChangedListener<CustomView> itemChangedListener) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.item_view, null);
        TextView name = (TextView) view.findViewById(R.id.view_name);
        ImageView icon = (ImageView) view.findViewById(R.id.view_icon);

        final CustomView customView = dataList.get(position);
        if (customView != null) {
            name.setText(customView.name);
            icon.setImageResource(customView.iconId);
        }

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemChangedListener.itemChanged(position, customView);
            }
        });
        return view;
    }
}
