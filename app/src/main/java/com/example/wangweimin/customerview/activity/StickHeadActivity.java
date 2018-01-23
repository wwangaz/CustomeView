package com.example.wangweimin.customerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.entity.GroupItem;
import com.example.wangweimin.customerview.view.StickyHeadDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: wayne
 * Date: 2018/1/4
 * Description: describe the class here
 */

public class StickHeadActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private StickHeadAdapter stickHeadAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_head);
        ButterKnife.bind(this);

        final GroupItem item1 = new GroupItem(0);
        item1.setData("name", "group1");

        final GroupItem item2 = new GroupItem(10);
        item2.setData("name", "group4");

        final GroupItem item3 = new GroupItem(5);
        item3.setData("name", "group3");

        final GroupItem item4 = new GroupItem(1);
        item4.setData("name", "group2");

        stickHeadAdapter = new StickHeadAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new StickyHeadDecoration(this, R.layout.group_view, new StickyHeadDecoration.DecorationCallback() {
            @Override
            public void setGroup(List<GroupItem> groupItems) {
                groupItems.add(item1);
                groupItems.add(item2);
                groupItems.add(item3);
                groupItems.add(item4);
            }

            @Override
            public void buildGroupView(View groupView, GroupItem item) {
                TextView textView = (TextView) groupView.findViewById(R.id.group_text);
                textView.setText((String) item.getData("name"));
            }
        }));

        recyclerView.setAdapter(stickHeadAdapter);

        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            stringList.add(i + "");
        }

        stickHeadAdapter.setStringList(stringList);
    }

    public class StickHeadAdapter extends RecyclerView.Adapter<StickHeadViewHolder> {

        List<String> stringList = new ArrayList<>();

        @Override
        public StickHeadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stick_head, parent, false);
            return new StickHeadViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StickHeadViewHolder holder, int position) {
            holder.itemText.setText(stringList.get(position));
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
            notifyDataSetChanged();
        }
    }

    public class StickHeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_text)
        TextView itemText;

        public StickHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
