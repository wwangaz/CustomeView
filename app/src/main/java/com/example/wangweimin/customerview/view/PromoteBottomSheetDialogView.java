package com.example.wangweimin.customerview.view;

import android.app.Activity;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangweimin.customerview.adapter.BaseRecyclerListAdapter;

import java.util.List;

/**
 * Created by wangweimin on 16/5/11.
 */
public class PromoteBottomSheetDialogView<T> {

    final private int MEDIATE_HEIGHT = 600;

    private BaseRecyclerListAdapter mAdapter;

    public PromoteBottomSheetDialogView(final Activity activity, List<T> list) {

        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        final View view = LayoutInflater.from(activity).inflate(getBottomViewId(), null);

        ListRecyclerView mListView = (ListRecyclerView) view.findViewById(getBottomViewListId());
        TextView dismissBtn = (TextView) view.findViewById(getBottomBtnId());

        // TODO: 16/10/17 adapter initialize

        mListView.setAdapter(mAdapter);

        mAdapter.refreshViewByReplaceData(list);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                //process onclicl
                dialog.dismiss();
            }
        });

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        View parentView = (View) view.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parentView);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height < MEDIATE_HEIGHT) {
            layoutParams.height = MEDIATE_HEIGHT;
            view.setLayoutParams(layoutParams);
        }

        behavior.setPeekHeight(MEDIATE_HEIGHT);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parentView.getLayoutParams();
        params.gravity = Gravity.TOP;
        parentView.setLayoutParams(params);
        dialog.show();

    }

    public void setAdapter(BaseRecyclerListAdapter adapter){
        mAdapter = adapter;
    }

    public int getBottomViewId() {
        return 0;
    }

    public int getBottomViewListId() {
        return 0;
    }

    public int getBottomBtnId() {
        return 0;
    }
}

