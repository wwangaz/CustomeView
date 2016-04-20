package com.example.wangweimin.customerview.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wangweimin.customerview.view.PieChart;
import com.example.wangweimin.customerview.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
