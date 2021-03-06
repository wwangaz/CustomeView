package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.view.WheelView;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteActivity extends AppCompatActivity {

    private final static String TAG = AutoCompleteActivity.class.getSimpleName();

    private RelativeLayout mainLayout;
    private TextView showSelect;
    private Button showSelectBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        showSelect = (TextView) findViewById(R.id.show_select_tx);
        showSelectBtn = (Button) findViewById(R.id.show_select_bt);

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + " ");
        }
        showSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindows(list);
            }
        });

    }

    private void popupWindows(List<String> list) {
        LinearLayout wheelLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.wheel_layout, mainLayout, false);
        if (wheelLayout != null) {
            final PopupWindow mPopupWindow = new PopupWindow(this);
            mPopupWindow.setContentView(wheelLayout);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);

            //对WheelView进行初始化
            WheelView wheelView = (WheelView) wheelLayout.findViewById(R.id.wheel_view);

            TextView cancel = (TextView) wheelLayout.findViewById(R.id.cancel_action);
            TextView sure = (TextView) wheelLayout.findViewById(R.id.sure_action);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                }
            };

            cancel.setOnClickListener(listener);
            sure.setOnClickListener(listener);

            // TODO: 16/4/29 应将WheelView的高度设定与offset有关
            wheelView.initData(list);
            wheelView.setOnSelectedListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    showSelect.setText(item);
                }
            });

            mainLayout.post(new Runnable() {
                @Override
                public void run() {
                    mPopupWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 0, 0);
                }
            });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

}

