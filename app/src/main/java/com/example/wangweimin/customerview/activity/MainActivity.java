package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.wangweimin.customerview.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = new RelativeLayout(this);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        popupWindows(list);

    }


//        Resources res = getResources();

//        final PieChart pie = (PieChart) findViewById(R.id.pie_chart);
//        pie.setShowText(true);
//        pie.addItem("Agamemnon", 2f, res.getColor(R.color.seafoam));
//        pie.addItem("Bocephus", 3.5f, res.getColor(R.color.chartreuse));
//        pie.addItem("Calliope", 2.5f, res.getColor(R.color.emerald));
//        pie.addItem("Daedalus", 10f, res.getColor(R.color.bluegrass));
//        pie.addItem("Euripides", 1f, res.getColor(R.color.turquoise));
//        pie.addItem("Ganymede", 3f, res.getColor(R.color.slate));
//
//        (findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pie.setCurrentItem(0);
//            }
//        });

    private void popupWindows(List<String> list) {
        LinearLayout wheelLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.wheel_view, mainLayout, false);
        if (wheelLayout != null) {
            PopupWindow mPopupWindow;
            mPopupWindow = new PopupWindow(this);
            mPopupWindow.setContentView(wheelLayout);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);

            mPopupWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 0, 0);
        }

    }

}

